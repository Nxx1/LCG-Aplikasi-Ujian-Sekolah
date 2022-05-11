package com.upu.lcgujiansekolahguru.ui.materi;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upu.lcgujiansekolahguru.calback.IMateriPembelajaranCallbackListener;
import com.upu.lcgujiansekolahguru.common.Common;
import com.upu.lcgujiansekolahguru.model.MateriPembelajaranModel;

import java.util.ArrayList;
import java.util.List;

public class MateriPembelajaranViewModel extends ViewModel implements IMateriPembelajaranCallbackListener {
    private MutableLiveData<List<MateriPembelajaranModel>> categoryListMultable;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IMateriPembelajaranCallbackListener iMateriPembelajaranCallbackListener;

    public MateriPembelajaranViewModel() {
        iMateriPembelajaranCallbackListener = this;

    }

    public MutableLiveData<List<MateriPembelajaranModel>> getCategoryListMultable() {
        if (categoryListMultable == null){
            categoryListMultable = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadCategories();
        }
        return categoryListMultable;

    }

    public void loadCategories() {
        List<MateriPembelajaranModel> tempList = new ArrayList<>();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference(Common.MATERI_REF);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    MateriPembelajaranModel informasiModel = itemSnapshot.getValue(MateriPembelajaranModel.class);
                    informasiModel.setMenu_id(itemSnapshot.getKey());
                    tempList.add(informasiModel);
                }
                iMateriPembelajaranCallbackListener.onCategoryLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iMateriPembelajaranCallbackListener.onCategoryLoadFailed(databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onCategoryLoadSuccess(List<MateriPembelajaranModel> informasiModelList) {
        categoryListMultable.setValue(informasiModelList);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue(message);
    }
}
