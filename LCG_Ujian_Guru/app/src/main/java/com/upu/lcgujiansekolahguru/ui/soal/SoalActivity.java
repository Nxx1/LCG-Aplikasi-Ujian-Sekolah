package com.upu.lcgujiansekolahguru.ui.soal;

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
import com.upu.lcgujiansekolahguru.adapter.MySoalAdapter;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.SoalModel;

import java.util.ArrayList;

public class SoalActivity extends AppCompatActivity {


    private ListView listView;

    //tambahkan kode ini
    private MySoalAdapter adapter;
    private ArrayList<SoalModel> soalModelArrayList;
    DatabaseReference dbSoal;


    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);

        progressDialog = new ProgressDialog(SoalActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(Common.bankSoalModelSelected.getNamaUjian());
        progressDialog.setMessage("Mohon tunggu...");

        dbSoal = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF)
        .child(Common.bankSoalModelSelected.getKodeUndangan())
        .child(Common.SOAL_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listView = findViewById(R.id.lv_list);

        soalModelArrayList = new ArrayList<>();

        TextView textViewNamaUjian = findViewById(R.id.textView2);
        
        textViewNamaUjian.setText("Soal "+ Common.bankSoalModelSelected.getNamaUjian()+" (kelas : "+Common.bankSoalModelSelected.getKelas()+")");

        ImageView imageViewBack = findViewById(R.id.informasi_img_back);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton fabTambah = findViewById(R.id.fab_tambah_data_soal);

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SoalActivity.this, TambahSoalActivity.class));
            }
        });

        //Dialog Form Update
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Common.soalModelSelected = soalModelArrayList.get(i);
                //UpdateForm();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final int[] nomorSoal = {0};

        progressDialog.setTitle("Data Guru");
        progressDialog.show();

        dbSoal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                soalModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    nomorSoal[0] +=1;
                    SoalModel soalModel = dataSnapshot1.getValue(SoalModel.class);
                    soalModel.setNomorSoal(String.valueOf(nomorSoal[0]));
                    soalModelArrayList.add(soalModel);
                }

                progressDialog.dismiss();

                MySoalAdapter adapter = new MySoalAdapter(SoalActivity.this);
                adapter.setSoalModelArrayList(soalModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SoalActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}