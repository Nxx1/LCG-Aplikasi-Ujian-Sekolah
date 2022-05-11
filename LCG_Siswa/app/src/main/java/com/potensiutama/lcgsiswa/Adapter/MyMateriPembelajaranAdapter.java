package com.potensiutama.lcgsiswa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potensiutama.lcgsiswa.Callback.IRecyclerClickListener;
import com.potensiutama.lcgsiswa.Common;
import com.potensiutama.lcgsiswa.Models.MateriPembelajaranModel;
import com.potensiutama.lcgsiswa.R;
import com.potensiutama.lcgsiswa.Ui.materi.SingleMateriActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyMateriPembelajaranAdapter extends RecyclerView.Adapter<MyMateriPembelajaranAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.img_materi)
        ImageView category_image;
        @BindView(R.id.txt_nama_materi)
        TextView category_name;

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
           // listener.onItemClickListener(v,getAdapterPosition());

            Common.materiPembelajaranModel = materiPembelajaranModelList.get(getAdapterPosition());

            Intent intent = new Intent(context, SingleMateriActivity.class);
            context.startActivity(intent);
        }
    }

    Context context;
    List<MateriPembelajaranModel> materiPembelajaranModelList;

    public MyMateriPembelajaranAdapter(Context context, List<MateriPembelajaranModel> materiPembelajaranModelList) {
        this.context = context;
        this.materiPembelajaranModelList = materiPembelajaranModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_materi_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(materiPembelajaranModelList.get(position).getImage())
                .into(holder.category_image);
        holder.category_name.setText(new StringBuilder(materiPembelajaranModelList.get(position).getName()));

        //Event Trigger


        holder.setListener((view, pos) -> {

        });
    }

    @Override
    public int getItemCount() {
        return materiPembelajaranModelList.size();
    }

    public MateriPembelajaranModel getItemAtPosition(int pos) {
        return materiPembelajaranModelList.get(pos);
    }




    @Override
    public int getItemViewType(int position) {
        if(materiPembelajaranModelList.size() == 1)
            return Common.DEFAULT_COLUMN_COUNT;
        else {
            if(materiPembelajaranModelList.size() % 2 == 0)
                return Common.DEFAULT_COLUMN_COUNT;
            else
                return  (position > 1 && position == materiPembelajaranModelList.size()-1) ? Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
        }
    }
}
