package huehnerstall.kitaproject.model;

public class Vormund {
    private int vormundId;
    private String vorname;
    private String nachname;
    private String rolle;  // z. B. "Mutter", "Vater" o.ä.
    private String telefon;
    private String email;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;

    public Vormund() { }

    public Vormund(int vormundId, String vorname, String nachname, String rolle, String telefon,
                   String email, String strasse, String hausnummer, String plz, String ort) {
        this.vormundId = vormundId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.rolle = rolle;
        this.telefon = telefon;
        this.email = email;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
    }

    public int getVormundId() {
        return vormundId;
    }

    public void setVormundId(int vormundId) {
        this.vormundId = vormundId;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return vorname + " " + nachname;
    }
}
