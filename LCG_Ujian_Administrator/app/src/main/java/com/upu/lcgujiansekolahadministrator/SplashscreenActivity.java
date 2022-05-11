package com.upu.lcgujiansekolahadministrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahadministrator.common.Common;
import com.upu.lcgujiansekolahadministrator.model.UserModel;

public class SplashscreenActivity extends AppCompatActivity {
    private int waktu_loading=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splashscreen);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        if(!name.equalsIgnoreCase("") || !password.equalsIgnoreCase(""))
        {
            cekakun(name,password);
        }else{
            startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void cekakun(String username,String password){
        if(isNetworkAvailable(SplashscreenActivity.this)){
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(Common.SUPERADMIN_REF).child(username);
            //rootRef.child(username);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        // The child doesn't exist
                        startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
                        finish();
                    }else{
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if(userModel.getPassword() != null){
                            if(userModel.getPassword().equals(password)){
                                Common.userModel = userModel;
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashscreenActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username",userModel.getUsername());
                                editor.putString("password",userModel.getPassword());
                                editor.apply();
                                Intent intent = new Intent(SplashscreenActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }else{
                                startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
                            }
                        }else{
                            startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
                        }
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SplashscreenActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashscreenActivity.this,LoginActivity.class));
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Tidak ada koneksi internet, silahkan periksa jaringan anda", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}