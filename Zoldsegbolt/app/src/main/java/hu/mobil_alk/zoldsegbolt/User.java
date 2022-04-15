package hu.mobil_alk.zoldsegbolt;

public class User {
    private String felhasznalonev;
    private String vezeteknev;
    private String keresztnev;
    private String email;
    private String jelszo;
    private String jelszoUjra;
    private String telefonszam;
    private String szallitasiCim;

    public User() {
    }

    public User(String felhasznalonev, String vezeteknev, String keresztnev, String email, String jelszo, String jelszoUjra, String telefonszam, String szallitasiCim) {
        this.felhasznalonev = felhasznalonev;
        this.vezeteknev = vezeteknev;
        this.keresztnev = keresztnev;
        this.email = email;
        this.jelszo = jelszo;
        this.jelszoUjra = jelszoUjra;
        this.telefonszam = telefonszam;
        this.szallitasiCim = szallitasiCim;
    }

    public String getFelhasznalonev() {
        return felhasznalonev;
    }

    public void setFelhasznalonev(String felhasznalonev) {
        this.felhasznalonev = felhasznalonev;
    }

    public String getVezeteknev() {
        return vezeteknev;
    }

    public void setVezeteknev(String vezeteknev) {
        this.vezeteknev = vezeteknev;
    }

    public String getKeresztnev() {
        return keresztnev;
    }

    public void setKeresztnev(String keresztnev) {
        this.keresztnev = keresztnev;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJelszo() {
        return jelszo;
    }

    public void setJelszo(String jelszo) {
        this.jelszo = jelszo;
    }

    public String getJelszoUjra() {
        return jelszoUjra;
    }

    public void setJelszoUjra(String jelszoUjra) {
        this.jelszoUjra = jelszoUjra;
    }

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }

    public String getSzallitasiCim() {
        return szallitasiCim;
    }

    public void setSzallitasiCim(String szallitasiCim) {
        this.szallitasiCim = szallitasiCim;
    }
}
