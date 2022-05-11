package com.upu.lcgujiansekolahguru.calback;

import com.upu.lcgujiansekolahguru.model.BankSoalModel;

import java.util.List;

public interface IKategoriSoalCallbackListener {
    void onKategoriSoalLoadSuccess(List<BankSoalModel> bankSoalModelList);
    void onKategoriSoalLoadFailed(String message);
}
