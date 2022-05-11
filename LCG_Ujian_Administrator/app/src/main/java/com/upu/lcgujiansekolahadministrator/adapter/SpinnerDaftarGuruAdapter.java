package com.upu.lcgujiansekolahadministrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.upu.lcgujiansekolahadministrator.model.GuruModel;

import java.util.List;

public class SpinnerDaftarGuruAdapter extends ArrayAdapter<GuruModel> {

    private Context context;
    private List<GuruModel> values;

    public SpinnerDaftarGuruAdapter(Context context, int textViewResourceId,
                       List<GuruModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public GuruModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setPadding(16,8,16,8);
        label.setText(values.get(position).getNamalengkap() + " ("+values.get(position).getUsername()+")");

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setPadding(16,8,16,8);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getNamalengkap() + " ("+values.get(position).getUsername()+")");

        return label;
    }
}