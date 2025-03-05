module huehnerstall.kitaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens huehnerstall.kitaproject to javafx.fxml;
    exports huehnerstall.kitaproject;
}