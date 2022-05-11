package com.potensiutama.lcgsiswa.Ui.highscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potensiutama.lcgsiswa.Adapter.MyHighScoreAdapter;
import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.R;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private MyHighScoreAdapter adapter;
    private ArrayList<SkorModel> skorModelArrayList;
    DatabaseReference dbSkor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        dbSkor = FirebaseDatabase.getInstance().getReference(Common.SKOR_REF);

        listView = findViewById(R.id.lv_list);
        skorModelArrayList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int[] ranking = {0};
        dbSkor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                skorModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SkorModel skorModel = dataSnapshot1.getValue(SkorModel.class);
                    ranking[0] +=1;
                    //skorModel.setRanking(ranking[0]);
                    skorModelArrayList.add(skorModel);
                }

                adapter = new MyHighScoreAdapter(HighScoreActivity.this);
                adapter.setSkorModelArrayList(skorModelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HighScoreActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}