package com.stevenkristian.tubes.model;

import java.io.Serializable;

public class History implements Serializable {

    public int id;
    public String id_user;
    public String id_motor;
    public String tglPinjam;
    public String tglKembali;

    public History(String id_User, String id_Motor, String tglPinjam, String tglKembali) {
        this.id_user = id_User;
        this.id_motor = id_Motor;
        this.tglPinjam = tglPinjam;
        this.tglKembali = tglKembali;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getId_User() { return id_user; }

    public void setId_User(String id_User) { this.id_user = id_User; }

    public String getId_Motor() { return id_motor; }

    public void setId_Motor(String id_Motor) { this.id_motor = id_Motor; }

    public String getTglPinjam() { return tglPinjam; }

    public void setTglPinjam(String tglPinjam) { this.tglPinjam = tglPinjam; }

    public String getTglKembali() { return tglKembali; }

    public void setTglKembali(String tglKembali) { this.tglKembali = tglKembali; }
}
