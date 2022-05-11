package com.upu.lcgujiansekolahguru.calback;

import com.upu.lcgujiansekolahguru.model.MateriPembelajaranModel;

import java.util.List;

public interface IMateriPembelajaranCallbackListener {
    void onCategoryLoadSuccess(List<MateriPembelajaranModel> informasiModelList);
    void onCategoryLoadFailed(String message);
}
