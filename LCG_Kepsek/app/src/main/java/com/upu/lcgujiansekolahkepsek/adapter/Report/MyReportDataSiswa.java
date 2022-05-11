package com.upu.lcgujiansekolahkepsek.adapter.Report;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.calback.IRecyclerClickListener;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.model.SiswaModel;
import com.upu.lcgujiansekolahkepsek.ui.Report.LaporanDetailDataNilaiSiswaActivity;
import com.upu.lcgujiansekolahkepsek.ui.Report.LaporanDetailDataUjianActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyReportDataSiswa extends RecyclerView.Adapter<MyReportDataSiswa.MyViewHolder> {

    private Context context;
    private List<SiswaModel> siswaModelList;


    public MyReportDataSiswa(Context context, List<SiswaModel> siswaModelList) {
        this.context = context;
        this.siswaModelList = siswaModelList;
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
                .append(siswaModelList.get(position).getNamalengkap()));

        holder.txt_nilai.setVisibility(View.GONE);



        holder.setListener((view, pos) -> {
            Common.siswaModelSelected = siswaModelList.get(pos);
            Toast.makeText(context, "Laporan nilai "+Common.siswaModelSelected.getNamalengkap(), Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LaporanDetailDataNilaiSiswaActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return siswaModelList.size();
    }

    public SiswaModel getItemAtPosition(int pos){
        return siswaModelList.get(pos);
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
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}
