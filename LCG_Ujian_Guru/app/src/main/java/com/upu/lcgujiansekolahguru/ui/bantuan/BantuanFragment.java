package com.upu.lcgujiansekolahguru.ui.bantuan;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahguru.R;
import com.upu.lcgujiansekolahguru.common.Common;

public class BantuanFragment extends Fragment {

    private BantuanViewModel mViewModel;

    EditText edt_desc;

    public static BantuanFragment newInstance() {
        return new BantuanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.bantuan_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bantuan_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_save_bantuan);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BantuanViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edt_desc = (EditText) getView().findViewById(R.id.bantuan_description);
        loadBantuan();
        super.onViewCreated(view, savedInstanceState);
    }

    public void loadBantuan() {
        Query orderRef = FirebaseDatabase.getInstance().getReference(Common.BANTUAN_REF).child("deskripsi");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edt_desc.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_save_bantuan){
            saveBantuan();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBantuan() {
        FirebaseDatabase.getInstance()
                .getReference(Common.BANTUAN_REF)
                .child("deskripsi")
                .setValue(edt_desc.getText().toString())
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> {
                    Toast.makeText(getContext(), "Update Bantuan", Toast.LENGTH_SHORT).show();
                });
    }

}