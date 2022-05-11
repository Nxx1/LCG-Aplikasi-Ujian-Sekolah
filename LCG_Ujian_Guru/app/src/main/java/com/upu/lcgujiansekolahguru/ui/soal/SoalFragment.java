package com.upu.lcgujiansekolahguru.ui.soal;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahguru.EventBus.ChangeMenuClick;
import com.upu.lcgujiansekolahguru.EventBus.LoadSoalEvent;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.adapter.MySoalAdapter;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.common.MySwipeHelper;
import com.upu.lcgujiansekolahguru.model.SoalModel;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class SoalFragment extends Fragment {

/*
    @BindView(R.id.recycler_order)
    RecyclerView recycler_order;

    Unbinder unbinder;

    List<SoalModel> soalModelList;

    LayoutAnimationController layoutAnimationController;
    MySoalAdapter adapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SoalViewModel soalViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        soalViewModel =
                ViewModelProviders.of(this).get(SoalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_soal, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        soalViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });
        soalViewModel.getSoalModelMutableLiveData().observe(getViewLifecycleOwner(), bahasaDaerahModels -> {
            if(bahasaDaerahModels != null){
                soalModelList = bahasaDaerahModels;
                adapter = new MySoalAdapter(getContext(), soalModelList);
                recycler_order.setAdapter(adapter);
                recycler_order.setLayoutAnimation(layoutAnimationController);
            }
        });
        return root;
    }
    private void initViews() {


        setHasOptionsMenu(true);

        recycler_order.setHasFixedSize(true);
        recycler_order.setLayoutManager(new LinearLayoutManager(getContext()));

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

      MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(),recycler_order,width/3) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {
                buf.add(new MyButton(getContext(),"Hapus",30,0, Color.parseColor("#12005e"),
                        pos -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("HAPUS")
                                    .setMessage("Apakah anda yakin ingin menghapus soal ini?")
                                    .setNegativeButton("Batal", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    }).setPositiveButton("Hapus",((dialogInterface, i) -> {
                                SoalModel soalModel = adapter.getItemAtPosition(pos);
                                FirebaseDatabase.getInstance()
                                        .getReference(Common.SOAL_REF)
                                        .child(soalModel.getKey())
                                        .removeValue()
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                                        .addOnSuccessListener(aVoid -> {
                                            adapter.removeItem(pos);
                                            adapter.notifyItemRemoved(pos);
                                            dialogInterface.dismiss();
                                            Toast.makeText(getContext(), "Soal berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                        });
                            }));
                            AlertDialog deleteDialog = builder.create();
                            deleteDialog.show();

                            Button negativeButton = deleteDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                            negativeButton.setTextColor(Color.GRAY);
                            Button positiveButton = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            positiveButton.setTextColor(Color.RED);
                        }));

                buf.add(new MyButton(getContext(),"Update",30,0, Color.parseColor("#336699"),
                        pos -> {
                            Common.soalModelSelected = soalModelList.get(pos);
                            showEditDialog();

                        }));
            }
        };
    }

    private void showEditDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Ubah");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_soal,null);
        EditText edt_soal_name = (EditText) itemView.findViewById(R.id.edt_soal_name);
        ImageView img_soal = (ImageView) itemView.findViewById(R.id.img_soal);
        EditText edt_pilihan_a = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_a);
        EditText edt_pilihan_b = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_b);
        EditText edt_pilihan_c = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_c);
        EditText edt_pilihan_d = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_d);
        RadioGroup rg_jawaban_benar = (RadioGroup) itemView.findViewById(R.id.rg_jawaban_benar);
        RadioButton jawabanA = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_a);
        RadioButton jawabanB = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_b);
        RadioButton jawabanC = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_c);
        RadioButton jawabanD = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_d);

        edt_soal_name.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getTeksSoal()));

        edt_pilihan_a.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanA()));

        edt_pilihan_b.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanB()));

        edt_pilihan_c.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanC()));

        edt_pilihan_d.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanD()));

        String jawaban = Common.soalModelSelected.getJawabanBenar();

        switch (jawaban){
            case "A":
                jawabanA.setChecked(true);
                break;
            case "B":
                jawabanB.setChecked(true);
                break;
            case "C":
                jawabanC.setChecked(true);
                break;
            case "D":
                jawabanD.setChecked(true);
                break;
        }

        builder.setNegativeButton("Batal",((dialogInterface, i) -> {dialogInterface.dismiss();}))
                .setPositiveButton("Ubah",((dialogInterface, i) -> {
                    Map<String,Object> updateData = new HashMap<>();
                    updateData.put("teksSoal",edt_soal_name.getText().toString());
                    updateData.put("pilihanA",edt_pilihan_a.getText().toString());
                    updateData.put("pilihanB",edt_pilihan_b.getText().toString());
                    updateData.put("pilihanC",edt_pilihan_c.getText().toString());
                    updateData.put("pilihanD",edt_pilihan_d.getText().toString());

                    if(jawabanA.isChecked()){
                        updateData.put("jawabanBenar","A");
                        updateBahasa(updateData);
                    }else if(jawabanB.isChecked()){
                        updateData.put("jawabanBenar","B");
                        updateBahasa(updateData);
                    }else if(jawabanC.isChecked()){
                        updateData.put("jawabanBenar","C");
                        updateBahasa(updateData);
                    }else if(jawabanD.isChecked()){
                        updateData.put("jawabanBenar","D");
                        updateBahasa(updateData);
                    }else{
                        Toast.makeText(getContext(), "Silahkan pilih jawaban benar terlebih dahulu!", Toast.LENGTH_SHORT).show();
                        showEditDialog();
                    }
                }));

        builder.setView(itemView);
        AlertDialog updateDialog = builder.create();
        updateDialog.show();

    }

    private void updateBahasa(Map<String, Object> updateData) {
        FirebaseDatabase.getInstance()
                .getReference(Common.SOAL_REF)
                .child(Common.soalModelSelected.getKey())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    soalViewModel.loadSoal();
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bank_soal_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search_soal);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                startSearchSoal(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
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
            soalViewModel.loadSoal();

        });
    }

    private void startSearchSoal(String s) {
        List<SoalModel> resultList = new ArrayList<>();
        for(int i=0;i<adapter.getListBahasa().size();i++){
            SoalModel soalModel = adapter.getListBahasa().get(i);
            if(soalModel.getTeksSoal().toLowerCase().contains(s)){
                resultList.add(soalModel);
            }
        }
        soalViewModel.getSoalModelMutableLiveData().setValue(resultList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_add){
            showAddDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Data");
        builder.setMessage("Silahkan isi informasi dibawah ini...");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_soal,null);
        EditText edt_soal_name = (EditText) itemView.findViewById(R.id.edt_soal_name);
        ImageView img_soal = (ImageView) itemView.findViewById(R.id.img_soal);
        EditText edt_pilihan_a = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_a);
        EditText edt_pilihan_b = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_b);
        EditText edt_pilihan_c = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_c);
        EditText edt_pilihan_d = (EditText) itemView.findViewById(R.id.edt_soal_pilihan_jawaban_d);
        RadioGroup rg_jawaban_benar = (RadioGroup) itemView.findViewById(R.id.rg_jawaban_benar);
        RadioButton jawabanA = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_a);
        RadioButton jawabanB = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_b);
        RadioButton jawabanC = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_c);
        RadioButton jawabanD = (RadioButton) itemView.findViewById(R.id.rb_soal_jawaban_d);

        builder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("Tambah", (dialogInterface, i) -> {

            SoalModel soalModel = new SoalModel();
            soalModel.setTeksSoal(edt_soal_name.getText().toString());
            soalModel.setPilihanA(edt_pilihan_a.getText().toString());
            soalModel.setPilihanB(edt_pilihan_b.getText().toString());
            soalModel.setPilihanC(edt_pilihan_c.getText().toString());
            soalModel.setPilihanD(edt_pilihan_d.getText().toString());

            if(jawabanA.isChecked()){
                soalModel.setJawabanBenar("A");
                addBahasa(soalModel);
            }else if(jawabanB.isChecked()){
                soalModel.setJawabanBenar("B");
                addBahasa(soalModel);
            }else if(jawabanC.isChecked()){
                soalModel.setJawabanBenar("C");
                addBahasa(soalModel);
            }else if(jawabanD.isChecked()){
                soalModel.setJawabanBenar("D");
                addBahasa(soalModel);
            }else{
                Toast.makeText(getContext(), "Silahkan pilih jawaban benar terlebih dahulu!", Toast.LENGTH_SHORT).show();
                showAddDialog();
            }

            // find the radiobutton by returned id


        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void addBahasa(SoalModel soalModel) {
        FirebaseDatabase.getInstance()
                .getReference(Common.SOAL_REF)
                .push()
                .setValue(soalModel)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    soalViewModel.loadSoal();
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        if(EventBus.getDefault().hasSubscriberForEvent(LoadSoalEvent.class))
            EventBus.getDefault().removeStickyEvent(LoadSoalEvent.class);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onLoaderEvent(LoadSoalEvent event){
        soalViewModel.loadSoal();
    }

       */
}