package com.upu.lcgujiansekolahadministrator.ui.DataUjian;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahadministrator.calback.IKategoriSoalCallbackListener;
import com.upu.lcgujiansekolahadministrator.common.Common;
import com.upu.lcgujiansekolahadministrator.model.BankSoalModel;

import java.util.ArrayList;
import java.util.List;

public class DataUjianViewModel extends ViewModel implements IKategoriSoalCallbackListener {

    private MutableLiveData<List<BankSoalModel>> listMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IKategoriSoalCallbackListener iKategoriSoalCallbackListener;

    public DataUjianViewModel() {
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
                    Common.daftarKategoriBankSoalModel.add(bankSoalModel);

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