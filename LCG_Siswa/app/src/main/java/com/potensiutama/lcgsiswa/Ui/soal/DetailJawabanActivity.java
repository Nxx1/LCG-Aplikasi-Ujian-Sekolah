package com.potensiutama.lcgsiswa.Ui.soal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.potensiutama.lcgsiswa.Adapter.MyDetailNilaiAdapter;
import com.potensiutama.lcgsiswa.Adapter.MyNilaiAdapter;
import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.LoginActivity;
import com.potensiutama.lcgsiswa.MainActivity;
import com.potensiutama.lcgsiswa.Models.BankSoalModel;
import com.potensiutama.lcgsiswa.Models.JawabanSiswaModel;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.R;

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
        TextView nilaiUjian = findViewById(R.id.txt_nilai_ujian);

        namaUjian.setText(Common.skorModel.getNamaUjian());
        nilaiUjian.setText("Nilai : "+Common.skorModel.getSkor().toString());

        int nomorsoal = 0;

        for(int i=0;i<Common.skorModel.getJawabanSiswaModelList().size();i++){
            nomorsoal +=1;
            JawabanSiswaModel jawabanSiswaModel = Common.skorModel.getJawabanSiswaModelList().get(i);
            jawabanSiswaModel.setNomorsoal(nomorsoal);
            jawabanSiswaModelArrayList.add(jawabanSiswaModel);
        }


        MyDetailNilaiAdapter adapter = new MyDetailNilaiAdapter(DetailJawabanActivity.this);
        adapter.setSkorModelArrayList(jawabanSiswaModelArrayList);
        listView.setAdapter(adapter);


    }
}