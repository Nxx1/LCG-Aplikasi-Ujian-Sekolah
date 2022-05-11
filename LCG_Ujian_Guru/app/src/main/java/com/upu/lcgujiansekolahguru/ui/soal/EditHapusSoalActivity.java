package com.upu.lcgujiansekolahguru.ui.soal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.SoalModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditHapusSoalActivity extends AppCompatActivity {
    private Uri uriGambarsoal = null;

    private ImageView imgGambarSoal;
    private String teksSoal, pilihanA,pilihanB,pilihanC,pilihanD,gambarSoal,jawabanBenar;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private android.app.AlertDialog dialog;

    private TextView bSimpan,bHapus;

    EditText edt_soal_name,edt_pilihan_a,edt_pilihan_b,edt_pilihan_c,edt_pilihan_d;
    RadioButton jawabanA,jawabanB,jawabanC,jawabanD;
    RadioGroup rg_jawaban_benar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hapus_soal);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Common.linkgambarsoal = "";

        edt_soal_name = (EditText) findViewById(R.id.edt_soal_name);
        edt_pilihan_a = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_a);
        edt_pilihan_b = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_b);
        edt_pilihan_c = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_c);
        edt_pilihan_d = (EditText) findViewById(R.id.edt_soal_pilihan_jawaban_d);
        rg_jawaban_benar = (RadioGroup) findViewById(R.id.rg_jawaban_benar);
        jawabanA = (RadioButton) findViewById(R.id.rb_soal_jawaban_a);
        jawabanB = (RadioButton) findViewById(R.id.rb_soal_jawaban_b);
        jawabanC = (RadioButton) findViewById(R.id.rb_soal_jawaban_c);
        jawabanD = (RadioButton) findViewById(R.id.rb_soal_jawaban_d);
        imgGambarSoal = (ImageView) findViewById(R.id.img_soal);

        if(Common.soalModelSelected.getGambarSoal() != null){
            Glide.with(EditHapusSoalActivity.this).load(Common.soalModelSelected.getGambarSoal()).into(imgGambarSoal);
        }

        imgGambarSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(EditHapusSoalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(EditHapusSoalActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(EditHapusSoalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        ImagePicker();
                    }
                } else {
                    ImagePicker();
                }
            }
        });

        edt_soal_name.setText(Common.soalModelSelected.getTeksSoal());

        edt_pilihan_a.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanA()));

        edt_pilihan_b.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanB()));

        edt_pilihan_c.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanC()));

        edt_pilihan_d.setText(new StringBuilder("")
                .append(Common.soalModelSelected.getPilihanD()));

        String jawaban = Common.soalModelSelected.getJawabanBenar();

        switch (jawaban){
            case "A":
                jawabanA.setChecked(true);
                break;
            case "B":
                jawabanB.setChecked(true);
                break;
            case "C":
                jawabanC.setChecked(true);
                break;
            case "D":
                jawabanD.setChecked(true);
                break;
        }

        bSimpan = findViewById(R.id.button_ubah);

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

        bHapus = findViewById(R.id.button_hapus);

        bHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditHapusSoalActivity.this);
                builder.setTitle("HAPUS")
                        .setMessage("Apakah anda yakin ingin menghapus soal ini?")
                        .setNegativeButton("Batal", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).setPositiveButton("Hapus",((dialogInterface, i) -> {
                    SoalModel soalModel = Common.soalModelSelected;
                    FirebaseDatabase.getInstance()
                            .getReference(Common.BANK_SOAL_REF)
                            .child(Common.bankSoalModelSelected.getKodeUndangan())
                            .child(Common.SOAL_REF)
                            .child(Common.soalModelSelected.getKey())
                            .removeValue()
                            .addOnFailureListener(e -> Toast.makeText(EditHapusSoalActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                            .addOnSuccessListener(aVoid -> {
                                dialogInterface.dismiss();
                                Toast.makeText(EditHapusSoalActivity.this, "Soal berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                }));
                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();

                Button negativeButton = deleteDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.GRAY);
                Button positiveButton = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.RED);
            }
        });

    }

    private void ImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(EditHapusSoalActivity.this);
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

        SoalModel soalModel = new SoalModel();
        soalModel.setTeksSoal(edt_soal_name.getText().toString());
        soalModel.setPilihanA(edt_pilihan_a.getText().toString());
        soalModel.setPilihanB(edt_pilihan_b.getText().toString());
        soalModel.setPilihanC(edt_pilihan_c.getText().toString());
        soalModel.setPilihanD(edt_pilihan_d.getText().toString());
        soalModel.setGambarSoal(Common.linkgambarsoal);
        soalModel.setKey(Common.soalModelSelected.getKey());
        if(jawabanA.isChecked()){
            soalModel.setJawabanBenar("A");
        }else if(jawabanB.isChecked()){
            soalModel.setJawabanBenar("B");
        }else if(jawabanC.isChecked()){
            soalModel.setJawabanBenar("C");
        }else if(jawabanD.isChecked()){
            soalModel.setJawabanBenar("D");
        }else{
            Toast.makeText(EditHapusSoalActivity.this, "Silahkan pilih jawaban benar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("teksSoal",soalModel.getTeksSoal());
        updateData.put("pilihanA",soalModel.getPilihanA());
        updateData.put("pilihanB",soalModel.getPilihanB());
        updateData.put("pilihanC",soalModel.getPilihanC());
        updateData.put("pilihanD",soalModel.getPilihanD());
        updateData.put("gambarSoal",soalModel.getGambarSoal());
        updateData.put("jawabanBenar",soalModel.getJawabanBenar());
        updateData.put("key",soalModel.getKey());

        FirebaseDatabase.getInstance()
                .getReference(Common.BANK_SOAL_REF)
                .child(Common.bankSoalModelSelected.getKodeUndangan())
                .child(Common.SOAL_REF)
                .child(soalModel.getKey())
                .setValue(updateData)
                .addOnFailureListener(e -> Toast.makeText(EditHapusSoalActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}