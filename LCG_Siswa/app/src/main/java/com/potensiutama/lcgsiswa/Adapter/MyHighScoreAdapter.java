package com.potensiutama.lcgsiswa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.potensiutama.lcgsiswa.Models.SkorModel;
import com.potensiutama.lcgsiswa.R;

import java.util.ArrayList;

public class MyHighScoreAdapter extends BaseAdapter {
    Context context;
    private ArrayList<SkorModel> skorModelArrayList = new ArrayList<>();

    public void setSkorModelArrayList(ArrayList<SkorModel> skorModelArrayList) {
        this.skorModelArrayList = skorModelArrayList;
    }

    public MyHighScoreAdapter(Context context) {
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
        return itemView;
    }

    private class ViewHolder {
        private TextView txtNama, txtSkor, txtNomor;
        private ImageView imageView;

        ViewHolder(View view) {
            /*txtSkor = view.findViewById(R.id.txt_cv_skor);
            txtNama = view.findViewById(R.id.txt_cv_nama);
            txtNomor = view.findViewById(R.id.txt_cv_no);
            imageView = view.findViewById(R.id.imageView);*/
        }

        void bind(SkorModel skorModel) {
            //txtNomor.setText(skorModel.getRanking().toString());

            txtNama.setText(skorModel.getNama());
            txtSkor.setText(skorModel.getSkor().toString());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color2 = generator.getColor(txtNama.getText());

            char singlename = skorModel.getNama().charAt(0);

            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(String.valueOf(singlename), color2);
            imageView.setImageDrawable(drawable);

        }
    }
}
