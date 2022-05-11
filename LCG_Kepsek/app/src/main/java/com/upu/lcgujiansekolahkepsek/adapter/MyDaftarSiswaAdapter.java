package com.upu.lcgujiansekolahkepsek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.model.SiswaModel;

import java.util.ArrayList;

public class MyDaftarSiswaAdapter extends BaseAdapter {
    Context context;
    private ArrayList<SiswaModel> siswaModelArrayList = new ArrayList<>();

    public void setSiswaModelArrayList(ArrayList<SiswaModel> siswaModelArrayList) {
        this.siswaModelArrayList = siswaModelArrayList;
    }

    public MyDaftarSiswaAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return siswaModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return siswaModelArrayList.get(i);
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
                    .inflate(R.layout.carditem_guru, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        SiswaModel siswaModel = (SiswaModel) getItem(i);
        viewHolder.bind(siswaModel);
        return itemView;
    }

    private class ViewHolder {
        private TextView txtNama;

        ViewHolder(View view) {
            txtNama = view.findViewById(R.id.guru_card_nama);
        }

        void bind(SiswaModel siswaModel) {
            txtNama.setText(siswaModel.getNamalengkap());
        }
    }
}