package com.upu.lcgujiansekolahkepsek.ui.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.adapter.MyDaftarGuruAdapter;
import com.upu.lcgujiansekolahkepsek.adapter.MyDataUjianAdapter;
import com.upu.lcgujiansekolahkepsek.adapter.Report.MyReportDataUjian;
import com.upu.lcgujiansekolahkepsek.common.BottomOffsetDecoration;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.model.GuruModel;
import com.upu.lcgujiansekolahkepsek.ui.DataGuru.DataGuruActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LaporanDataUjianActivity extends AppCompatActivity {

    RecyclerView recycler_data_ujian;
    private MyReportDataUjian adapter;
    private ArrayList<BankSoalModel> bankSoalModelArrayList;
    DatabaseReference dbBankSoal;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_data_ujian);

        dbBankSoal = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recycler_data_ujian = findViewById(R.id.recycler_data_ujian_list);

        progressDialog = new ProgressDialog(LaporanDataUjianActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Simpan Data");
        progressDialog.setMessage("Mohon tunggu...");

        bankSoalModelArrayList = new ArrayList<>();

       // layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LaporanDataUjianActivity.this);

        recycler_data_ujian.setLayoutManager(layoutManager);
        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        recycler_data_ujian.addItemDecoration(bottomOffsetDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Laporan Data Ujian");
        progressDialog.show();

        dbBankSoal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bankSoalModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    BankSoalModel bankSoalModel = dataSnapshot1.getValue(BankSoalModel.class);

                    bankSoalModelArrayList.add(bankSoalModel);
                }

                progressDialog.dismiss();

                Collections.sort(bankSoalModelArrayList, new Comparator<BankSoalModel>() {
                    public int compare(BankSoalModel o1, BankSoalModel o2) {
                        return o1.getNamaUjian().compareTo(o2.getNamaUjian());
                    }
                });

                adapter= new MyReportDataUjian(LaporanDataUjianActivity.this, bankSoalModelArrayList);
                recycler_data_ujian.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LaporanDataUjianActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}