package com.stevenkristian.tubes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "fullname")
    public String fullname;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "ktp")
    public String ktp;

    public String imgURL;

    public User(int id, String fullname, String email, String password, String phone, String ktp, String imgURL) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.ktp = ktp;
        this.imgURL = imgURL;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFullname() { return fullname; }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getKtp() { return ktp; }

    public void setKtp(String ktp) { this.ktp = ktp; }

    public String getImgURL() { return imgURL; }

    public void setImgURL(String imgURL) { this.imgURL = imgURL; }
}
