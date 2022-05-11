package com.potensiutama.lcgsiswa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.Models.JawabanSiswaModel;
import com.potensiutama.lcgsiswa.R;

import java.util.ArrayList;

public class MyDetailNilaiAdapter extends BaseAdapter {
    Context context;
    private ArrayList<JawabanSiswaModel> jawabanSiswaModelArrayList = new ArrayList<>();

    public void setSkorModelArrayList(ArrayList<JawabanSiswaModel> jawabanSiswaModelArrayList) {
        this.jawabanSiswaModelArrayList = jawabanSiswaModelArrayList;
    }

    public MyDetailNilaiAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return jawabanSiswaModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return jawabanSiswaModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (itemView == null) {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.carditem_highscore, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        JawabanSiswaModel jawabanSiswaModel = (JawabanSiswaModel) getItem(i);
        viewHolder.bind(jawabanSiswaModel);



        return itemView;
    }

    private class ViewHolder {
        private TextView teksNamaUjian, teksKodeUndangan,teksNilai;
        private View viewS;
        ViewHolder(View view) {
            teksNamaUjian = view.findViewById(R.id.txt_nama_ujian);
            teksKodeUndangan = view.findViewById(R.id.txt_kode_ujian);
            teksNilai = view.findViewById(R.id.txt_nilai);
            viewS = view;
        }

        void bind(JawabanSiswaModel jawabanSiswaModel) {
            teksNamaUjian.setText(jawabanSiswaModel.getNomorsoal()+". "+jawabanSiswaModel.getTeksSoal());
            teksNamaUjian.setTextSize(14);
            teksKodeUndangan.setText("Jawaban Benar : "+jawabanSiswaModel.getJawabanBenar()+"\nJawaban Kamu : "+jawabanSiswaModel.getJawabanUser());
            if(jawabanSiswaModel.isCorrect()){
                teksNilai.setText("Benar");
                teksNilai.setTextColor(Color.GREEN);
            }else{
                teksNilai.setText("Salah");
                teksNilai.setTextColor(Color.RED);
            }

        }


    }
}
