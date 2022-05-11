package com.upu.lcgujiansekolahkepsek.ui.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.adapter.Report.MyReportDataUjian;
import com.upu.lcgujiansekolahkepsek.adapter.Report.MyReportDetailDataUjian;
import com.upu.lcgujiansekolahkepsek.common.BottomOffsetDecoration;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.model.SkorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LaporanDetailDataUjianActivity extends AppCompatActivity {

    RecyclerView recycler_detail_data_ujian;
    private MyReportDetailDataUjian adapter;
    private ArrayList<SkorModel> skorModelArrayList;
    DatabaseReference dbNilai;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail_data_ujian);

        TextView tvHeader = findViewById(R.id.textView6);
        TextView tvSubtitle = findViewById(R.id.textView7);

        tvHeader.setText(new StringBuilder("Laporan\nUjian ").append(Common.bankSoalModelSelected.getNamaUjian()));
        tvSubtitle.setText(new StringBuilder("Daftar nilai siswa pada ujian ").append(Common.bankSoalModelSelected.getNamaUjian()));

        dbNilai = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF)
                    .child(Common.bankSoalModelSelected.getKodeUndangan()).child(Common.NILAI_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recycler_detail_data_ujian = findViewById(R.id.recycler_data_detail_ujian_list);

        progressDialog = new ProgressDialog(LaporanDetailDataUjianActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Memuat Data");
        progressDialog.setMessage("Mohon tunggu...");

        skorModelArrayList = new ArrayList<>();

        // layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LaporanDetailDataUjianActivity.this);

        recycler_detail_data_ujian.setLayoutManager(layoutManager);
        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        recycler_detail_data_ujian.addItemDecoration(bottomOffsetDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Laporan Data Ujian");
        progressDialog.show();

        dbNilai.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                skorModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SkorModel skorModel = dataSnapshot1.getValue(SkorModel.class);

                    skorModelArrayList.add(skorModel);
                }

                progressDialog.dismiss();

                Collections.sort(skorModelArrayList, new Comparator<SkorModel>() {
                    public int compare(SkorModel o1, SkorModel o2) {
                        return o1.getNama().compareTo(o2.getNama());
                    }
                });

                adapter= new MyReportDetailDataUjian(LaporanDetailDataUjianActivity.this, skorModelArrayList);
                recycler_detail_data_ujian.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LaporanDetailDataUjianActivity.this, "Terjadi kesalahan." +databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}