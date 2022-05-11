package com.potensiutama.lcgsiswa.Callback;





import com.potensiutama.lcgsiswa.Models.SoalModel;

import java.util.List;

public interface ISoalCallbackListener {
    void onOrderLoadSuccess(List<SoalModel> soalModelList);
    void onOrderLoadFailed(String message);
}
