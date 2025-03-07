package huehnerstall.kitaproject.model;

public class Mitarbeiter {
    private int mitarbeiterId;
    private int rolleId;
    private String vorname;
    private String nachname;
    private String telefon;
    private String email;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;
    private String mitarbeiterRolle;

    public Mitarbeiter() { }

    public Mitarbeiter(int mitarbeiterId, int rolleId, String vorname, String nachname, String telefon,
                       String email, String strasse, String hausnummer, String plz, String ort, String mitarbeiterRolle) {
        this.mitarbeiterId = mitarbeiterId;
        this.rolleId = rolleId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefon = telefon;
        this.email = email;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.mitarbeiterRolle = mitarbeiterRolle;
    }

    public int getMitarbeiterId() {
        return mitarbeiterId;
    }

    public void setMitarbeiterId(int mitarbeiterId) {
        this.mitarbeiterId = mitarbeiterId;
    }

    public int getRolleId() {
        return rolleId;
    }

    public void setRolleId(int rolleId) {
        this.rolleId = rolleId;
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

    public String getMitarbeiterRolle() {
        return mitarbeiterRolle;
    }

    public void setMitarbeiterRolle(String mitarbeiterRolle) {
        this.mitarbeiterRolle = mitarbeiterRolle;
    }

    @Override
    public String toString() {
        return "ID: " + mitarbeiterId + "\n"
                + "Name: " + vorname + " " + nachname + "\n"
                + "Telefon: " + telefon + "\n"
                + "Email: " + email + "\n"
                + "Adresse: " + strasse + " " + hausnummer + ", " + plz + " " + ort + "\n"
                + "Berufsbezeichnung: " + mitarbeiterRolle;
    }
}
