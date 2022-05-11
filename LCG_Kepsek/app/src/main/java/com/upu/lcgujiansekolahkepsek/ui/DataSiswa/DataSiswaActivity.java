package com.upu.lcgujiansekolahkepsek.ui.DataSiswa;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.adapter.MyDaftarSiswaAdapter;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.SiswaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSiswaActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_nama, txt_username,txt_password;
    ImageView imgBack;

    private ListView listView;

    private ArrayList<SiswaModel> siswaModelArrayList;
    DatabaseReference dbSiswa;

    private ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_siswa);
        dbSiswa = FirebaseDatabase.getInstance().getReference(Common.SISWA_REF);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listView = findViewById(R.id.lv_list);
        imgBack = findViewById(R.id.informasi_img_back);

        progressDialog = new ProgressDialog(DataSiswaActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Simpan Data");
        progressDialog.setMessage("Mohon tunggu...");

        siswaModelArrayList = new ArrayList<>();

        fab = findViewById(R.id.fab_tambah_data_siswa);
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
                Common.siswaModelSelected = siswaModelArrayList.get(i);
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

        progressDialog.setTitle("Data Siswa");
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

                MyDaftarSiswaAdapter adapter = new MyDaftarSiswaAdapter(DataSiswaActivity.this);
                adapter.setSiswaModelArrayList(siswaModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataSiswaActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DialogForm() {
        dialog = new AlertDialog.Builder(DataSiswaActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_data_siswa, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Tambah Data Akun Siswa");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama_siswa);
        txt_username = (EditText) dialogView.findViewById(R.id.txt_username_siswa);
        txt_password = (EditText) dialogView.findViewById(R.id.txt_password_siswa);

        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();

                SiswaModel siswaModel = new SiswaModel();
                siswaModel.setNamalengkap(txt_nama.getText().toString());
                siswaModel.setKey(txt_username.getText().toString());
                siswaModel.setPassword(txt_password.getText().toString());
                siswaModel.setUsername(txt_username.getText().toString());

                addDataFirebase(siswaModel);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void UpdateForm() {
        dialog = new AlertDialog.Builder(DataSiswaActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_data_siswa, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Ubah Akun Siswa");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama_siswa);
        txt_username = (EditText) dialogView.findViewById(R.id.txt_username_siswa);
        txt_password = (EditText) dialogView.findViewById(R.id.txt_password_siswa);

        txt_nama.setText(Common.siswaModelSelected.getNamalengkap());
        txt_username.setText(Common.siswaModelSelected.getUsername());
        txt_password.setText(Common.siswaModelSelected.getPassword());

        txt_username.setVisibility(View.GONE);

        dialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("namalengkap",txt_nama.getText().toString());
                updateData.put("password",txt_password.getText().toString());
                updateData.put("key",Common.siswaModelSelected.getKey());
                updateData.put("username",Common.siswaModelSelected.getKey());


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
            Toast.makeText(DataSiswaActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        } else {
            getReference.child(Common.GURU_REF).child(Common.siswaModelSelected.getKey())
                    .setValue(updateData)
                    .addOnSuccessListener(this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            txt_nama.setText("");
                            txt_password.setText("");
                            txt_username.setText("");
                            txt_username.setVisibility(View.VISIBLE);
                            Toast.makeText(DataSiswaActivity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DataSiswaActivity.this, "Update gagal!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteDataFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        getReference = database.getReference();

        getReference.child(Common.SISWA_REF).child(Common.siswaModelSelected.getKey())
                .removeValue();

        Toast.makeText(DataSiswaActivity.this, "Data telah dihapus...",
                Toast.LENGTH_SHORT).show();

    }

    private void addDataFirebase(SiswaModel siswaModel) {
        //Mendapatkan Instance dari Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        getReference = database.getReference();

        if (isEmpty(txt_nama.getText()) && isEmpty(txt_username.getText()) && isEmpty(txt_username.getText())) {
            //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
            Toast.makeText(DataSiswaActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        } else {

            getReference.child(Common.SISWA_REF).child(siswaModel.getUsername())
                    .setValue(siswaModel)
                    .addOnSuccessListener(this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            txt_nama.setText("");
                            txt_username.setText("");
                            txt_password.setText("");
                            Toast.makeText(DataSiswaActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DataSiswaActivity.this, "Proses gagal!", Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

    }
}