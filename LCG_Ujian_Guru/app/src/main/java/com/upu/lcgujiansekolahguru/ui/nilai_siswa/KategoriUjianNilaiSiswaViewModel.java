package com.upu.lcgujiansekolahguru.ui.nilai_siswa;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahguru.calback.IKategoriSoalCallbackListener;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.BankSoalModel;

import java.util.ArrayList;
import java.util.List;

public class KategoriUjianNilaiSiswaViewModel extends ViewModel implements IKategoriSoalCallbackListener {

    private MutableLiveData<List<BankSoalModel>> listMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IKategoriSoalCallbackListener iKategoriSoalCallbackListener;

    public KategoriUjianNilaiSiswaViewModel() {
        iKategoriSoalCallbackListener = this;
    }

    public MutableLiveData<List<BankSoalModel>> getListMutableLiveData() {
        if (listMutableLiveData == null){
            listMutableLiveData = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadKategoriSoal();
        }
        return listMutableLiveData;

    }

    public void loadKategoriSoal() {
        Common.daftarKategoriBankSoalModel = new ArrayList<>();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference(Common.BANK_SOAL_REF);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    BankSoalModel bankSoalModel = itemSnapshot.getValue(BankSoalModel.class);
                    bankSoalModel.setJumlahData("Siswa yang telah melaksanakan ujian : "+ String.valueOf(itemSnapshot.child(Common.NILAI_REF).getChildrenCount()));
                    if(bankSoalModel.getUsernameGuru().equals(Common.userModel.getKey())){
                        Common.daftarKategoriBankSoalModel.add(bankSoalModel);
                    }
                }
                iKategoriSoalCallbackListener.onKategoriSoalLoadSuccess(Common.daftarKategoriBankSoalModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iKategoriSoalCallbackListener.onKategoriSoalLoadFailed(databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onKategoriSoalLoadSuccess(List<BankSoalModel> bankSoalModelList) {
        listMutableLiveData.setValue(bankSoalModelList);
    }

    @Override
    public void onKategoriSoalLoadFailed(String message) {
        messageError.setValue(message);
    }
}