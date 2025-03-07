package huehnerstall.kitaproject.model;

public class Gruppe {
    private int gruppeId;
    private String gruppeName;
    private int standortId;

    public Gruppe() { }

    public Gruppe(int gruppeId, String gruppeName, int standortId) {
        this.gruppeId = gruppeId;
        this.gruppeName = gruppeName;
        this.standortId = standortId;
    }

    public int getGruppeId() {
        return gruppeId;
    }

    public void setGruppeId(int gruppeId) {
        this.gruppeId = gruppeId;
    }

    public String getGruppeName() {
        return gruppeName;
    }

    public void setGruppeName(String gruppeName) {
        this.gruppeName = gruppeName;
    }

    public int getStandortId() {
        return standortId;
    }

    public void setStandortId(int standortId) {
        this.standortId = standortId;
    }

    @Override
    public String toString() {
        return gruppeName;
    }
}
