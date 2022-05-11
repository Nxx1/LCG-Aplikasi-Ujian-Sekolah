package com.upu.lcgujiansekolahkepsek.ui.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.adapter.Report.MyReportDataSiswa;
import com.upu.lcgujiansekolahkepsek.adapter.Report.MyReportDataUjian;
import com.upu.lcgujiansekolahkepsek.common.BottomOffsetDecoration;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.model.SiswaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LaporanDataNilaiSiswaActivity extends AppCompatActivity {

    RecyclerView recycler_data_siswa;
    private MyReportDataSiswa adapter;
    private ArrayList<SiswaModel> siswaModelArrayList;
    DatabaseReference dbSiswa;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_data_nilai_siswa);

        dbSiswa = FirebaseDatabase.getInstance().getReference(Common.SISWA_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recycler_data_siswa = findViewById(R.id.recycler_data_siswa_list);

        progressDialog = new ProgressDialog(LaporanDataNilaiSiswaActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Simpan Data");
        progressDialog.setMessage("Mohon tunggu...");

        siswaModelArrayList = new ArrayList<>();

        // layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LaporanDataNilaiSiswaActivity.this);

        recycler_data_siswa.setLayoutManager(layoutManager);
        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        recycler_data_siswa.addItemDecoration(bottomOffsetDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Laporan Data Siswa");
        progressDialog.show();

        dbSiswa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                siswaModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SiswaModel siswaModel = dataSnapshot1.getValue(SiswaModel.class);

                    siswaModelArrayList.add(siswaModel);
                }

                progressDialog.dismiss();

                Collections.sort(siswaModelArrayList, new Comparator<SiswaModel>() {
                    public int compare(SiswaModel o1, SiswaModel o2) {
                        return o1.getNamalengkap().compareTo(o2.getNamalengkap());
                    }
                });

                adapter= new MyReportDataSiswa(LaporanDataNilaiSiswaActivity.this, siswaModelArrayList);
                recycler_data_siswa.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LaporanDataNilaiSiswaActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}