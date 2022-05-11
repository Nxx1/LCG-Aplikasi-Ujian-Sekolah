package com.upu.lcgujiansekolahkepsek.calback;

import com.upu.lcgujiansekolahkepsek.model.MateriPembelajaranModel;

import java.util.List;

public interface IMateriPembelajaranCallbackListener {
    void onCategoryLoadSuccess(List<MateriPembelajaranModel> informasiModelList);
    void onCategoryLoadFailed(String message);
}
