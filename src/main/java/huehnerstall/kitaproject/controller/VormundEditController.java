package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Vormund;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class VormundEditController {

    @FXML
    private TextField vornameField;
    @FXML
    private TextField nachnameField;
    @FXML
    private TextField rolleField;
    @FXML
    private TextField telefonField;
    @FXML
    private TextField emailField;
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

    private Vormund vormund;

    /**
     * Setzt den aktuellen Vormund oder leert die Felder, falls null.
     */
    public void setVormund(Vormund vormund) {
        this.vormund = vormund;
        if (vormund != null) {
            vornameField.setText(vormund.getVorname());
            nachnameField.setText(vormund.getNachname());
            rolleField.setText(vormund.getRolle());
            telefonField.setText(vormund.getTelefon());
            emailField.setText(vormund.getEmail());
            strasseField.setText(vormund.getStrasse());
            hausnummerField.setText(vormund.getHausnummer());
            plzField.setText(vormund.getPlz());
            ortField.setText(vormund.getOrt());
        } else {
            vornameField.clear();
            nachnameField.clear();
            rolleField.clear();
            telefonField.clear();
            emailField.clear();
            strasseField.clear();
            hausnummerField.clear();
            plzField.clear();
            ortField.clear();
        }
    }

    /**
     * Validiert, ob alle Pflichtfelder ausgefüllt sind.
     */
    private boolean validateForm() {
        String email = emailField.getText().trim();
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return false;
        }
        if (!plzField.getText().trim().matches("\\d{5}")) {
            return false;
        }
        return !vornameField.getText().trim().isEmpty()
                && !nachnameField.getText().trim().isEmpty()
                && !rolleField.getText().trim().isEmpty()
                && !telefonField.getText().trim().isEmpty()
                && !emailField.getText().trim().isEmpty()
                && !strasseField.getText().trim().isEmpty()
                && !hausnummerField.getText().trim().isEmpty()
                && !plzField.getText().trim().isEmpty()
                && !ortField.getText().trim().isEmpty();
    }

    /**
     * Speichert den Vormund in die Datenbank (INSERT oder UPDATE).
     */
    @FXML
    private void handleSave() {
        if (!validateForm()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Bitte alle Pflichtfelder korrekt ausfüllen!\n(E-Mail muss dem Format ***@***.*** entsprechen, PLZ genau 5 Ziffern.)");
            alert.showAndWait();
            return;
        }

        String vorname = vornameField.getText().trim();
        String nachname = nachnameField.getText().trim();
        String rolle = rolleField.getText().trim();
        String telefon = telefonField.getText().trim();
        String email = emailField.getText().trim();
        String strasse = strasseField.getText().trim();
        String hausnummer = hausnummerField.getText().trim();
        String plz = plzField.getText().trim();
        String ort = ortField.getText().trim();

        String sql;
        if (vormund == null) {
            // Neuer Vormund
            sql = "INSERT INTO vormund (vorname, nachname, rolle, telefon, email, strasse, hausnummer, plz, ort) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Bestehenden Vormund aktualisieren
            sql = "UPDATE vormund SET vorname=?, nachname=?, rolle=?, telefon=?, email=?, strasse=?, hausnummer=?, plz=?, ort=? " +
                    "WHERE vormund_id=?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, vorname);
            ps.setString(2, nachname);
            ps.setString(3, rolle);
            ps.setString(4, telefon);
            ps.setString(5, email);
            ps.setString(6, strasse);
            ps.setString(7, hausnummer);
            ps.setString(8, plz);
            ps.setString(9, ort);

            if (vormund != null) {
                ps.setInt(10, vormund.getVormundId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kein Datensatz wurde gespeichert!");
                alert.showAndWait();
                return;
            }

            if (vormund == null) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    vormund = new Vormund(newId, vorname, nachname, rolle, telefon, email, strasse, hausnummer, plz, ort);
                    System.out.println("Neuer Vormund mit ID " + newId + " wurde hinzugefügt.");
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