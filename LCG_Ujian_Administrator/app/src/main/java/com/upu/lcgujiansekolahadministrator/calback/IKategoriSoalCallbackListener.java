package com.upu.lcgujiansekolahadministrator.calback;

import com.upu.lcgujiansekolahadministrator.model.BankSoalModel;

import java.util.List;

public interface IKategoriSoalCallbackListener {
    void onKategoriSoalLoadSuccess(List<BankSoalModel> bankSoalModelList);
    void onKategoriSoalLoadFailed(String message);
}
