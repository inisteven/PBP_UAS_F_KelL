package com.stevenkristian.tubes;



//import androidx.databinding.BindingAdapter;

//import com.bumptech.glide.Glide;

public class Motor  {

    public String merk;
    public String warna;
    public String plat;
    public String tahun;
    public String status;
    public String harga;
    public String imgURL;

    public Motor(String merk, String warna, String plat, String tahun, String status, String harga, String imgURL) {
        this.merk = merk;
        this.warna = warna;
        this.plat = plat;
        this.tahun = tahun;
        this.status = status;
        this.harga = harga;
        this.imgURL = imgURL;
    }

    public String getmerk()
    {
        return merk;
    }

    public void setmerk(String merk)
    {
        this.merk = merk;
    }

    public String getwarna()
    {
        return warna;
    }

    public void setwarna(String warna)
    {
        this.warna = warna;
    }

    public String getplat()
    {
        return plat;
    }

    public void setplat(String plat)
    {
        this.plat = plat;
    }

    public String gettahun()
    {
        return tahun;
    }

    public void settahun(String tahun)
    {
        this.tahun = tahun;
    }

    public String getstatus()
    {
        return status;
    }

    public void setstatus(String status)
    {
        this.status = status;
    }

    public String getharga()
    {
        return harga;
    }

    public void setharga(String harga)
    {
        this.harga = harga;
    }

    public String getImgURL()
    {
        return imgURL;
    }

    public void setImgURL(String imgURL)
    {
        this.imgURL = imgURL;
    }

    //@BindingAdapter("android:loadImplat")
   // public static void loadImplat (ImplatView imgView, String imgURL){
     //   Glide.with(imgView)
      //          .load(imgURL)
    //            .into(imgView);

   // }
}
