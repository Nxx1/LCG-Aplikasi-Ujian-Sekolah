package com.upu.lcgujiansekolahguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.SoalModel;
import com.upu.lcgujiansekolahguru.ui.soal.EditHapusSoalActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MySoalAdapter extends BaseAdapter {
    Context context;
    private ArrayList<SoalModel> soalModelArrayList = new ArrayList<>();

    public void setSoalModelArrayList(ArrayList<SoalModel> soalModelArrayList) {
        this.soalModelArrayList = soalModelArrayList;
    }

    public MySoalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return soalModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return soalModelArrayList.get(i);
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
                    .inflate(R.layout.layout_soal_items, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        SoalModel soalModel = (SoalModel) getItem(i);
        viewHolder.bind(soalModel);
        return itemView;
    }

    private class ViewHolder {
        private TextView teksSoal, teksJawabanBenar;
        private ImageView imgGambarSoal;
        private View viewS;
        ViewHolder(View view) {
            teksSoal = view.findViewById(R.id.txt_teks_soal);
            teksJawabanBenar = view.findViewById(R.id.txt_jawaban);
            imgGambarSoal = view.findViewById(R.id.img_gambar_soal);
            viewS = view;
        }

        void bind(SoalModel soalModel) {
            String soal = soalModel.getTeksSoal();
            String jawabanBenar ="Pilihan :"
                    + "\nA. " + soalModel.getPilihanA()
                    + "\nB. " + soalModel.getPilihanB()
                    + "\nC. " + soalModel.getPilihanC()
                    + "\nD. " + soalModel.getPilihanD()
                    + "\nJawaban Benar : "+ soalModel.getJawabanBenar();
            teksSoal.setText(soalModel.getNomorSoal()+". " +soal);
            teksJawabanBenar.setText(jawabanBenar);

            if(soalModel.getGambarSoal() == null || soalModel.getGambarSoal().equals("")){
                imgGambarSoal.setVisibility(View.GONE);
            }else{
                Glide.with(context).load(soalModel.getGambarSoal()).into(imgGambarSoal);
            }

            viewS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.soalModelSelected = soalModel;
                    context.startActivity(new Intent(context, EditHapusSoalActivity.class));
                }
            });
        }
    }
}

