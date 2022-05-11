package com.upu.lcgujiansekolahguru.model;

public class SoalModel {
    private String key;
    private String teksSoal, pilihanA,pilihanB,pilihanC,pilihanD,gambarSoal,jawabanBenar;
    private String nomorSoal;

    public String getNomorSoal() {
        return nomorSoal;
    }

    public void setNomorSoal(String nomorSoal) {
        this.nomorSoal = nomorSoal;
    }

    public SoalModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SoalModel(String key, String teksSoal, String pilihanA, String pilihanB, String pilihanC, String pilihanD, String gambarSoal, String jawabanBenar) {
        this.key = key;
        this.teksSoal = teksSoal;
        this.pilihanA = pilihanA;
        this.pilihanB = pilihanB;
        this.pilihanC = pilihanC;
        this.pilihanD = pilihanD;
        this.gambarSoal = gambarSoal;
        this.jawabanBenar = jawabanBenar;
    }

    public String getTeksSoal() {
        return teksSoal;
    }

    public void setTeksSoal(String teksSoal) {
        this.teksSoal = teksSoal;
    }

    public String getPilihanA() {
        return pilihanA;
    }

    public void setPilihanA(String pilihanA) {
        this.pilihanA = pilihanA;
    }

    public String getPilihanB() {
        return pilihanB;
    }

    public void setPilihanB(String pilihanB) {
        this.pilihanB = pilihanB;
    }

    public String getPilihanC() {
        return pilihanC;
    }

    public void setPilihanC(String pilihanC) {
        this.pilihanC = pilihanC;
    }

    public String getPilihanD() {
        return pilihanD;
    }

    public void setPilihanD(String pilihanD) {
        this.pilihanD = pilihanD;
    }

    public String getGambarSoal() {
        return gambarSoal;
    }

    public void setGambarSoal(String gambarSoal) {
        this.gambarSoal = gambarSoal;
    }

    public String getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(String jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }
}
