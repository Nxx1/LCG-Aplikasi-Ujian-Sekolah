package com.upu.lcgujiansekolahkepsek.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.SoalModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MySoalAdapter extends RecyclerView.Adapter<MySoalAdapter.MyViewHolder> {


    Context context;
    List<SoalModel> soalModelList;

    public MySoalAdapter(Context context, List<SoalModel> soalModels) {
        this.context = context;
        this.soalModelList = soalModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_soal_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String pilihan = "(A) "+ soalModelList.get(position).getPilihanA();
        pilihan += "\n(B) "+ soalModelList.get(position).getPilihanB();
        pilihan += "\n(C) "+ soalModelList.get(position).getPilihanC();
        pilihan += "\n(D) "+ soalModelList.get(position).getPilihanD();
        Common.setSpanStringColor("", soalModelList.get(position).getTeksSoal() + "\nPilihan :\n"
                        +pilihan,
                holder.txt_nama_bahasa_daerah, Color.parseColor("#333639"));
        Common.setSpanStringColor("Jawaban : ", soalModelList.get(position).getJawabanBenar(),
                holder.txt_terjemahan, Color.parseColor("#333639"));

        holder.setRecyclerClickListener((view, pos) -> {
            Common.soalModelSelected = soalModelList.get(pos);
        });
    }


    public List<SoalModel> getListBahasa() {
        return soalModelList;
    }

    @Override
    public int getItemCount() {
        return soalModelList.size();
    }

    public SoalModel getItemAtPosition(int pos){
        return soalModelList.get(pos);
    }

    public void removeItem(int pos) {
        soalModelList.remove(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.txt_nama_bahasa_daerah)
        TextView txt_nama_bahasa_daerah;
        @BindView(R.id.txt_terjemahan)
        TextView txt_terjemahan;


        private Unbinder unbinder;

        IRecyclerClickListener recyclerClickListener;

        public void setRecyclerClickListener(IRecyclerClickListener recyclerClickListener) {
            this.recyclerClickListener = recyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerClickListener.onItemClickListener(view,getAdapterPosition());
        }
    }
}
