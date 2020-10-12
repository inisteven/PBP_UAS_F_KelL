package com.stevenkristian.tubes;

import java.util.ArrayList;

public class DaftarPopular {
    public ArrayList<Popular> POPULAR;

    public DaftarMotor(){
        POPULAR = new ArrayList();
        POPULAR.add(BEATBIRU);
        POPULAR.add(BEATHITAM);
    }

    public static final Motor BEATBIRU = new Motor("Honda Beat", "Biru",
            "AB 123 CD", "2018", "Tidak", "150.000",
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_6.jpg");

    public static final Motor BEATHITAM = new Motor("Honda Beat", "Hitam",
            "AB 124 CD", "2018", "Tersedia", "150.000",
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_3.jpg");

}
