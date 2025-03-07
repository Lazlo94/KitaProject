package huehnerstall.kitaproject.model;

import java.time.LocalDate;

public class Dienstplan {
    private int dienstplanId;
    private LocalDate datum;
    private String dpStatus;  // z. B. "Geplant", "Gebucht", "Fehlgeschlagen"

    public Dienstplan() { }

    public Dienstplan(int dienstplanId, LocalDate datum, String dpStatus) {
        this.dienstplanId = dienstplanId;
        this.datum = datum;
        this.dpStatus = dpStatus;
    }

    public int getDienstplanId() {
        return dienstplanId;
    }

    public void setDienstplanId(int dienstplanId) {
        this.dienstplanId = dienstplanId;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getDpStatus() {
        return dpStatus;
    }

    public void setDpStatus(String dpStatus) {
        this.dpStatus = dpStatus;
    }

    @Override
    public String toString() {
        return "Dienstplan " + dienstplanId + " (" + datum + ")";
    }
}
