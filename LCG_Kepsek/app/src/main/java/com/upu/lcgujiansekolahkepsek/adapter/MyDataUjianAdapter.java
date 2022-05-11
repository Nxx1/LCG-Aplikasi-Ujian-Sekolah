package com.upu.lcgujiansekolahkepsek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyDataUjianAdapter extends RecyclerView.Adapter<MyDataUjianAdapter.MyViewHolder> {

    private Context context;
    private List<BankSoalModel> bankSoalModelList;


    public MyDataUjianAdapter(Context context, List<BankSoalModel> bankSoalModelList) {
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
        holder.txt_kode_ujian.setText(new StringBuilder("Kode Ujian : ")
                .append(bankSoalModelList.get(position).getKodeUndangan())
                .append("\nDurasi waktu ujian : ")
                .append(bankSoalModelList.get(position).getDurasi().toString())
                .append(" Menit")
        );
        holder.txt_nama_guru.setText(new StringBuilder("Guru : ")
                .append(bankSoalModelList.get(position).getNamaLengkapGuru())
                .append(" (")
                .append(bankSoalModelList.get(position).getUsernameGuru())
                .append(")")
        );

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.bankSoalModelSelected = bankSoalModelList.get(position);
                Common.dataUjianFragment.showDeleteDialog();
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.bankSoalModelSelected = bankSoalModelList.get(position);
                Common.dataUjianFragment.showUpdateDialog();
            }
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
        @BindView(R.id.txt_nama_guru)
        TextView txt_nama_guru;
        @BindView(R.id.btn_edit)
        ImageButton btn_edit;
        @BindView(R.id.btn_delete)
        ImageButton btn_delete;

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