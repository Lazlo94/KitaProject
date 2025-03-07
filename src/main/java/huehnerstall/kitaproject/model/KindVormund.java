package huehnerstall.kitaproject.model;

public class KindVormund {
    private int kindVormundId;
    private int kindId;
    private int vormundId;
    private String vertrauenspersonen;

    public KindVormund() { }

    public KindVormund(int kindVormundId, int kindId, int vormundId, String vertrauenspersonen) {
        this.kindVormundId = kindVormundId;
        this.kindId = kindId;
        this.vormundId = vormundId;
        this.vertrauenspersonen = vertrauenspersonen;
    }

    public int getKindVormundId() {
        return kindVormundId;
    }

    public void setKindVormundId(int kindVormundId) {
        this.kindVormundId = kindVormundId;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public int getVormundId() {
        return vormundId;
    }

    public void setVormundId(int vormundId) {
        this.vormundId = vormundId;
    }

    public String getVertrauenspersonen() {
        return vertrauenspersonen;
    }

    public void setVertrauenspersonen(String vertrauenspersonen) {
        this.vertrauenspersonen = vertrauenspersonen;
    }

    @Override
    public String toString() {
        return "KindVormund: Kind " + kindId + " – Vormund " + vormundId;
    }
}
