package com.potensiutama.lcgsiswa.Ui.soal;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.potensiutama.lcgsiswa.Callback.ISoalCallbackListener;
import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.MainActivity;
import com.potensiutama.lcgsiswa.Models.BankSoalModel;
import com.potensiutama.lcgsiswa.Models.JawabanSiswaModel;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.Models.SoalModel;
import com.potensiutama.lcgsiswa.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoalActivity extends AppCompatActivity {

    private ImageView gambarSoal;

    private TextView textViewShowTime; // will show the time
    private CountDownTimer countDownTimer; // built in android class
    // CountDownTimer
    private long totalTimeCountInMilliseconds; // total count down time in

    TextView tSoal,tJumlahSoal,tSelanjutnya;
    RadioGroup rgPilihan;
    RadioButton rbA,rbB,rbC,rbD;
    CardView bNext;
    String jawaban;
    private ISoalCallbackListener listener;

    List<SoalModel> tempList;
    List<JawabanSiswaModel> jawabanSiswaModelList;
    int indexSoal[] = new int[10];
    int jumlah = 1;
    int SoalKe = 1;
    int Skor;

    int index,indexJawaban;
    ProgressDialog progressDialog;

    AlertDialog.Builder builder;

    ConstraintLayout clSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);

        progressDialog = ProgressDialog.show(SoalActivity.this, "",
                "Loading. Please wait...", true);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        jawabanSiswaModelList = new ArrayList<>();

        gambarSoal = findViewById(R.id.img_gambar_soal);


        textViewShowTime = (TextView) findViewById(R.id.textViewTimer);

        totalTimeCountInMilliseconds = Common.bankSoalModel.getDurasi() *60000;

        startTimer();

        tSoal = findViewById(R.id.txt_soal);
        tSelanjutnya = findViewById(R.id.txt_soal_selanjutnya);
        clSoal = findViewById(R.id.cl_soal);
        clSoal.setVisibility(View.GONE);

        rgPilihan = findViewById(R.id.rg_pilihan);

        bNext = findViewById(R.id.cv_soal_next);

        rbA = findViewById(R.id.rb_pilihan_jawaban_a);
        rbB = findViewById(R.id.rb_pilihan_jawaban_b);
        rbC = findViewById(R.id.rb_pilihan_jawaban_c);
        rbD = findViewById(R.id.rb_pilihan_jawaban_d);

        tempList = new ArrayList<>();
        index = 0;
        indexJawaban =0;
        LoadSoal();
        Skor = 0;
        tJumlahSoal = findViewById(R.id.textView2);

        bNext.setOnClickListener(v -> {
            TampilSoal();
        });
    }

    private void GenerateLCM(int nilaiMod){
        //nilaiMod += 1;
        int min = 0;
        int max = nilaiMod;
        int random = new Random().nextInt((max - min) + 1) + min;
        int seed = random;
        // Modulus parameter
        int mod = nilaiMod;

        int multiplier = 11;

        // Increment term
        //int inc = random-1;
        int inc = 7;
        // Number of Random numbers
        // to be generated
        int noOfRandomNum = nilaiMod;
        // To store random numbers
        int[] randomNums = new int[noOfRandomNum];
        // Function Call
        lcm(seed, mod, multiplier, inc, randomNums, noOfRandomNum);

        // Print the generated random numbers
        for (int i = 0; i < 10; i++) {
            indexSoal[i] = randomNums[i];
        }
    }

    private void lcm(int seed, int mod, int multiplier, int inc, int[] randomNums, int noOfRandomNum)
    {

        Log.d("Number","Seed : "+ seed + " Mod : "+mod+" Multiplier :"+multiplier+ " Increment : "+inc);
        // Initialize the seed state
        randomNums[0] = seed;

        // Traverse to generate required
        // numbers of random numbers
        for (int i = 1; i < noOfRandomNum; i++) {
            // Follow the linear congruential method
            randomNums[i] = ((randomNums[i - 1] * multiplier) + inc) % mod;

            Log.d("Number","Random number : "+ randomNums[i]);
        }
    }

    private void LoadSoal(){

            Query orderRef = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF)
                    .child(Common.kodeUjian)
                    .child(Common.SOAL_REF)
                    .orderByChild("teksSoal");
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                        if(dataSnapshot.getChildrenCount() <= 0){
                            progressDialog.dismiss();
                            Toast.makeText(SoalActivity.this, "Soal tidak tersedia, silahkan hubungi guru", Toast.LENGTH_SHORT).show();
                            finish();
                        }else if(dataSnapshot.getChildrenCount() < 15){
                            progressDialog.dismiss();
                            Toast.makeText(SoalActivity.this, "Soal tidak memenuhi persyaratan minimum, silahkan hubungi guru", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        SoalModel soalModel = itemSnapshot.getValue(SoalModel.class);
                        tempList.add(soalModel);
                        if(dataSnapshot.getChildrenCount() == tempList.size()){
                            GenerateLCM(tempList.size());

                            if(tempList.get(indexSoal[0]).getGambarSoal() != null){
                                if(tempList.get(indexSoal[0]).getGambarSoal().equals("")){
                                    gambarSoal.setVisibility(View.GONE);
                                }else{
                                    Glide.with(SoalActivity.this).load(tempList.get(indexSoal[0]).getGambarSoal()).into(gambarSoal);
                                }
                            }else{
                                gambarSoal.setVisibility(View.GONE);
                            }

                            tSoal.setText(tempList.get(indexSoal[0]).getTeksSoal());
                            rbA.setText("A. " + tempList.get(indexSoal[0]).getPilihanA());
                            rbB.setText("B. " + tempList.get(indexSoal[0]).getPilihanB());
                            rbC.setText("C. " + tempList.get(indexSoal[0]).getPilihanC());
                            rbD.setText("D. " + tempList.get(indexSoal[0]).getPilihanD());
                            jawaban = tempList.get(indexSoal[0]).getJawabanBenar();
                            clSoal.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SoalActivity.this, "Gagal memuat soal, silahkan coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SoalActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
    }

    private void TampilSoal(){
        if(tempList.get(indexSoal[index]).getGambarSoal() != null){
            if(tempList.get(indexSoal[index]).getGambarSoal().equals("")){
                gambarSoal.setVisibility(View.GONE);
            }else {
                Glide.with(SoalActivity.this).load(tempList.get(indexSoal[index]).getGambarSoal()).into(gambarSoal);
            }
        }else{
            gambarSoal.setVisibility(View.GONE);
        }
        jawaban = tempList.get(indexSoal[index]).getJawabanBenar();
        JawabanSiswaModel jawabanSiswaModel = new JawabanSiswaModel();
        jawabanSiswaModel.setKey(tempList.get(indexSoal[index]).getKey());
        jawabanSiswaModel.setTeksSoal(tempList.get(indexSoal[index]).getTeksSoal());

        switch (tempList.get(indexSoal[index]).getJawabanBenar()){
            case "A":
                jawabanSiswaModel.setJawabanBenar(tempList.get(indexSoal[index]).getJawabanBenar()+"."+tempList.get(indexSoal[index]).getPilihanA());
                break;
            case "B":
                jawabanSiswaModel.setJawabanBenar(tempList.get(indexSoal[index]).getJawabanBenar()+"."+tempList.get(indexSoal[index]).getPilihanB());
                break;
            case "C":
                jawabanSiswaModel.setJawabanBenar(tempList.get(indexSoal[index]).getJawabanBenar()+"."+tempList.get(indexSoal[index]).getPilihanC());
                break;
            case "D":
                jawabanSiswaModel.setJawabanBenar(tempList.get(indexSoal[index]).getJawabanBenar()+"."+tempList.get(indexSoal[index]).getPilihanD());
                break;

        }

        if (rbA.isChecked()) {
            if(jawaban.equals("A")){
                jawabanSiswaModel.setCorrect(true);
                Skor += 10;
            }
            jawabanSiswaModel.setJawabanUser("A."+tempList.get(indexSoal[index]).getPilihanA());
        } else if (rbB.isChecked()) {
            if(jawaban.equals("B")){
                jawabanSiswaModel.setCorrect(true);
                Skor += 10;
            }
            jawabanSiswaModel.setJawabanUser("B."+tempList.get(indexSoal[index]).getPilihanB());
        } else if (rbC.isChecked()) {
            if(jawaban.equals("C")){
                jawabanSiswaModel.setCorrect(true);
                Skor += 10;
            }
            jawabanSiswaModel.setJawabanUser("C."+tempList.get(indexSoal[index]).getPilihanC());
        } else if (rbD.isChecked()) {
            if(jawaban.equals("D")){
                jawabanSiswaModel.setCorrect(true);
                Skor += 10;
            }
            jawabanSiswaModel.setJawabanUser("D."+tempList.get(indexSoal[index]).getPilihanD());
        }

        jawabanSiswaModelList.add(jawabanSiswaModel);

        rgPilihan.clearCheck();
        if (tSelanjutnya.getText() == "SELESAI") {
            builder.setTitle("Ujian Selesai");
            builder.setMessage("Nilai anda : "+Skor );
            builder.setPositiveButton("OK", (dialog, which) -> {
               // DialogForm(Skor);
                SkorModel skorModel = new SkorModel();
                skorModel.setNama(Common.siswaModel.getNamalengkap());
                skorModel.setNis(Common.siswaModel.getKey());
                skorModel.setSkor(Skor);
                skorModel.setKodeUjian(Common.bankSoalModel.getKodeUndangan());
                skorModel.setNamaUjian(Common.bankSoalModel.getNamaUjian());
                skorModel.setJawabanSiswaModelList(jawabanSiswaModelList);
                FirebaseDatabase.getInstance()
                        .getReference(Common.SISWA_REF)
                        .child(Common.siswaModel.getKey())
                        .child(Common.SKOR_REF)
                        .push()
                        .setValue(skorModel)
                        .addOnFailureListener(e -> Toast.makeText(SoalActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnCompleteListener(task -> {
                            FirebaseDatabase.getInstance()
                                    .getReference(Common.BANK_SOAL_REF)
                                    .child(Common.bankSoalModel.getKodeUndangan())
                                    .child(Common.SKOR_REF)
                                    .child(Common.siswaModel.getKey())
                                    .setValue(skorModel)
                                    .addOnFailureListener(e2 -> Toast.makeText(SoalActivity.this, ""+e2.getMessage(), Toast.LENGTH_SHORT).show())
                                    .addOnCompleteListener(task2 -> {
                                        Toast.makeText(SoalActivity.this, "Ujian Selesai!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                        });
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            index += 1;
            SoalKe += 1;
            tSoal.setText(tempList.get(indexSoal[index]).getTeksSoal());
            rbA.setText("A. " + tempList.get(indexSoal[index]).getPilihanA());
            rbB.setText("B. " + tempList.get(indexSoal[index]).getPilihanB());
            rbC.setText("C. " + tempList.get(indexSoal[index]).getPilihanC());
            rbD.setText("D. " + tempList.get(indexSoal[index]).getPilihanD());
            tJumlahSoal.setText("Soal " + SoalKe + "/10");

            if (SoalKe == 10) {
                tSelanjutnya.setText("SELESAI");
            }
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long durasi = leftTimeInMilliseconds / 60000;

                textViewShowTime.setText("Sisa Waktu : "+String.format("%02d Jam %02d Menit", durasi / 60, durasi % 60));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                textViewShowTime.setText("Waktu Habis!");
                Toast.makeText(SoalActivity.this, "Ujian Berakhir!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}