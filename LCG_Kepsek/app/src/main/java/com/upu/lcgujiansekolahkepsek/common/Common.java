package com.upu.lcgujiansekolahkepsek.common;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.upu.lcgujiansekolahkepsek.model.BankSoalModel;
import com.upu.lcgujiansekolahkepsek.model.GuruModel;
import com.upu.lcgujiansekolahkepsek.model.SiswaModel;
import com.upu.lcgujiansekolahkepsek.model.SoalModel;
import com.upu.lcgujiansekolahkepsek.model.MateriPembelajaranModel;
import com.upu.lcgujiansekolahkepsek.model.UserModel;
import com.upu.lcgujiansekolahkepsek.ui.DataUjian.DataUjianFragment;

import java.util.ArrayList;

public class Common {

    public static final String MATERI_REF = "Materi";
    public static final String BANK_SOAL_REF = "BankSoal";
    public static final String SOAL_REF = "Soal";
    public static final String GURU_REF = "Guru";
    public static final String SISWA_REF = "Siswa";
    public static final String SUPERADMIN_REF = "Superadmin";

    public static final String NILAI_REF = "Nilai";
    public static final String KEPSEK_REF = "Kepsek";
    public static final String BANTUAN_REF = "Bantuan";
    public static MateriPembelajaranModel materiPembelajaranModel;
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static SoalModel soalModelSelected;
    public static UserModel userModel;
    public static GuruModel guruModelSelected;
    public static SiswaModel siswaModelSelected;
    public static ArrayList<BankSoalModel> daftarKategoriBankSoalModel;
    public static BankSoalModel bankSoalModelSelected;

    public static DataUjianFragment dataUjianFragment;

    public enum ACTION{
        CREATE,
        UPDATE,
        DELETE
    }

    public static void setSpanStringColor(String welcome, String name, TextView textView, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(welcome);
        SpannableString spannableString = new SpannableString(name);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,0,name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color),0,name.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        textView.setText(builder,TextView.BufferType.SPANNABLE);
    }

}
