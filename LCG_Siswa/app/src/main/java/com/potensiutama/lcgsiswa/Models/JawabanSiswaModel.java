package com.potensiutama.lcgsiswa.Models;

public class JawabanSiswaModel {
    private String key;
    private String teksSoal,jawabanBenar,jawabanUser;
    private boolean isCorrect;

    private int nomorsoal;

    public int getNomorsoal() {
        return nomorsoal;
    }

    public void setNomorsoal(int nomorsoal) {
        this.nomorsoal = nomorsoal;
    }

    public JawabanSiswaModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTeksSoal() {
        return teksSoal;
    }

    public void setTeksSoal(String teksSoal) {
        this.teksSoal = teksSoal;
    }

    public String getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(String jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }

    public String getJawabanUser() {
        return jawabanUser;
    }

    public void setJawabanUser(String jawabanUser) {
        this.jawabanUser = jawabanUser;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
