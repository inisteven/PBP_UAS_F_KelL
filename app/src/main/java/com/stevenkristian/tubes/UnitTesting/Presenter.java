package com.stevenkristian.tubes.UnitTesting;

import com.stevenkristian.tubes.model.Motor;

public class Presenter {
    private CreateView view;
    private Service service;
    private Callback callback;
    public Presenter(CreateView view, Service service) {
        this.view = view;
        this.service = service;
    }
    public void onLoginClicked() {
        if (view.getMerk().isEmpty()) {
            view.showMerkError("Merk tidak boleh kosong");
            return;
        } else if (String.valueOf(view.getHarga()).isEmpty()) {
            view.showHargaError("Harga tidak boleh kosong");
            return;
        }else if (view.getPlat().isEmpty()) {
            view.showPlatError("Plat tidak boleh kosong");
            return;
        }else if (view.getWarna().isEmpty()) {
            view.showWarnaError("Warna tidak boleh kosong");
            return;
        } else if (view.getTahun().isEmpty()) {
            view.showTahunError("Tahun tidak boleh kosong");
            return;
        }else if (view.getStatus().isEmpty()) {
            view.showStatusError("Status tidak boleh kosong");
            return;
        }else {
            service.login(view, view.getMerk(), view.getWarna(),
                    view.getPlat(),view.getTahun(),view.getStatus(),
                    view.getHarga(),view.getContext(),new
                    Callback() {
                        @Override
                        public void onSuccess(boolean value, Motor motor) {
                            view.startActivity();
                        }

                        @Override
                        public void onError() {
                        }
                    });
            return;
        }
    }
}
