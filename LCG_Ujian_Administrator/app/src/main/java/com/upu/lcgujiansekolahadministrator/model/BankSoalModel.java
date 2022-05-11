package com.upu.lcgujiansekolahadministrator.model;

public class BankSoalModel {
    private String namaUjian, kelas,kodeUndangan,userCreator,namaLengkapGuru,usernameGuru;

    public String getNamaLengkapGuru() {
        return namaLengkapGuru;
    }

    public void setNamaLengkapGuru(String namaLengkapGuru) {
        this.namaLengkapGuru = namaLengkapGuru;
    }

    public String getUsernameGuru() {
        return usernameGuru;
    }

    public void setUsernameGuru(String usernameGuru) {
        this.usernameGuru = usernameGuru;
    }

    private Integer durasi;

    public Integer getDurasi() {
        return durasi;
    }

    public void setDurasi(Integer durasi) {
        this.durasi = durasi;
    }

    private int positionInList = -1;

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    public BankSoalModel() {
    }

    public BankSoalModel(String namaUjian, String kelas, String kodeUndangan, String userCreator, int positionInList) {
        this.namaUjian = namaUjian;
        this.kelas = kelas;
        this.kodeUndangan = kodeUndangan;
        this.userCreator = userCreator;
        this.positionInList = positionInList;
    }

    public String getNamaUjian() {
        return namaUjian;
    }

    public void setNamaUjian(String namaUjian) {
        this.namaUjian = namaUjian;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getKodeUndangan() {
        return kodeUndangan;
    }

    public void setKodeUndangan(String kodeUndangan) {
        this.kodeUndangan = kodeUndangan;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }
}
