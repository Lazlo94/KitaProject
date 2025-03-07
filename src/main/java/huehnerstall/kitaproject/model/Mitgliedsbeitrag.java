package huehnerstall.kitaproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Mitgliedsbeitrag {
    private int beitragId;
    private int kindId;
    private BigDecimal betrag;
    private LocalDate faelligkeitsdatum;
    private String zahlungsstatus;
    private String zahlungsart;

    public Mitgliedsbeitrag() { }

    public Mitgliedsbeitrag(int beitragId, int kindId, BigDecimal betrag, LocalDate faelligkeitsdatum, String zahlungsstatus, String zahlungsart) {
        this.beitragId = beitragId;
        this.kindId = kindId;
        this.betrag = betrag;
        this.faelligkeitsdatum = faelligkeitsdatum;
        this.zahlungsstatus = zahlungsstatus;
        this.zahlungsart = zahlungsart;
    }

    public int getBeitragId() {
        return beitragId;
    }

    public void setBeitragId(int beitragId) {
        this.beitragId = beitragId;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public LocalDate getFaelligkeitsdatum() {
        return faelligkeitsdatum;
    }

    public void setFaelligkeitsdatum(LocalDate faelligkeitsdatum) {
        this.faelligkeitsdatum = faelligkeitsdatum;
    }

    public String getZahlungsstatus() {
        return zahlungsstatus;
    }

    public void setZahlungsstatus(String zahlungsstatus) {
        this.zahlungsstatus = zahlungsstatus;
    }

    public String getZahlungsart() {
        return zahlungsart;
    }

    public void setZahlungsart(String zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    @Override
    public String toString() {
        return "Beitrag " + beitragId + ": " + betrag;
    }
}
