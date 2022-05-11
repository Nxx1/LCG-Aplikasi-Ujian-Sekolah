package com.upu.lcgujiansekolahkepsek.adapter.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahkepsek.model.SkorModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyReportDetailDataNilaiSiswa extends RecyclerView.Adapter<MyReportDetailDataNilaiSiswa.MyViewHolder> {

    private Context context;
    private List<SkorModel> skorModelList;


    public MyReportDetailDataNilaiSiswa(Context context, List<SkorModel> skorModels) {
        this.context = context;
        this.skorModelList = skorModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nilai, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_nama.setText(new StringBuilder("")
                .append(skorModelList.get(position).getNamaUjian()));
        holder.txt_nilai.setText(new StringBuilder("Nilai : ")
                .append(skorModelList.get(position).getSkor()));
    }

    @Override
    public int getItemCount() {
        return skorModelList.size();
    }

    public SkorModel getItemAtPosition(int pos){
        return skorModelList.get(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_nama)
        TextView txt_nama;
        @BindView(R.id.txt_nilai)
        TextView txt_nilai;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}