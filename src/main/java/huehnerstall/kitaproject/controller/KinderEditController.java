package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Kind;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class KinderEditController {

    @FXML
    private TextField vornameField;
    @FXML
    private TextField nachnameField;
    @FXML
    private DatePicker geburtstagPicker;
    @FXML
    private TextField gruppenNameField;  // Optional, falls ihr den Gruppennamen direkt bearbeiten möchtet
    @FXML
    private TextField standortField;      // Optional

    private Kind kind; // Beim Bearbeiten ist dies der bestehende Datensatz, beim Hinzufügen null

    /**
     * Wird im Add-Modus aufgerufen.
     */
    public void setKindForAdd() {
        this.kind = null;
        // Setzt Standardwerte
        vornameField.setText("");
        nachnameField.setText("");
        geburtstagPicker.setValue(LocalDate.now());
        gruppenNameField.setText("");
        standortField.setText("");
    }

    /**
     * Wird im Edit-Modus aufgerufen. Füllt die Felder mit den Werten des bestehenden Kind-Eintrags.
     */
    public void setKindForEdit(Kind kind) {
        this.kind = kind;
        vornameField.setText(kind.getVorname());
        nachnameField.setText(kind.getNachname());
        geburtstagPicker.setValue(kind.getGeburtstag());
        gruppenNameField.setText(kind.getGruppenName());
        standortField.setText(kind.getStandort());
    }

    @FXML
    private void handleSave() {
        String vorname = vornameField.getText();
        String nachname = nachnameField.getText();
        LocalDate geburtstag = geburtstagPicker.getValue();
        String gruppenName = gruppenNameField.getText();
        String standort = standortField.getText();

        if (vorname.isEmpty() || nachname.isEmpty() || geburtstag == null) {
            // Hier könnt ihr z. B. einen Warnungsdialog anzeigen.
            System.out.println("Bitte füllen Sie alle Pflichtfelder aus!");
            return;
        }

        if (kind == null) {
            // Hinzufügen (INSERT)
            String sql = "INSERT INTO kind (vorname, nachname, geburtstag, gruppe_id) VALUES (?, ?, ?, ?)";
            // Hinweis: Um den Gruppennamen in eine Gruppe zu übersetzen, müsstet ihr ggf. zuerst die Gruppe ermitteln.
            // Für dieses Beispiel setzen wir den Gruppen-FK auf NULL.
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, vorname);
                ps.setString(2, nachname);
                ps.setDate(3, java.sql.Date.valueOf(geburtstag));
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Bearbeiten (UPDATE)
            String sql = "UPDATE kind SET vorname = ?, nachname = ?, geburtstag = ? WHERE kind_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, vorname);
                ps.setString(2, nachname);
                ps.setDate(3, java.sql.Date.valueOf(geburtstag));
                ps.setInt(4, kind.getKindId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Schließt das Popup
        Stage stage = (Stage) vornameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) vornameField.getScene().getWindow();
        stage.close();
    }
}
