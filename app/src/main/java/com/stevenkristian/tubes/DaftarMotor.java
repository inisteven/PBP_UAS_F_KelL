package com.stevenkristian.tubes;

import com.stevenkristian.tubes.model.Motor;

import java.util.ArrayList;

public class DaftarMotor {
    public ArrayList<Motor> MOTOR;

    public DaftarMotor(){
        MOTOR = new ArrayList();
        MOTOR.add(BEATBIRU);
        MOTOR.add(BEATHITAM);
        MOTOR.add(SCOPPY);
        MOTOR.add(XRIDE);
        MOTOR.add(VARIO110);
    }

    public static final Motor BEATBIRU = new Motor(1,"Honda Beat", "Biru",
            "AB123CD", "2018", "Tidak", 60000,
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_6.jpg");

    public static final Motor BEATHITAM = new Motor(2,"Honda Beat", "Hitam",
            "AB124CD", "2018", "Tersedia", 60000,
            "https://d2pa5gi5n2e1an.cloudfront.net/webp/global/images/product/motorcycle/Honda_All_New_Beat/Honda_All_New_Beat_L_3.jpg");

    public static final Motor SCOPPY = new Motor(3,"Honda Scoopy", "Hitam",
            "AB381FD", "2019", "Tersedia", 70000,
            "https://static.wixstatic.com/media/64a57e_149b558797354cde8ef15a8ad2e04379.png/v1/fill/w_352,h_366,al_c,q_85,usm_0.66_1.00_0.01/64a57e_149b558797354cde8ef15a8ad2e04379.webp");

    public static final Motor XRIDE = new Motor(4,"Yamaha X Ride", "Hitam",
            "AB458CD", "2019", "Tersedia", 50000,
            "https://www.frentjogja.com/wp-content/uploads/2019/12/sewa-motor-xride.png");

    public static final Motor VARIO110 = new Motor(5,"Honda Vario 110", "Putih",
            "AB458CD", "2019", "Tersedia", 50000,
            "https://www.frentjogja.com/wp-content/uploads/2019/12/sewa-motor-honda-vario110.png");
}
