package com.potensiutama.lcgsiswa.Ui.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.R;

import java.util.Locale;

public class SingleMateriActivity extends AppCompatActivity {

    TextToSpeech TTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_materi);

        TextView tNama = findViewById(R.id.txt_nama_materi);
        TextView tPenjelasan = findViewById(R.id.txt_penjelasan_materi);

        tNama.setText(Common.materiPembelajaranModel.getName());
        tPenjelasan.setText(Common.materiPembelajaranModel.getPenjelasan());

        ImageView imgMateri = findViewById(R.id.img_materi);

        Glide.with(getApplicationContext()).load(Common.materiPembelajaranModel.getImage())
                .into(imgMateri);

        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(new Locale("id","ID"));

                } else {
                    Toast.makeText(SingleMateriActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_materi_tts);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTS.speak(Common.materiPembelajaranModel.getName() +"\n"+ Common.materiPembelajaranModel.getPenjelasan(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onPause() {
        if (TTS != null && TTS.isSpeaking()) {
            TTS.stop();
        }
        super.onPause();
    }
}