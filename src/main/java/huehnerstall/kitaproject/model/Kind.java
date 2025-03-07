package huehnerstall.kitaproject.model;

import java.time.LocalDate;

public class Kind {
    private int kindId;
    private Integer gruppeId; // kann null sein, wenn kein FK gesetzt ist
    private String vorname;
    private String nachname;
    private LocalDate geburtstag;

    public Kind() { }

    public Kind(int kindId, Integer gruppeId, String vorname, String nachname, LocalDate geburtstag) {
        this.kindId = kindId;
        this.gruppeId = gruppeId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtstag = geburtstag;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public Integer getGruppeId() {
        return gruppeId;
    }

    public void setGruppeId(Integer gruppeId) {
        this.gruppeId = gruppeId;
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

    public LocalDate getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(LocalDate geburtstag) {
        this.geburtstag = geburtstag;
    }

    @Override
    public String toString() {
        return vorname + " " + nachname;
    }
}