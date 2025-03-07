package huehnerstall.kitaproject.model;

public class Rolle {
    private int rolleId;
    private String beruf;

    public Rolle() { }

    public Rolle(int rolleId, String beruf) {
        this.rolleId = rolleId;
        this.beruf = beruf;
    }

    public int getRolleId() {
        return rolleId;
    }

    public void setRolleId(int rolleId) {
        this.rolleId = rolleId;
    }

    public String getBeruf() {
        return beruf;
    }

    public void setBeruf(String beruf) {
        this.beruf = beruf;
    }

    @Override
    public String toString() {
        return beruf;
    }
}
