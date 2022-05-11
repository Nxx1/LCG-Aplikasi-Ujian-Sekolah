package com.upu.lcgujiansekolahguru.calback;

import com.upu.lcgujiansekolahguru.model.SoalModel;

import java.util.List;

public interface ISoalCallbackListener {
    void onOrderLoadSuccess(List<SoalModel> soalModelList);
    void onOrderLoadFailed(String message);
}
