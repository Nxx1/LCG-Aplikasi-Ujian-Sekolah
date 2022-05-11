package com.upu.lcgujiansekolahguru.model;

import java.util.List;

public class SkorModel {
    private String nama,nis,kodeUjian,namaUjian;
    private Integer skor;

    private List<JawabanSiswaModel> jawabanSiswaModelList;

    public List<JawabanSiswaModel> getJawabanSiswaModelList() {
        return jawabanSiswaModelList;
    }

    public void setJawabanSiswaModelList(List<JawabanSiswaModel> jawabanSiswaModelList) {
        this.jawabanSiswaModelList = jawabanSiswaModelList;
    }

    public SkorModel(String nama, String nis, String kodeUjian, String namaUjian, Integer skor) {
        this.nama = nama;
        this.nis = nis;
        this.kodeUjian = kodeUjian;
        this.namaUjian = namaUjian;
        this.skor = skor;
    }

    public SkorModel() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getKodeUjian() {
        return kodeUjian;
    }

    public void setKodeUjian(String kodeUjian) {
        this.kodeUjian = kodeUjian;
    }

    public String getNamaUjian() {
        return namaUjian;
    }

    public void setNamaUjian(String namaUjian) {
        this.namaUjian = namaUjian;
    }

    public Integer getSkor() {
        return skor;
    }

    public void setSkor(Integer skor) {
        this.skor = skor;
    }
}
