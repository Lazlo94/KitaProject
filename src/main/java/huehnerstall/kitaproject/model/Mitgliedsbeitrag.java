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

    private String kindVorname;
    private String kindNachname;

    public Mitgliedsbeitrag() { }

    public Mitgliedsbeitrag(int beitragId, int kindId, BigDecimal betrag, LocalDate faelligkeitsdatum, String zahlungsstatus, String zahlungsart, String kindVorname, String kindNachname) {
        this.beitragId = beitragId;
        this.kindId = kindId;
        this.betrag = betrag;
        this.faelligkeitsdatum = faelligkeitsdatum;
        this.zahlungsstatus = zahlungsstatus;
        this.zahlungsart = zahlungsart;
        this.kindVorname = kindVorname;
        this.kindNachname = kindNachname;
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


    public String getKindVorname() {
        return kindVorname;
    }

    public void setKindVorname(String kindVorname) {
        this.kindVorname = kindVorname;
    }

    public String getKindNachname() {
        return kindNachname;
    }

    public void setKindNachname(String kindNachname) {
        this.kindNachname = kindNachname;
    }

    @Override
    public String toString() {
        return "Beitrag " + beitragId + ": " + betrag;
    }
}
