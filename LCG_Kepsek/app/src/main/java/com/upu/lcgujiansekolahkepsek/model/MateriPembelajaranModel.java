package com.upu.lcgujiansekolahkepsek.model;


public class MateriPembelajaranModel {
    private String menu_id,name,penjelasan,image;


    public MateriPembelajaranModel() {
    }

    public MateriPembelajaranModel(String menu_id, String name, String penjelasan, String image) {
        this.menu_id = menu_id;
        this.name = name;
        this.penjelasan = penjelasan;
        this.image = image;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
    }
}
