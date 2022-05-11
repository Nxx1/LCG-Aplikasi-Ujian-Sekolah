package com.upu.lcgujiansekolahguru.ui.nilai_siswa;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahguru.EventBus.ToastEvent;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.adapter.MyKategoriSoalAdapter;
import com.upu.lcgujiansekolahguru.adapter.MyKategoriUjianNilaiSiswaAdapter;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.common.MySwipeHelper;
import com.upu.lcgujiansekolahguru.model.BankSoalModel;
import com.upu.lcgujiansekolahguru.ui.soal.KategoriSoalViewModel;

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

public class KategoriUjianNilaiSiswaFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private android.app.AlertDialog dialog;

    private KategoriUjianNilaiSiswaViewModel kategoriUjianNilaiSiswaViewModel;

    private List<BankSoalModel> bankSoalModelList;




    Unbinder unbinder;
    @BindView(R.id.recycler_kategori_soal_list)
    RecyclerView recycler_kategori_list;

    LayoutAnimationController layoutAnimationController;
    MyKategoriUjianNilaiSiswaAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kategoriUjianNilaiSiswaViewModel =
                ViewModelProviders.of(this).get(KategoriUjianNilaiSiswaViewModel.class);
        View root = inflater.inflate(R.layout.kategori_ujian_nilai_siswa_fragment, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        kategoriUjianNilaiSiswaViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), bankSoalModels -> {
            if(bankSoalModels != null){
                bankSoalModelList = bankSoalModels;

                adapter = new MyKategoriUjianNilaiSiswaAdapter(getContext(), bankSoalModelList);
                recycler_kategori_list.setAdapter(adapter);
                recycler_kategori_list.setLayoutAnimation(layoutAnimationController);



            }
        });


        return root;
    }

    private void initViews() {

        setHasOptionsMenu(true);

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recycler_kategori_list.setLayoutManager(layoutManager);
        recycler_kategori_list.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

    }


    @Override
    public void onDestroy() {
        // EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }
}