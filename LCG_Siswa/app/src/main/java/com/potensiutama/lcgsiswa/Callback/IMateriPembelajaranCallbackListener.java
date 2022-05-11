package com.potensiutama.lcgsiswa.Callback;

import com.potensiutama.lcgsiswa.Models.MateriPembelajaranModel;

import java.util.List;

public interface IMateriPembelajaranCallbackListener {
    void onCategoryLoadSuccess(List<MateriPembelajaranModel> materiPembelajaranModelList);
    void onCategoryLoadFailed(String message);
}
