package huehnerstall.kitaproject.model;

import java.time.LocalTime;

public class MitarbeiterDienstplan {
    private int mitarbeiterDienstplanId;
    private int mitarbeiterId;
    private int dienstplanId;
    private Integer gruppeId;  // kann null sein
    private LocalTime startzeit;
    private LocalTime endzeit;
    private String bemerkung;

    public MitarbeiterDienstplan() { }

    public MitarbeiterDienstplan(int mitarbeiterDienstplanId, int mitarbeiterId, int dienstplanId, Integer gruppeId,
                                 LocalTime startzeit, LocalTime endzeit, String bemerkung) {
        this.mitarbeiterDienstplanId = mitarbeiterDienstplanId;
        this.mitarbeiterId = mitarbeiterId;
        this.dienstplanId = dienstplanId;
        this.gruppeId = gruppeId;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.bemerkung = bemerkung;
    }

    public int getMitarbeiterDienstplanId() {
        return mitarbeiterDienstplanId;
    }

    public void setMitarbeiterDienstplanId(int mitarbeiterDienstplanId) {
        this.mitarbeiterDienstplanId = mitarbeiterDienstplanId;
    }

    public int getMitarbeiterId() {
        return mitarbeiterId;
    }

    public void setMitarbeiterId(int mitarbeiterId) {
        this.mitarbeiterId = mitarbeiterId;
    }

    public int getDienstplanId() {
        return dienstplanId;
    }

    public void setDienstplanId(int dienstplanId) {
        this.dienstplanId = dienstplanId;
    }

    public Integer getGruppeId() {
        return gruppeId;
    }

    public void setGruppeId(Integer gruppeId) {
        this.gruppeId = gruppeId;
    }

    public LocalTime getStartzeit() {
        return startzeit;
    }

    public void setStartzeit(LocalTime startzeit) {
        this.startzeit = startzeit;
    }

    public LocalTime getEndzeit() {
        return endzeit;
    }

    public void setEndzeit(LocalTime endzeit) {
        this.endzeit = endzeit;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    @Override
    public String toString() {
        return "Dienstplan-Eintrag für Mitarbeiter " + mitarbeiterId;
    }
}
