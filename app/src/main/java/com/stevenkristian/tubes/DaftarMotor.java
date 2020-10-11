package com.stevenkristian.tubes;

import java.util.ArrayList;

public class DaftarMotor {
    public ArrayList<Motor> MOTOR;

    public DaftarMotor(){
        MOTOR = new ArrayList();
        MOTOR.add(BEATBIRU);
        MOTOR.add(BEATHITAM);
        MOTOR.add(SCOPPY);
    }

    public static final Motor BEATBIRU = new Motor("Honda Beat", "Biru",
            "AB 123 CD", "2018", "Tidak", "150.000",
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_6.jpg");

    public static final Motor BEATHITAM = new Motor("Honda Beat", "Hitam",
            "AB 124 CD", "2018", "Tersedia", "150.000",
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_3.jpg");

    public static final Motor SCOPPY = new Motor("Honda Scoopy", "Hitam",
            "AB  321 CD", "2019", "Tersedia", "150.000",
            "https://static.wixstatic.com/media/64a57e_149b558797354cde8ef15a8ad2e04379.png/v1/fill/w_352,h_366,al_c,q_85,usm_0.66_1.00_0.01/64a57e_149b558797354cde8ef15a8ad2e04379.webp");
}
