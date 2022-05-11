package com.upu.lcgujiansekolahguru.ui.nilai_siswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.adapter.MyDetailNilaiAdapter;
import com.upu.lcgujiansekolahguru.adapter.MyNilaiAdapter;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.JawabanSiswaModel;

import java.util.ArrayList;

public class DetailJawabanActivity extends AppCompatActivity {
    private ListView listView;

    //tambahkan kode ini
    private MyNilaiAdapter adapter;
    private ArrayList<JawabanSiswaModel> jawabanSiswaModelArrayList;
    DatabaseReference dbSkor;


    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jawaban);

        listView = findViewById(R.id.lv_list);

        jawabanSiswaModelArrayList = new ArrayList<>();

        TextView namaUjian = findViewById(R.id.txt_nama_ujian);
        TextView namaSiswa = findViewById(R.id.txt_nama_siswa);
        TextView nilaiUjian = findViewById(R.id.txt_nilai_ujian);

        namaSiswa.setText(Common.skorModel.getNama());
        namaUjian.setText(Common.skorModel.getNamaUjian());
        nilaiUjian.setText("Nilai : "+Common.skorModel.getSkor().toString());

        int nomorsoal = 0;

        for(int i = 0; i< Common.skorModel.getJawabanSiswaModelList().size(); i++){

            nomorsoal +=1;

            JawabanSiswaModel jawabanSiswaModel = new JawabanSiswaModel();
            jawabanSiswaModel = Common.skorModel.getJawabanSiswaModelList().get(i);
            jawabanSiswaModel.setNomorSoal(nomorsoal);
            jawabanSiswaModelArrayList.add(jawabanSiswaModel);
        }


        MyDetailNilaiAdapter adapter = new MyDetailNilaiAdapter(DetailJawabanActivity.this);
        adapter.setSkorModelArrayList(jawabanSiswaModelArrayList);
        listView.setAdapter(adapter);


    }
}