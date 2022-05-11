package com.upu.lcgujiansekolahkepsek.adapter.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.ui.Report.LaporanDetailDataUjianActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyReportDataUjian extends RecyclerView.Adapter<MyReportDataUjian.MyViewHolder> {

    private Context context;
    private List<BankSoalModel> bankSoalModelList;


    public MyReportDataUjian(Context context, List<BankSoalModel> bankSoalModelList) {
        this.context = context;
        this.bankSoalModelList = bankSoalModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_ujian, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_nama_ujian.setText(new StringBuilder("")
                .append(bankSoalModelList.get(position).getNamaUjian()));
        holder.txt_kelas.setText(new StringBuilder("Kelas : ")
                .append(bankSoalModelList.get(position).getKelas()));
        holder.txt_nama_guru.setText(new StringBuilder("Guru : ")
                .append(bankSoalModelList.get(position).getNamaLengkapGuru())
                .append(" (")
                .append(bankSoalModelList.get(position).getUsernameGuru())
                .append(")")
        );

        holder.setListener((view, pos) -> {
            Common.bankSoalModelSelected = bankSoalModelList.get(pos);
            Toast.makeText(context, "Laporan ujian "+Common.bankSoalModelSelected.getNamaUjian(), Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LaporanDetailDataUjianActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return bankSoalModelList.size();
    }

    public BankSoalModel getItemAtPosition(int pos){
        return bankSoalModelList.get(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_nama_ujian)
        TextView txt_nama_ujian;
        @BindView(R.id.txt_kelas)
        TextView txt_kelas;
        @BindView(R.id.txt_nama_guru)
        TextView txt_nama_guru;

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
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}