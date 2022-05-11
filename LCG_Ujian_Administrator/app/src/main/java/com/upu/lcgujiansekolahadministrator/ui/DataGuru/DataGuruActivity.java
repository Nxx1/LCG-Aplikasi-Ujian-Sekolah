package com.upu.lcgujiansekolahadministrator.ui.DataGuru;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upu.lcgujiansekolahadministrator.R;
import com.upu.lcgujiansekolahadministrator.adapter.MyDaftarGuruAdapter;
import com.upu.lcgujiansekolahadministrator.common.Common;
import com.upu.lcgujiansekolahadministrator.model.GuruModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataGuruActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_nama, txt_username,txt_password;
    ImageView imgBack;

    private ListView listView;

    private Uri imageUri = null;

    //tambahkan kode ini
    private MyDaftarGuruAdapter adapter;
    private ArrayList<GuruModel> guruModelArrayList;
    DatabaseReference dbGuru;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_guru);
        dbGuru = FirebaseDatabase.getInstance().getReference(Common.GURU_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listView = findViewById(R.id.lv_list);
        imgBack = findViewById(R.id.informasi_img_back);

        progressDialog = new ProgressDialog(DataGuruActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Simpan Data");
        progressDialog.setMessage("Mohon tunggu...");

        guruModelArrayList = new ArrayList<>();

        fab = findViewById(R.id.fab_tambah_data_guru);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm();
            }
        });

        //Dialog Form Update
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Common.guruModelSelected = guruModelArrayList.get(i);
                UpdateForm();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Data Guru");
        progressDialog.show();

        dbGuru.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guruModelArrayList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    GuruModel guruModel = dataSnapshot1.getValue(GuruModel.class);
                    guruModelArrayList.add(guruModel);
                }

                progressDialog.dismiss();

                MyDaftarGuruAdapter adapter = new MyDaftarGuruAdapter(DataGuruActivity.this);
                adapter.setGuruModelArrayList(guruModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataGuruActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DialogForm() {
        dialog = new AlertDialog.Builder(DataGuruActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_data_guru, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Tambah Data Akun Guru");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama_guru);
        txt_username = (EditText) dialogView.findViewById(R.id.txt_username_guru);
        txt_password = (EditText) dialogView.findViewById(R.id.txt_password_guru);



        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();

                GuruModel guruModel = new GuruModel();
                guruModel.setNamalengkap(txt_nama.getText().toString());
                guruModel.setKey(txt_username.getText().toString());
                guruModel.setPassword(txt_password.getText().toString());
                guruModel.setUsername(txt_username.getText().toString());

                addDataFirebase(guruModel);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog formDialog = dialog.create();
        txt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    DatabaseReference dbCekAkun = FirebaseDatabase.getInstance().getReference(Common.GURU_REF).child(txt_username.getText().toString());
                    dbCekAkun.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(DataGuruActivity.this, "Username sudah terdaftar!\nSilahkan gunakan username lain.", Toast.LENGTH_SHORT).show();
                                formDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }else{
                                formDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(DataGuruActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        formDialog.show();
    }

    private void UpdateForm() {
        dialog = new AlertDialog.Builder(DataGuruActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_data_guru, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Ubah Akun Guru");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama_guru);
        txt_username = (EditText) dialogView.findViewById(R.id.txt_username_guru);
        txt_password = (EditText) dialogView.findViewById(R.id.txt_password_guru);

        txt_nama.setText(Common.guruModelSelected.getNamalengkap());
        txt_username.setText(Common.guruModelSelected.getUsername());
        txt_password.setText(Common.guruModelSelected.getPassword());

        txt_username.setEnabled(false);

        dialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("namalengkap",txt_nama.getText().toString());
                updateData.put("password",txt_password.getText().toString());
                updateData.put("key",Common.guruModelSelected.getKey());
                updateData.put("username",Common.guruModelSelected.getKey());


                updateDataFirebase(updateData);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDataFirebase();
                dialog.dismiss();
            }
        });

        dialog.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateDataFirebase(Map<String, Object> updateData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        getReference = database.getReference();

        if (isEmpty(txt_nama.getText()) && isEmpty(txt_password.getText())) {
            Toast.makeText(DataGuruActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        } else {
            getReference.child(Common.GURU_REF).child(Common.guruModelSelected.getKey())
                    .setValue(updateData)
                    .addOnSuccessListener(this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            txt_nama.setText("");
                            txt_password.setText("");
                            txt_username.setText("");
                            txt_username.setVisibility(View.VISIBLE);
                            Toast.makeText(DataGuruActivity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DataGuruActivity.this, "Update gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteDataFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        getReference = database.getReference();

        getReference.child(Common.GURU_REF).child(Common.guruModelSelected.getKey())
                .removeValue();

        Toast.makeText(DataGuruActivity.this, "Data telah dihapus...",
                Toast.LENGTH_SHORT).show();

        imageUri = null;
    }

    private void addDataFirebase(GuruModel guruModel) {
        //Mendapatkan Instance dari Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        getReference = database.getReference();

        if (isEmpty(txt_nama.getText()) && isEmpty(txt_username.getText()) && isEmpty(txt_username.getText())) {
            //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
            Toast.makeText(DataGuruActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        } else {

            getReference.child(Common.GURU_REF).child(guruModel.getUsername())
                    .setValue(guruModel)
                    .addOnSuccessListener(this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            txt_nama.setText("");
                            txt_username.setText("");
                            txt_password.setText("");
                            Toast.makeText(DataGuruActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DataGuruActivity.this, "Proses gagal!", Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.dismiss();
        }

        imageUri = null;
    }

    @Override
    public void onClick(View v) {

    }
}