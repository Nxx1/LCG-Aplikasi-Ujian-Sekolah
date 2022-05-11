package com.upu.lcgujiansekolahadministrator.calback;

import com.upu.lcgujiansekolahadministrator.model.MateriPembelajaranModel;

import java.util.List;

public interface IMateriPembelajaranCallbackListener {
    void onCategoryLoadSuccess(List<MateriPembelajaranModel> informasiModelList);
    void onCategoryLoadFailed(String message);
}
