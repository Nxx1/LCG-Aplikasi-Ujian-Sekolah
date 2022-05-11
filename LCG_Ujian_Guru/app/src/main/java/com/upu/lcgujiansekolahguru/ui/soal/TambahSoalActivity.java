package com.upu.lcgujiansekolahguru.ui.soal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.SoalModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class TambahSoalActivity extends AppCompatActivity {

    private Uri uriGambarsoal = null;

    private ImageView imgGambarSoal;
    private String teksSoal, pilihanA,pilihanB,pilihanC,pilihanD,gambarSoal,jawabanBenar;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private android.app.AlertDialog dialog;

    private TextView bSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_soal);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Common.linkgambarsoal = "";

        bSimpan = findViewById(R.id.textView3);

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpanDatabase();


            }
        });

        ImageView imgBack = findViewById(R.id.informasi_img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgGambarSoal = findViewById(R.id.img_soal);

        imgGambarSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(TambahSoalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(TambahSoalActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(TambahSoalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        ImagePicker();
                    }
                } else {
                    ImagePicker();
                }
            }
        });

    }

    private void ImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(TambahSoalActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uriGambarsoal = result.getUri();

                imgGambarSoal.setImageURI(uriGambarsoal);

                String randomName = UUID.randomUUID().toString();
                StorageReference filePath = storageReference.child("gambar_soal").child(randomName + ".jpg");

                filePath.putFile(uriGambarsoal)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        filePath.getDownloadUrl().addOnSuccessListener(
                                                new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Common.linkgambarsoal = imageUrl;
                                                    }
                                                }
                                        );
                                    }
                                }
                        ).addOnProgressListener(taskSnapshot -> {
                    double progress = (100* taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void SimpanDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference(Common.BANK_SOAL_REF)
                .child(Common.bankSoalModelSelected.getKodeUndangan())
                .child(Common.BANK_SOAL_REF).push().getKey();

        EditText edt_soal_name = (EditText) findViewById(R.id.edt_soal_name);
        //ImageView img_soal = (ImageView) findViewById(R.id.img_soal);
        EditText edt_pilihan_a = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_a);
        EditText edt_pilihan_b = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_b);
        EditText edt_pilihan_c = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_c);
        EditText edt_pilihan_d = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_d);
        RadioGroup rg_jawaban_benar = (RadioGroup) findViewById(R.id.rg_jawaban_benar);
        RadioButton jawabanA = (RadioButton) findViewById(R.id.rb_soal_jawaban_a);
        RadioButton jawabanB = (RadioButton) findViewById(R.id.rb_soal_jawaban_b);
        RadioButton jawabanC = (RadioButton) findViewById(R.id.rb_soal_jawaban_c);
        RadioButton jawabanD = (RadioButton) findViewById(R.id.rb_soal_jawaban_d);


        SoalModel soalModel = new SoalModel();
        soalModel.setTeksSoal(edt_soal_name.getText().toString());
        soalModel.setPilihanA(edt_pilihan_a.getText().toString());
        soalModel.setPilihanB(edt_pilihan_b.getText().toString());
        soalModel.setPilihanC(edt_pilihan_c.getText().toString());
        soalModel.setPilihanD(edt_pilihan_d.getText().toString());
        soalModel.setGambarSoal(Common.linkgambarsoal);
        soalModel.setKey(key);
        if(jawabanA.isChecked()){
            soalModel.setJawabanBenar("A");
        }else if(jawabanB.isChecked()){
            soalModel.setJawabanBenar("B");
        }else if(jawabanC.isChecked()){
            soalModel.setJawabanBenar("C");
        }else if(jawabanD.isChecked()){
            soalModel.setJawabanBenar("D");
        }else{
            Toast.makeText(TambahSoalActivity.this, "Silahkan pilih jawaban benar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase.getInstance()
                .getReference(Common.BANK_SOAL_REF)
                .child(Common.bankSoalModelSelected.getKodeUndangan())
                .child(Common.SOAL_REF)
                .push()
                .setValue(soalModel)
                .addOnFailureListener(e -> Toast.makeText(TambahSoalActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    Common.linkgambarsoal = "";
                    Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}