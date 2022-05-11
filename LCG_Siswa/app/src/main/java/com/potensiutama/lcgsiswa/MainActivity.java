package com.potensiutama.lcgsiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.potensiutama.lcgsiswa.Adapter.MyNilaiAdapter;
import com.potensiutama.lcgsiswa.Bantuan.BantuanActivity;
import com.potensiutama.lcgsiswa.Models.BankSoalModel;
import com.potensiutama.lcgsiswa.Models.SiswaModel;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.Models.SoalModel;
import com.potensiutama.lcgsiswa.Ui.highscore.HighScoreActivity;
import com.potensiutama.lcgsiswa.Ui.materi.MateriPembelajaranActivity;
import com.potensiutama.lcgsiswa.Ui.soal.SoalActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    //tambahkan kode ini
    private MyNilaiAdapter adapter;
    private ArrayList<SkorModel> skorModelArrayList;
    DatabaseReference dbSkor;


    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iLogout = findViewById(R.id.img_logout);

        iLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon tunggu...");

        dbSkor = FirebaseDatabase.getInstance().getReference(Common.SISWA_REF)
                .child(Common.siswaModel.getKey())
                .child(Common.SKOR_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listView = findViewById(R.id.lv_list);

        skorModelArrayList = new ArrayList<>();

        TextView namaSiswa = findViewById(R.id.nama_siswa);

        namaSiswa.setText(Common.siswaModel.getNamalengkap());

        FloatingActionButton fabUjian = findViewById(R.id.fab_mulai_ujian);

        fabUjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFormKodeUjian();
            }
        });

    }

    private void DialogFormKodeUjian() {
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_kode_ujian, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Ujian");

        EditText tKodeUjian = (EditText) dialogView.findViewById(R.id.txt_kode_ujian);

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kodeujian    = tKodeUjian.getText().toString();
                dialog.dismiss();
                cekKodeUjian(kodeujian);
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void DialogBeforeMulaiUjian(BankSoalModel bankSoalModel) {
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialogbeforeujian, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Deskripsi Ujian");

        TextView tKodeUjian = dialogView.findViewById(R.id.txt_nama_ujian);
        TextView tGuru = dialogView.findViewById(R.id.txt_guru_ujian);
        TextView tKelas = dialogView.findViewById(R.id.txt_kelas_ujian);
        TextView tDurasi = dialogView.findViewById(R.id.txt_durasi_ujian);

        tKodeUjian.setText(bankSoalModel.getNamaUjian());
        tGuru.setText(bankSoalModel.getNamaLengkapGuru());
        tKelas.setText(bankSoalModel.getKelas());
        tDurasi.setText(bankSoalModel.getDurasi().toString() + " Menit");

        dialog.setPositiveButton("Mulai", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,SoalActivity.class));
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cekKodeUjian(String kodeUjian){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF).child(kodeUjian);
        //rootRef.child(username);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    // The child doesn't exist
                    Toast.makeText(MainActivity.this, "Kode Ujian Tidak Ditemukan!", Toast.LENGTH_SHORT).show();
                }else{
                    if(snapshot.child(Common.SOAL_REF).getChildrenCount() >= 20){
                        if(snapshot.child(Common.SKOR_REF).child(Common.siswaModel.getKey()).exists()){
                            Toast.makeText(MainActivity.this, "Kamu telah melaksanakan ujian ini!", Toast.LENGTH_SHORT).show();
                        }else{
                            Common.kodeUjian = kodeUjian;
                            BankSoalModel bankSoalModel = snapshot.getValue(BankSoalModel.class);
                            Common.bankSoalModel = bankSoalModel;
                            DialogBeforeMulaiUjian(bankSoalModel);
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Bank soal tidak memenuhi persyaratan, silahkan hubungi guru anda.", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();

        TextView tTidakAdaData = findViewById(R.id.txt_tidak_ada_data);

        dbSkor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                skorModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SkorModel skorModel = dataSnapshot1.getValue(SkorModel.class);
                    skorModelArrayList.add(skorModel);

                    tTidakAdaData.setVisibility(View.GONE);
                }

                progressDialog.dismiss();

                MyNilaiAdapter adapter = new MyNilaiAdapter(MainActivity.this);
                adapter.setSkorModelArrayList(skorModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setMessage("Kamu yakin ingin keluar?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username","");
                        editor.putString("password","");
                        editor.apply();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.cancel())
                .show();
    }
}