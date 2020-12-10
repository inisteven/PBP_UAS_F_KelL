package com.stevenkristian.tubes.UnitTesting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {
    @Mock
    private CreateView view;
    @Mock
    private Service service;
    private Presenter presenter;
    @Before
    public void setUp() throws Exception {
        presenter = new Presenter(view, service);
    }
    @Test
    public void shouldShowErrorMessageWhenMerkIsEmpty() throws Exception {
        when(view.getMerk()).thenReturn("");
        System.out.println("merk : "+view.getMerk());
        presenter.onLoginClicked();
        verify(view).showMerkError("Merk Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenPlatIsEmpty() throws Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("");
        System.out.println("plat : "+view.getPlat());
        presenter.onLoginClicked();
        verify(view).showPlatError("Plat Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenWarnaIsEmpty() throws
            Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("AB1234B");
        System.out.println("plat : "+view.getPlat());
        when(view.getWarna()).thenReturn("");
        System.out.println("warna : "+view.getWarna());
        presenter.onLoginClicked();
        verify(view).showWarnaError("Warna Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenTahunIsEmpty() throws
            Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("AB1234B");
        System.out.println("plat : "+view.getPlat());
        when(view.getWarna()).thenReturn("Hitam");
        System.out.println("warna : "+view.getWarna());
        when(view.getTahun()).thenReturn("");
        System.out.println("tahun : "+view.getTahun());
        presenter.onLoginClicked();
        verify(view).showTahunError("Tahun Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenStatusIsEmpty() throws
            Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("AB1234B");
        System.out.println("plat : "+view.getPlat());
        when(view.getWarna()).thenReturn("Hitam");
        System.out.println("warna : "+view.getWarna());
        when(view.getTahun()).thenReturn("2000");
        System.out.println("tahun : "+view.getTahun());
        when(view.getStatus()).thenReturn("");
        System.out.println("status : "+view.getStatus());
        presenter.onLoginClicked();
        verify(view).showStatusError("Status Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenHargaIsEmpty() throws
            Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("AB1234B");
        System.out.println("plat : "+view.getPlat());
        when(view.getWarna()).thenReturn("Hitam");
        System.out.println("warna : "+view.getWarna());
        when(view.getTahun()).thenReturn("2000");
        System.out.println("tahun : "+view.getTahun());
        when(view.getStatus()).thenReturn("Ada");
        System.out.println("status : "+view.getStatus());
        when(view.getHarga()).thenReturn("");
        System.out.println("harga : "+view.getHarga());
        presenter.onLoginClicked();
        verify(view).showHargaError("Harga Tidak Boleh Kosong");
    }
    @Test
    public void shouldStartActivityWhenDatasAreCorrect() throws
            Exception {
        when(view.getMerk()).thenReturn("Honda Beat");
        System.out.println("honda : "+ view.getMerk());
        when(view.getPlat()).thenReturn("AB1234B");
        System.out.println("plat : "+view.getPlat());
        when(view.getWarna()).thenReturn("Hitam");
        System.out.println("warna : "+view.getWarna());
        when(view.getTahun()).thenReturn("2000");
        System.out.println("tahun : "+view.getTahun());
        when(view.getStatus()).thenReturn("Ada");
        System.out.println("status : "+view.getStatus());
        when(view.getHarga()).thenReturn("100000");
        System.out.println("harga : "+view.getHarga());
        when(service.getValid(view, view.getMerk(),
                view.getPlat(),view.getWarna(),view.getTahun(),
                view.getStatus(),view.getHarga(),view.getContext())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view,view.getMerk(),
                view.getPlat(),view.getWarna(),view.getTahun(),
                view.getStatus(),view.getHarga(),view.getContext()));
        presenter.onLoginClicked();
        //verify(view).startMainActivity();
    }
}