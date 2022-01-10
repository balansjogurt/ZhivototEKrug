package com.example.zhivototekrug;

public class Baranje {
    private String UserId;
    private String VolonterUId;
    private String AktivnostId;
    private String Aktivnost;
    private String OpisAktivnost;
    private String VremeOd;
    private String VremeDo;
    private String Datum;
    private String Den;
    private String Adresa;
    private String Status;
    private double Longitude;
    private double Latitude;
    private String EmailVolonter;
    private String TelefonVolonter;
    private double Rastojanie;
    private int OcenaVolonter;
    private int OcenaPostaroLice;
    private String IzvestajVolonter;
    private String IzvestajPostaroLice;
    private int StatusId;

    public Baranje() {

    }

    public Baranje(String userId,String volonterUId, String aktivnost, String opisAktivnost, String vremeOd, String vremeDo, String datum,
                   String den, String adresa, String status, double longitude, double latitude, String emailVolonter, String telefonVolonter,
                   int ocenaVolonter, int ocenaPostaroLice, String izvestajVolonter, String izvestajPostaroLice) {
        this.UserId = userId;
        this.VolonterUId = volonterUId;
        this.Aktivnost = aktivnost;
        this.OpisAktivnost = opisAktivnost;
        this.VremeOd = vremeOd;
        this.VremeDo = vremeDo;
        this.Datum = datum;
        this.Den = den;
        this.Adresa = adresa;
        this.Status = status;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.EmailVolonter = emailVolonter;
        this.TelefonVolonter = telefonVolonter;
        this.OcenaVolonter = ocenaVolonter;
        this.OcenaPostaroLice = ocenaPostaroLice;
        this.IzvestajVolonter = izvestajVolonter;
        this.IzvestajPostaroLice = izvestajPostaroLice;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAktivnostId() {
        return AktivnostId;
    }

    public void setAktivnostId(String aktivnostId) {
        AktivnostId = aktivnostId;
    }

    public String getAktivnost() {
        return Aktivnost;
    }

    public void setAktivnost(String aktivnost) {
        Aktivnost = aktivnost;
    }

    public String getOpisAktivnost() {
        return OpisAktivnost;
    }

    public void setOpisAktivnost(String opisAktivnost) {
        OpisAktivnost = opisAktivnost;
    }

    public String getVremeOd() {
        return VremeOd;
    }

    public void setVremeOd(String vremeOd) {
        VremeOd = vremeOd;
    }

    public String getVremeDo() {
        return VremeDo;
    }

    public void setVremeDo(String vremeDo) {
        VremeDo = vremeDo;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public String getDen() {
        return Den;
    }

    public void setDen(String den) {
        Den = den;
    }

    public String getAdresa() {
        return Adresa;
    }

    public void setAdresa(String adresa) {
        Adresa = adresa;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getEmailVolonter() {
        return EmailVolonter;
    }

    public void setEmailVolonter(String emailVolonter) {
        EmailVolonter = emailVolonter;
    }

    public String getTelefonVolonter() {
        return TelefonVolonter;
    }

    public void setTelefonVolonter(String telefonVolonter) {
        TelefonVolonter = telefonVolonter;
    }

    public double getRastojanie() {
        return Rastojanie;
    }

    public void setRastojanie(double rastojanie) {
        Rastojanie = rastojanie;
    }

    public String getVolonterUId() {
        return VolonterUId;
    }

    public void setVolonterUId(String volonterUId) {
        VolonterUId = volonterUId;
    }

    public int getOcenaVolonter() {
        return OcenaVolonter;
    }

    public void setOcenaVolonter(int ocenaVolonter) {
        OcenaVolonter = ocenaVolonter;
    }

    public int getOcenaPostaroLice() {
        return OcenaPostaroLice;
    }

    public void setOcenaPostaroLice(int ocenaPostaroLice) {
        OcenaPostaroLice = ocenaPostaroLice;
    }

    public String getIzvestajVolonter() {
        return IzvestajVolonter;
    }

    public void setIzvestajVolonter(String izvestajVolonter) {
        IzvestajVolonter = izvestajVolonter;
    }

    public String getIzvestajPostaroLice() {
        return IzvestajPostaroLice;
    }

    public void setIzvestajPostaroLice(String izvestajPostaroLice) {
        IzvestajPostaroLice = izvestajPostaroLice;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }
}