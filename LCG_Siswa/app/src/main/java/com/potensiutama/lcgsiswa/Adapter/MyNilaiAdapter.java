package com.potensiutama.lcgsiswa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.R;
import com.potensiutama.lcgsiswa.Ui.soal.DetailJawabanActivity;

import java.util.ArrayList;

public class MyNilaiAdapter extends BaseAdapter {
    Context context;
    private ArrayList<SkorModel> skorModelArrayList = new ArrayList<>();

    public void setSkorModelArrayList(ArrayList<SkorModel> skorModelArrayList) {
        this.skorModelArrayList = skorModelArrayList;
    }

    public MyNilaiAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return skorModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return skorModelArrayList.get(i);
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

        SkorModel skorModel = (SkorModel) getItem(i);
        viewHolder.bind(skorModel);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.skorModel = skorModel;
                context.startActivity(new Intent(context, DetailJawabanActivity.class));
            }
        });

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

        void bind(SkorModel skorModel) {
            teksNamaUjian.setText(skorModel.getNamaUjian());
            teksKodeUndangan.setText(skorModel.getKodeUjian());
            teksNilai.setText("Nilai : "+ skorModel.getSkor().toString());
        }


    }
}
