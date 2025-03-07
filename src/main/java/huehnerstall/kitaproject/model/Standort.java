package huehnerstall.kitaproject.model;

public class Standort {
    private int standortId;
    private String standortName;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;

    public Standort() { }

    public Standort(int standortId, String standortName, String strasse, String hausnummer, String plz, String ort) {
        this.standortId = standortId;
        this.standortName = standortName;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
    }

    public int getStandortId() {
        return standortId;
    }

    public void setStandortId(int standortId) {
        this.standortId = standortId;
    }

    public String getStandortName() {
        return standortName;
    }

    public void setStandortName(String standortName) {
        this.standortName = standortName;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        return standortName;
    }
}
