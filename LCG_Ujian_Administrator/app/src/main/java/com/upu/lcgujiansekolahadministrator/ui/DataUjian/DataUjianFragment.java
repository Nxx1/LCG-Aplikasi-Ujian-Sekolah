package com.upu.lcgujiansekolahadministrator.ui.DataUjian;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahadministrator.EventBus.ToastEvent;
import com.upu.lcgujiansekolahadministrator.R;
import com.upu.lcgujiansekolahadministrator.adapter.MyDataUjianAdapter;
import com.upu.lcgujiansekolahadministrator.adapter.SpinnerDaftarGuruAdapter;
import com.upu.lcgujiansekolahadministrator.common.BottomOffsetDecoration;
import com.upu.lcgujiansekolahadministrator.common.Common;
import com.upu.lcgujiansekolahadministrator.common.MySwipeHelper;
import com.upu.lcgujiansekolahadministrator.model.BankSoalModel;
import com.upu.lcgujiansekolahadministrator.model.GuruModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class DataUjianFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private android.app.AlertDialog dialog;

    private DataUjianViewModel kategoriSoalViewModel;

    private List<BankSoalModel> bankSoalModelList;


    Unbinder unbinder;
    @BindView(R.id.recycler_kategori_soal_list)
    RecyclerView recycler_kategori_list;

    @BindView(R.id.fab_tambah_produk)
    FloatingActionButton fab_tambah_produk;

    LayoutAnimationController layoutAnimationController;
    MyDataUjianAdapter adapter;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.kategori_bank_soal_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                startSearchProduk(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(view -> {
            EditText ed = (EditText) searchView.findViewById(R.id.search_src_text);
            ed.setText("");
            searchView.setQuery("",false);
            searchView.onActionViewCollapsed();
            menuItem.collapseActionView();
            kategoriSoalViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(),bankSoalModels -> {
                dialog.dismiss();
                bankSoalModelList = bankSoalModels;
                adapter= new MyDataUjianAdapter(getContext(), bankSoalModelList);
                recycler_kategori_list.setAdapter(adapter);
                recycler_kategori_list.setLayoutAnimation(layoutAnimationController);
            });

        });
    }

    private void startSearchProduk(String s){
        List<BankSoalModel> resultProduk = new ArrayList<>();
        for (int i = 0; i< Common.daftarKategoriBankSoalModel.size(); i++){
            BankSoalModel bankSoalModel = Common.daftarKategoriBankSoalModel.get(i);
            if(bankSoalModel.getNamaUjian().toLowerCase().contains(s)) {
                bankSoalModel.setPositionInList(i);
                resultProduk.add(bankSoalModel);
            }
        }
        kategoriSoalViewModel.getListMutableLiveData().setValue(resultProduk);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kategoriSoalViewModel =
                ViewModelProviders.of(this).get(DataUjianViewModel.class);
        View root = inflater.inflate(R.layout.data_ujian_fragment, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        kategoriSoalViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), bankSoalModels -> {
            if(bankSoalModels != null){
                bankSoalModelList = bankSoalModels;
                adapter = new MyDataUjianAdapter(getContext(), bankSoalModelList);
                recycler_kategori_list.setAdapter(adapter);
                recycler_kategori_list.setLayoutAnimation(layoutAnimationController);
            }
        });

        fab_tambah_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(),TambahProdukActivity.class);
                //startActivity(intent);
                showAddDialog();
            }
        });

        return root;
    }

    private void initViews() {

        setHasOptionsMenu(true);

        Common.dataUjianFragment = DataUjianFragment.this;

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recycler_kategori_list.setLayoutManager(layoutManager);
        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        recycler_kategori_list.addItemDecoration(bottomOffsetDecoration);
        //recycler_kategori_list.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));


        MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(),recycler_kategori_list,200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(),"Hapus",30,0, Color.parseColor("#333639"),
                        pos -> {
                            Common.bankSoalModelSelected = bankSoalModelList.get(pos);
                            showDeleteDialog();

                        }));

                buf.add(new MyButton(getContext(),"Ubah",30,0, Color.parseColor("#560027"),
                        pos -> {
                            Common.bankSoalModelSelected = bankSoalModelList.get(pos);
                            showUpdateDialog();

                        }));
            }
        };


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        // EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }

    public void showDeleteDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Hapus");
        builder.setMessage("Apakah anda yakin ingin menghapus ujian ini?\nData nilai pada ujian ini juga akan dihapus.");

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Hapus",((dialogInterface, i) -> deleteCategory()));
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCategory() {
        FirebaseDatabase.getInstance()
                .getReference(Common.BANK_SOAL_REF)
                .child(Common.bankSoalModelSelected.getKodeUndangan())
                .removeValue()
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    kategoriSoalViewModel.loadKategoriSoal();
                    Toast.makeText(getContext(), "Berhasil hapus data", Toast.LENGTH_SHORT).show();
                });
    }

    public void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Ubah");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_bank_soal,null);
        EditText edt_nama = (EditText) itemView.findViewById(R.id.edt_nama);
        EditText edt_kelas = (EditText) itemView.findViewById(R.id.edt_kelas);
        EditText edt_durasi = (EditText) itemView.findViewById(R.id.edt_durasi);

        Spinner spDaftarGuru = itemView.findViewById(R.id.sp_daftar_guru);

        List<GuruModel> guruModelList = new ArrayList<>();
        Map<String,Object> updateData = new HashMap<>();
        edt_nama.setText(new StringBuilder("").append(Common.bankSoalModelSelected.getNamaUjian()));
        edt_kelas.setText(new StringBuilder("").append(Common.bankSoalModelSelected.getKelas()));
        edt_durasi.setText(new StringBuilder("").append(Common.bankSoalModelSelected.getDurasi().toString()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference(Common.GURU_REF);

        fDatabaseRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    GuruModel guruModel = addressSnapshot.getValue(GuruModel.class);
                    guruModelList.add(guruModel);
                }

                SpinnerDaftarGuruAdapter spinAdapter = new SpinnerDaftarGuruAdapter(getContext(),
                        android.R.layout.simple_spinner_item,
                        guruModelList);
                spDaftarGuru.setAdapter(spinAdapter);
                spDaftarGuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        GuruModel guruModel = (GuruModel) spinAdapter.getItem(i);
                        Common.guruModelSelected = guruModel;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Proses", (dialogInterface, i) -> {

            updateData.put("namaUjian",edt_nama.getText().toString());
            updateData.put("kelas",edt_kelas.getText().toString());
            updateData.put("kodeUndangan",Common.bankSoalModelSelected.getKodeUndangan().toString());
            updateData.put("userCreator",Common.userModel.getKey().toString());
            updateData.put("durasi",Integer.valueOf(edt_durasi.getText().toString()));
            updateData.put("namaLengkapGuru",Common.guruModelSelected.getNamalengkap());
            updateData.put("usernameGuru",Common.guruModelSelected.getUsername());

            updateCategory(updateData);

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Data Ujian");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_bank_soal,null);
        EditText edt_nama = (EditText) itemView.findViewById(R.id.edt_nama);
        EditText edt_kelas = (EditText) itemView.findViewById(R.id.edt_kelas);
        EditText edt_durasi = (EditText) itemView.findViewById(R.id.edt_durasi);

        Spinner spDaftarGuru = itemView.findViewById(R.id.sp_daftar_guru);

        List<GuruModel> guruModelList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference(Common.GURU_REF);

        fDatabaseRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    GuruModel guruModel = addressSnapshot.getValue(GuruModel.class);
                    guruModelList.add(guruModel);
                }

                SpinnerDaftarGuruAdapter spinAdapter = new SpinnerDaftarGuruAdapter(getContext(),
                        android.R.layout.simple_spinner_item,
                        guruModelList);
                spDaftarGuru.setAdapter(spinAdapter);
                spDaftarGuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        GuruModel guruModel = (GuruModel) spinAdapter.getItem(i);
                        Common.guruModelSelected = guruModel;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        String kodeUndangan = String.valueOf(Common.daftarKategoriBankSoalModel.size()) + generatedKodeUndangan();

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Tambah", (dialogInterface, i) -> {

            BankSoalModel bankSoalModel = new BankSoalModel();
            bankSoalModel.setNamaUjian(edt_nama.getText().toString());
            bankSoalModel.setKelas(edt_kelas.getText().toString());
            bankSoalModel.setKodeUndangan(kodeUndangan);
            bankSoalModel.setUserCreator(Common.userModel.getKey());
            bankSoalModel.setDurasi(Integer.valueOf(edt_durasi.getText().toString()));
            bankSoalModel.setNamaLengkapGuru(Common.guruModelSelected.getNamalengkap());
            bankSoalModel.setUsernameGuru(Common.guruModelSelected.getUsername());

            addCategory(bankSoalModel);
        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateCategory(Map<String, Object> updateData) {
        FirebaseDatabase.getInstance()
                .getReference(Common.BANK_SOAL_REF)
                .child(Common.bankSoalModelSelected.getKodeUndangan())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    kategoriSoalViewModel.loadKategoriSoal();
                    EventBus.getDefault().postSticky(new ToastEvent(Common.ACTION.UPDATE,true));
                });
    }

    private void addCategory(BankSoalModel bankSoalModel) {
        FirebaseDatabase.getInstance()
                .getReference(Common.BANK_SOAL_REF)
                .child(bankSoalModel.getKodeUndangan())
                .setValue(bankSoalModel)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    kategoriSoalViewModel.loadKategoriSoal();
                    EventBus.getDefault().postSticky(new ToastEvent(Common.ACTION.CREATE,true));
                });
    }

    public String generatedKodeUndangan() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
        //System.out.println(generatedString);
    }
}