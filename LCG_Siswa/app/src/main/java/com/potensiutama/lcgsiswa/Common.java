package com.potensiutama.lcgsiswa;

import com.potensiutama.lcgsiswa.Models.BankSoalModel;
import com.potensiutama.lcgsiswa.Models.JawabanSiswaModel;
import com.potensiutama.lcgsiswa.Models.MateriPembelajaranModel;
import com.potensiutama.lcgsiswa.Models.SiswaModel;
import com.potensiutama.lcgsiswa.Models.SkorModel;

public class Common {

    public static final String MATERI_REF = "Materi";
    public static final String SOAL_REF = "Soal";
    public static final String BANTUAN_REF = "Bantuan";
    public static final String SKOR_REF = "Nilai";
    public static MateriPembelajaranModel materiPembelajaranModel;
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static int skorSoal;
    public static int ranking;
    public static String kodeUjian;


    public static SiswaModel siswaModel;
    public static BankSoalModel bankSoalModel;
    public static SkorModel skorModel;


    public static final String BANK_SOAL_REF = "BankSoal";
    public static final String SISWA_REF = "Siswa";

    public static JawabanSiswaModel detailJawabanModel;
}
