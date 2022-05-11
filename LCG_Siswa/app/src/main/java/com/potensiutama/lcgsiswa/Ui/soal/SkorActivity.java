package com.potensiutama.lcgsiswa.Ui.soal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.MainActivity;
import com.potensiutama.lcgsiswa.R;

public class SkorActivity extends AppCompatActivity {

    ImageView imgHome;
    TextView tSkor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor);

        imgHome = findViewById(R.id.imgHome);
        tSkor = findViewById(R.id.tNilaiAkhir);

        tSkor.setText(String.valueOf(Common.skorSoal));

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SkorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}