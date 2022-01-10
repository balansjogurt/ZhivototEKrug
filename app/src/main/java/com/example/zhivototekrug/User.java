package com.example.zhivototekrug;

public class User {
    private String Email;
    private String Phone;
    private String Tip;
    private String Name;
    private int ZbirOceni;
    private int VkupnoOceni;

    public User() {

    }

    public User(String email, String phone, String tip, String name, int zbirOceni, int vkupnoOceni) {
        this.Email = email;
        this.Phone = phone;
        this.Tip = tip;
        this.Name = name;
        this.ZbirOceni = zbirOceni;
        this.VkupnoOceni = vkupnoOceni;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getZbirOceni() {
        return ZbirOceni;
    }

    public void setZbirOceni(int zbirOceni) {
        ZbirOceni = zbirOceni;
    }

    public int getVkupnoOceni() {
        return VkupnoOceni;
    }

    public void setVkupnoOceni(int vkupnoOceni) {
        VkupnoOceni = vkupnoOceni;
    }
}
