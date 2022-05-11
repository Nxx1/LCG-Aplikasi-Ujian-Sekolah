package com.upu.lcgujiansekolahguru.ui.nilai_siswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.adapter.MyKategoriSoalAdapter;
import com.upu.lcgujiansekolahguru.adapter.MyNilaiAdapter;
import com.upu.lcgujiansekolahguru.adapter.MySoalAdapter;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.BankSoalModel;
import com.upu.lcgujiansekolahguru.model.SkorModel;
import com.upu.lcgujiansekolahguru.model.SoalModel;
import com.upu.lcgujiansekolahguru.ui.soal.SoalActivity;
import com.upu.lcgujiansekolahguru.ui.soal.TambahSoalActivity;

import java.util.ArrayList;

public class NilaiSiswaActivity extends AppCompatActivity {

    private ListView listView;

    //tambahkan kode ini
    private MyNilaiAdapter adapter;
    private ArrayList<SkorModel> skorModelArrayList;
    DatabaseReference dbNilai;


    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    TextView tTidakAdaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_siswa);

        progressDialog = new ProgressDialog(NilaiSiswaActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Data nilai ujian");
        progressDialog.setMessage("Mohon tunggu...");

        dbNilai = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF)
        .child(Common.bankSoalModelSelected.getKodeUndangan())
        .child(Common.NILAI_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listView = findViewById(R.id.lv_list);

        skorModelArrayList = new ArrayList<>();


        ImageView imageViewBack = findViewById(R.id.informasi_img_back);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tTidakAdaData = findViewById(R.id.txt_tidak_ada_data);


    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();

        dbNilai.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                skorModelArrayList.clear();

                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        SkorModel skorModel = dataSnapshot1.getValue(SkorModel.class);
                        skorModelArrayList.add(skorModel);
                    }
                    tTidakAdaData.setVisibility(View.GONE);
                }

                progressDialog.dismiss();

                MyNilaiAdapter adapter = new MyNilaiAdapter(NilaiSiswaActivity.this);
                adapter.setSkorModelArrayList(skorModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NilaiSiswaActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}