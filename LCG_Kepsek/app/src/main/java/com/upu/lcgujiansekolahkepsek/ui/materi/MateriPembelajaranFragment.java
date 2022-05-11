package com.upu.lcgujiansekolahkepsek.ui.materi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.adapter.MyMateriPembelajaranAdapter;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.common.MySwipeHelper;
import com.upu.lcgujiansekolahkepsek.model.MateriPembelajaranModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class MateriPembelajaranFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1234;
    private MateriPembelajaranViewModel materiPembelajaranViewModel;

    Unbinder unbinder;
    @BindView(R.id.recycler_menu)
    RecyclerView recycler_menu;
    AlertDialog dialog;
    LayoutAnimationController layoutAnimationController;
    MyMateriPembelajaranAdapter adapter;

    List<MateriPembelajaranModel> galeriModels;
    ImageView img_category;
    private Uri imageUri = null;

    private Uri suaraDeskripsiUri = null;

    FirebaseStorage storage;
    StorageReference storageReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        materiPembelajaranViewModel =
                ViewModelProviders.of(this).get(MateriPembelajaranViewModel.class);
        View root = inflater.inflate(R.layout.fragment_materi_pembelajaran, container, false);

        unbinder = ButterKnife.bind(this,root);
        initView();
        materiPembelajaranViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(),""+s,Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        materiPembelajaranViewModel.getCategoryListMultable().observe(getViewLifecycleOwner(), categoryModelList -> {
            dialog.dismiss();
            galeriModels = categoryModelList;
            adapter= new MyMateriPembelajaranAdapter(getContext(), galeriModels);
            recycler_menu.setAdapter(adapter);
            recycler_menu.setLayoutAnimation(layoutAnimationController);
        });

        return root;
    }

    private void initView() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(),recycler_menu,200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(),"Hapus",30,0, Color.parseColor("#333639"),
                        pos -> {
                            Common.materiPembelajaranModel = galeriModels.get(pos);
                            showDeleteDialog();

                        }));

                buf.add(new MyButton(getContext(),"Ubah",30,0, Color.parseColor("#560027"),
                        pos -> {
                            Common.materiPembelajaranModel = galeriModels.get(pos);
                            showUpdateDialog();

                        }));
            }
        };

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_create){
            showAddDialog();
        }


        return super.onOptionsItemSelected(item);

    }

    private void showDeleteDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Hapus");
        builder.setMessage("Apakah anda yakin ingin menghapus materi ini?");

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Hapus",((dialogInterface, i) -> deleteInformasi()));
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteInformasi() {
        FirebaseDatabase.getInstance()
                .getReference(Common.MATERI_REF)
                .child(Common.materiPembelajaranModel.getMenu_id())
                .removeValue()
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    materiPembelajaranViewModel.loadCategories();
                    Toast.makeText(getContext(), "Berhasil Hapus Materi", Toast.LENGTH_SHORT).show();
                });
    }

    private void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Ubah");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_materi,null);
        EditText edt_materi_name = (EditText) itemView.findViewById(R.id.edt_materi_name);
        EditText edt_penjelasan = (EditText) itemView.findViewById(R.id.edt_informasi_penjelasan);
        img_category = (ImageView) itemView.findViewById(R.id.img_informasi);

        edt_materi_name.setText(new StringBuilder("").append(Common.materiPembelajaranModel.getName()));
        edt_penjelasan.setText(new StringBuilder("").append(Common.materiPembelajaranModel.getPenjelasan()));
        Glide.with(getContext()).load(Common.materiPembelajaranModel.getImage()).into(img_category);

        img_category.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);

        });

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Proses", (dialogInterface, i) -> {

            Map<String,Object> updateData = new HashMap<>();
            updateData.put("name",edt_materi_name.getText().toString());
            updateData.put("penjelasan",edt_penjelasan.getText().toString());

            if(imageUri != null || suaraDeskripsiUri != null){
                dialog.setMessage("Uploading...");
                dialog.show();
                if(imageUri != null){
                    String unique_name = UUID.randomUUID().toString();
                    StorageReference imageFolder = storageReference.child("images/"+unique_name);

                    imageFolder.putFile(imageUri)
                            .addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnCompleteListener(task -> {
                        dialog.dismiss();
                        imageFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                            updateData.put("image",uri.toString());
                            updateInformasi(updateData);
                        });
                    }).addOnProgressListener(taskSnapshot -> {
                        double progress = (100* taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                        dialog.setMessage(new StringBuilder("Uploading: ").append(progress).append("%"));
                    });
                }
            }else {
                updateInformasi(updateData);
            }

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Data");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_materi,null);
        EditText edt_materi_name = itemView.findViewById(R.id.edt_materi_name);
        EditText edt_penjelasan = itemView.findViewById(R.id.edt_informasi_penjelasan);
        img_category = itemView.findViewById(R.id.img_informasi);

        Glide.with(getContext()).load(R.drawable.ic_image_gray_24dp).into(img_category);

        img_category.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);

        });

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Tambah", (dialogInterface, i) -> {

            MateriPembelajaranModel galeriModel = new MateriPembelajaranModel();
            galeriModel.setName(edt_materi_name.getText().toString());
            galeriModel.setPenjelasan(edt_penjelasan.getText().toString());
            if(imageUri != null){
                dialog.setMessage("Uploading...");
                dialog.show();

                if(imageUri != null){
                    String unique_name = UUID.randomUUID().toString();
                    StorageReference imageFolder = storageReference.child("images/"+unique_name);

                    imageFolder.putFile(imageUri)
                            .addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnCompleteListener(task -> {
                        dialog.dismiss();
                        imageFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                            galeriModel.setImage(uri.toString());
                            addInformasi(galeriModel);
                        });
                    }).addOnProgressListener(taskSnapshot -> {
                        double progress = (100* taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                        dialog.setMessage(new StringBuilder("Uploading: ").append(progress).append("%"));
                    });
                }
                //
            }else{
                addInformasi(galeriModel);
            }

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateInformasi(Map<String, Object> updateData) {
        FirebaseDatabase.getInstance()
                .getReference(Common.MATERI_REF)
                .child(Common.materiPembelajaranModel.getMenu_id())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    materiPembelajaranViewModel.loadCategories();
                    Toast.makeText(getContext(), "Berhasil Ubah Materi", Toast.LENGTH_SHORT).show();
                });
    }

    private void addInformasi(MateriPembelajaranModel galeriModel) {
        FirebaseDatabase.getInstance()
                .getReference(Common.MATERI_REF)
                .push()
                .setValue(galeriModel)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    materiPembelajaranViewModel.loadCategories();
                    Toast.makeText(getContext(), "Berhasil Tambah Materi", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            if(data != null && data.getData() != null){
                imageUri = data.getData();
                img_category.setImageURI(imageUri);
            }
        }
    }
}
