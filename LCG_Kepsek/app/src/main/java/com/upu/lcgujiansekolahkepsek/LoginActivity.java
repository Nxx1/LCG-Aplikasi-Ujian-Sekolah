package com.upu.lcgujiansekolahkepsek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.UserModel;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);



        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
            if(usernameEditText.getText().equals("") || passwordEditText.getText().equals("")){
                Toast.makeText(this, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }else{
                cekguru(usernameEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
    }

    private void cekguru(String username,String password){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.KEPSEK_REF).child(username);
        //rootRef.child(username);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    // The child doesn't exist
                    loginButton.setEnabled(true);
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                }else{
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if(userModel.getPassword().equals(password)){
                        loginButton.setEnabled(true);
                        loadingProgressBar.setVisibility(View.GONE);
                        Common.userModel = userModel;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",userModel.getUsername());
                        editor.putString("password",userModel.getPassword());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MenuUtamaLaporanHasilUjianActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        loginButton.setEnabled(true);
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}