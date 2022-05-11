package com.upu.lcgujiansekolahkepsek.calback;

import com.upu.lcgujiansekolahkepsek.model.SoalModel;

import java.util.List;

public interface ISoalCallbackListener {
    void onOrderLoadSuccess(List<SoalModel> soalModelList);
    void onOrderLoadFailed(String message);
}
