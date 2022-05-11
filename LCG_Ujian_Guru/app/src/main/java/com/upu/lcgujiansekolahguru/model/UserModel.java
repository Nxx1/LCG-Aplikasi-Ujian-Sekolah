package com.upu.lcgujiansekolahguru.model;

public class UserModel {
    private String key;
    private String username,password,namalengkap;

    public UserModel() {
    }

    public UserModel(String key, String username, String password, String namalengkap) {
        this.key = key;
        this.username = username;
        this.password = password;
        this.namalengkap = namalengkap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }
}
