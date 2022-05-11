package com.upu.lcgujiansekolahadministrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.upu.lcgujiansekolahadministrator.R;
import com.upu.lcgujiansekolahadministrator.model.GuruModel;

import java.util.ArrayList;

public class MyDaftarGuruAdapter extends BaseAdapter {
    Context context;
    private ArrayList<GuruModel> guruModelArrayList = new ArrayList<>();

    public void setGuruModelArrayList(ArrayList<GuruModel> guruModelArrayList) {
        this.guruModelArrayList = guruModelArrayList;
    }

    public MyDaftarGuruAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return guruModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return guruModelArrayList.get(i);
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

        GuruModel guruModel = (GuruModel) getItem(i);
        viewHolder.bind(guruModel);
        return itemView;
    }

    private class ViewHolder {
        private TextView txtNama, txtPenjelasan;
        private ImageView imgInformasi;

        ViewHolder(View view) {
            txtNama = view.findViewById(R.id.guru_card_nama);
        }

        void bind(GuruModel guruModel) {
            txtNama.setText(guruModel.getNamalengkap());
        }
    }
}
