package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Standort;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class StandortEditController {

    @FXML
    private TextField standortNameField;
    @FXML
    private TextField strasseField;
    @FXML
    private TextField hausnummerField;
    @FXML
    private TextField plzField;
    @FXML
    private TextField ortField;
    @FXML
    private Button saveButton;

    private Standort standort; // Bei Bearbeitung: bestehender Datensatz; bei Hinzufügen: null

    /**
     * Setzt den aktuellen Standort; falls null, werden die Felder geleert.
     */
    public void setStandort(Standort standort) {
        this.standort = standort;
        if (standort != null) {
            standortNameField.setText(standort.getStandortName());
            strasseField.setText(standort.getStrasse());
            hausnummerField.setText(standort.getHausnummer());
            plzField.setText(standort.getPlz());
            ortField.setText(standort.getOrt());
        } else {
            standortNameField.clear();
            strasseField.clear();
            hausnummerField.clear();
            plzField.clear();
            ortField.clear();
        }
    }

    @FXML
    private void handleSave() {
        String name = standortNameField.getText().trim();
        String strasse = strasseField.getText().trim();
        String hausnummer = hausnummerField.getText().trim();
        String plz = plzField.getText().trim();
        String ort = ortField.getText().trim();

        if (name.isEmpty() || strasse.isEmpty() || hausnummer.isEmpty() || plz.isEmpty() || ort.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte alle Felder ausfüllen!");
            alert.showAndWait();
            return;
        }

        // Optional: PLZ validieren (genau 5 Ziffern)
        if (!plz.matches("\\d{5}")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "PLZ muss genau 5 Ziffern enthalten!");
            alert.showAndWait();
            return;
        }

        String sql;
        if (standort == null) {
            sql = "INSERT INTO standort (standort_name, strasse, hausnummer, plz, ort) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE standort SET standort_name=?, strasse=?, hausnummer=?, plz=?, ort=? WHERE standort_id=?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, strasse);
            ps.setString(3, hausnummer);
            ps.setString(4, plz);
            ps.setString(5, ort);

            if (standort != null) {
                ps.setInt(6, standort.getStandortId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kein Datensatz wurde gespeichert!");
                alert.showAndWait();
                return;
            }

            if (standort == null) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    standort = new Standort(newId, name, strasse, hausnummer, plz, ort);
                    System.out.println("Neuer Standort mit ID: " + newId + " wurde hinzugefügt.");
                }
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "SQL Fehler: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }

        ((Stage) saveButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) saveButton.getScene().getWindow()).close();
    }
}