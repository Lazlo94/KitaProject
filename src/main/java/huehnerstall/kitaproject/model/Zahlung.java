package huehnerstall.kitaproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Zahlung {
    private int zahlungId;
    private int beitragId;
    private int vormundId;
    private LocalDate zahlungsdatum;
    private BigDecimal zahlungsbetrag;

    public Zahlung() { }

    public Zahlung(int zahlungId, int beitragId, int vormundId, LocalDate zahlungsdatum, BigDecimal zahlungsbetrag) {
        this.zahlungId = zahlungId;
        this.beitragId = beitragId;
        this.vormundId = vormundId;
        this.zahlungsdatum = zahlungsdatum;
        this.zahlungsbetrag = zahlungsbetrag;
    }

    public int getZahlungId() {
        return zahlungId;
    }

    public void setZahlungId(int zahlungId) {
        this.zahlungId = zahlungId;
    }

    public int getBeitragId() {
        return beitragId;
    }

    public void setBeitragId(int beitragId) {
        this.beitragId = beitragId;
    }

    public int getVormundId() {
        return vormundId;
    }

    public void setVormundId(int vormundId) {
        this.vormundId = vormundId;
    }

    public LocalDate getZahlungsdatum() {
        return zahlungsdatum;
    }

    public void setZahlungsdatum(LocalDate zahlungsdatum) {
        this.zahlungsdatum = zahlungsdatum;
    }

    public BigDecimal getZahlungsbetrag() {
        return zahlungsbetrag;
    }

    public void setZahlungsbetrag(BigDecimal zahlungsbetrag) {
        this.zahlungsbetrag = zahlungsbetrag;
    }

    @Override
    public String toString() {
        return "Zahlung " + zahlungId + ": " + zahlungsbetrag;
    }
}
