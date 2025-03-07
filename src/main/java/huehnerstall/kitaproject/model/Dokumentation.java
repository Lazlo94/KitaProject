package huehnerstall.kitaproject.model;

import java.time.LocalDate;

public class Dokumentation {
    private int dokuId;
    private int kindId;
    private LocalDate dokuDatum;
    private String anwesenheit;
    private String bemerkung;

    public Dokumentation() { }

    public Dokumentation(int dokuId, int kindId, LocalDate dokuDatum, String anwesenheit, String bemerkung) {
        this.dokuId = dokuId;
        this.kindId = kindId;
        this.dokuDatum = dokuDatum;
        this.anwesenheit = anwesenheit;
        this.bemerkung = bemerkung;
    }

    public int getDokuId() {
        return dokuId;
    }

    public void setDokuId(int dokuId) {
        this.dokuId = dokuId;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public LocalDate getDokuDatum() {
        return dokuDatum;
    }

    public void setDokuDatum(LocalDate dokuDatum) {
        this.dokuDatum = dokuDatum;
    }

    public String getAnwesenheit() {
        return anwesenheit;
    }

    public void setAnwesenheit(String anwesenheit) {
        this.anwesenheit = anwesenheit;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    @Override
    public String toString() {
        return "Dokumentation " + dokuId;
    }
}
