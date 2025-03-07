module huehnerstall.kitaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens huehnerstall.kitaproject to javafx.fxml;
    opens huehnerstall.kitaproject.controller to javafx.fxml;

    exports huehnerstall.kitaproject;
    exports huehnerstall.kitaproject.controller;
}