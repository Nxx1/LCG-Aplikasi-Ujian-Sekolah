package com.upu.lcgujiansekolahadministrator.calback;

import com.upu.lcgujiansekolahadministrator.model.SoalModel;

import java.util.List;

public interface ISoalCallbackListener {
    void onOrderLoadSuccess(List<SoalModel> soalModelList);
    void onOrderLoadFailed(String message);
}
