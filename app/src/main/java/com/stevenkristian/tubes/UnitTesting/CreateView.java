package com.stevenkristian.tubes.UnitTesting;

import android.content.Context;

import com.stevenkristian.tubes.model.Motor;

public interface CreateView {
    String getMerk();
    void showMerkError(String message);
    String getPlat();
    void showPlatError(String message);
    String getWarna();
    void showWarnaError(String message);
    String getTahun();
    void showTahunError(String message);
    String getStatus();
    void showStatusError(String message);
    String getHarga();
    void showHargaError(String message);
    Context getContext();
    void startActivity();
    void showCreateError(String message);
    void showErrorResponse(String message);
}
