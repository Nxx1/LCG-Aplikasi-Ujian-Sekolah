package com.upu.lcgujiansekolahkepsek.calback;

import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;

import java.util.List;

public interface IKategoriSoalCallbackListener {
    void onKategoriSoalLoadSuccess(List<BankSoalModel> bankSoalModelList);
    void onKategoriSoalLoadFailed(String message);
}
