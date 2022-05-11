package com.upu.lcgujiansekolahkepsek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.upu.lcgujiansekolahkepsek.ui.Report.LaporanDataNilaiSiswaActivity;
import com.upu.lcgujiansekolahkepsek.ui.Report.LaporanDataUjianActivity;

public class MenuUtamaLaporanHasilUjianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama_laporan_hasil_ujian);

        ImageView bReportDataUjian = findViewById(R.id.imageView2);
        ImageView bReportNilaiSiswa = findViewById(R.id.imageView4);

        bReportDataUjian.setOnClickListener(v -> startActivity(new Intent(MenuUtamaLaporanHasilUjianActivity.this, LaporanDataUjianActivity.class)));

        bReportNilaiSiswa.setOnClickListener(v -> startActivity(new Intent(MenuUtamaLaporanHasilUjianActivity.this, LaporanDataNilaiSiswaActivity.class)));
    }
}