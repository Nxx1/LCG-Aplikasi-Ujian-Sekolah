package com.upu.lcgujiansekolahkepsek.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahkepsek.R;
import com.upu.lcgujiansekolahkepsek.common.Common;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {

    @BindView(R.id.home_jumlah_data_guru)
    TextView home_jumlah_data_guru;
    @BindView(R.id.home_jumlah_data_ujian)
    TextView home_jumlah_data_ujian;
    @BindView(R.id.home_jumlah_data_siswa)
    TextView home_jumlah_data_siswa;

    Unbinder unbinder;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,root);


        return root;
    }

    @Override
    public void onStart() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long jumlahDataUjian = dataSnapshot.child(Common.BANK_SOAL_REF).getChildrenCount();
                long jumlahDataGuru = dataSnapshot.child(Common.GURU_REF).getChildrenCount();
                long jumlahDataSiswa = dataSnapshot.child(Common.SISWA_REF).getChildrenCount();

                home_jumlah_data_ujian.setText(new StringBuilder(String.valueOf(jumlahDataUjian)).append(" Data"));
                home_jumlah_data_guru.setText(new StringBuilder(String.valueOf(jumlahDataGuru)).append(" Data"));
                home_jumlah_data_siswa.setText(new StringBuilder(String.valueOf(jumlahDataSiswa)).append(" Data"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });

        super.onStart();
    }
}