package com.upu.lcgujiansekolahguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.BankSoalModel;
import com.upu.lcgujiansekolahguru.ui.nilai_siswa.NilaiSiswaActivity;
import com.upu.lcgujiansekolahguru.ui.soal.SoalActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyKategoriUjianNilaiSiswaAdapter extends RecyclerView.Adapter<MyKategoriUjianNilaiSiswaAdapter.MyViewHolder> {

    private Context context;
    private List<BankSoalModel> bankSoalModelList;


    public MyKategoriUjianNilaiSiswaAdapter(Context context, List<BankSoalModel> bankSoalModelList) {
        this.context = context;
        this.bankSoalModelList = bankSoalModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.card_item_kategori_soal,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_nama_ujian.setText(new StringBuilder("")
                .append(bankSoalModelList.get(position).getNamaUjian()));
        holder.txt_kelas.setText(new StringBuilder("Kelas : ")
                .append(bankSoalModelList.get(position).getKelas()));
        holder.txt_kode_ujian.setText(new StringBuilder("Kode Ujian :\n")
                .append(bankSoalModelList.get(position).getKodeUndangan()));
        holder.txt_jumlah_data.setText(new StringBuilder("")
                .append(bankSoalModelList.get(position).getJumlahData()));

        //Event
        holder.setListener((view, pos) -> {
            Common.bankSoalModelSelected = bankSoalModelList.get(pos);
            Intent intent = new Intent(context.getApplicationContext(), NilaiSiswaActivity.class);
            context.startActivity(intent);
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
        @BindView(R.id.txt_kode_ujian)
        TextView txt_kode_ujian;
        @BindView(R.id.txt_jumlah_data)
        TextView txt_jumlah_data;

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