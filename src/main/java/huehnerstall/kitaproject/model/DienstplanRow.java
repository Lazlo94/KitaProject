package huehnerstall.kitaproject.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DienstplanRow {

    private final StringProperty groupName;
    private final StringProperty day1;
    private final StringProperty day2;
    private final StringProperty day3;
    private final StringProperty day4;
    private final StringProperty day5;
    private final StringProperty day6;
    private final StringProperty day7;

    public DienstplanRow(String groupName) {
        this.groupName = new SimpleStringProperty(groupName);
        this.day1 = new SimpleStringProperty("");
        this.day2 = new SimpleStringProperty("");
        this.day3 = new SimpleStringProperty("");
        this.day4 = new SimpleStringProperty("");
        this.day5 = new SimpleStringProperty("");
        this.day6 = new SimpleStringProperty("");
        this.day7 = new SimpleStringProperty("");
    }

    public String getGroupName() {
        return groupName.get();
    }

    public void setGroupName(String groupName) {
        this.groupName.set(groupName);
    }

    public StringProperty groupNameProperty() {
        return groupName;
    }

    public String getDay1() {
        return day1.get();
    }

    public void setDay1(String day1) {
        this.day1.set(day1);
    }

    public StringProperty day1Property() {
        return day1;
    }

    public String getDay2() {
        return day2.get();
    }

    public void setDay2(String day2) {
        this.day2.set(day2);
    }

    public StringProperty day2Property() {
        return day2;
    }

    public String getDay3() {
        return day3.get();
    }

    public void setDay3(String day3) {
        this.day3.set(day3);
    }

    public StringProperty day3Property() {
        return day3;
    }

    public String getDay4() {
        return day4.get();
    }

    public void setDay4(String day4) {
        this.day4.set(day4);
    }

    public StringProperty day4Property() {
        return day4;
    }

    public String getDay5() {
        return day5.get();
    }

    public void setDay5(String day5) {
        this.day5.set(day5);
    }

    public StringProperty day5Property() {
        return day5;
    }

    public String getDay6() {
        return day6.get();
    }

    public void setDay6(String day6) {
        this.day6.set(day6);
    }

    public StringProperty day6Property() {
        return day6;
    }

    public String getDay7() {
        return day7.get();
    }

    public void setDay7(String day7) {
        this.day7.set(day7);
    }

    public StringProperty day7Property() {
        return day7;
    }
}