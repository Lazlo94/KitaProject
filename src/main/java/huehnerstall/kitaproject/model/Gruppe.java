package huehnerstall.kitaproject.model;

public class Gruppe {
    private int gruppeId;
    private String gruppeName;
    private int standortId;
    private String standortName; // Für die Anzeige

    public Gruppe() { }

    public Gruppe(int gruppeId, String gruppeName, int standortId, String standortName) {
        this.gruppeId = gruppeId;
        this.gruppeName = gruppeName;
        this.standortId = standortId;
        this.standortName = standortName;
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

    public String getStandortName() {
        return standortName;
    }

    public void setStandortName(String standortName) {
        this.standortName = standortName;
    }

    @Override
    public String toString() {
        return gruppeName + " (" + standortName + ")";
    }
}