package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MitarbeiterEditController {

    @FXML
    private TextField vornameField;
    @FXML
    private TextField nachnameField;
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
    private TextField gehaltField;
    @FXML
    private ComboBox<String> rolleComboBox;
    @FXML
    private Button saveButton;

    // Mitarbeiter, der bearbeitet wird; null für neuen Mitarbeiter
    private Mitarbeiter mitarbeiter;

    // Für die Rollen: Liste und Map (Rollenname → Rollen-ID)
    private final ObservableList<String> rolleList = FXCollections.observableArrayList();
    private final Map<String, Integer> rolleMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadRollen();
    }

    /**
     * Lädt alle Rollen aus der Datenbank.
     */
    private void loadRollen() {
        rolleList.clear();
        rolleMap.clear();
        String sql = "SELECT rolle_id, beruf FROM rolle";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("rolle_id");
                String beruf = rs.getString("beruf");
                rolleList.add(beruf);
                rolleMap.put(beruf, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rolleComboBox.setItems(rolleList);
    }

    /**
     * Setzt den Mitarbeiter, dessen Daten im Formular angezeigt bzw. bearbeitet werden sollen.
     * Bei null werden die Felder geleert (Add-Modus).
     */
    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
        if (mitarbeiter != null) {
            vornameField.setText(mitarbeiter.getVorname());
            nachnameField.setText(mitarbeiter.getNachname());
            telefonField.setText(mitarbeiter.getTelefon());
            emailField.setText(mitarbeiter.getEmail());
            strasseField.setText(mitarbeiter.getStrasse());
            hausnummerField.setText(mitarbeiter.getHausnummer());
            plzField.setText(mitarbeiter.getPlz());
            ortField.setText(mitarbeiter.getOrt());
            gehaltField.setText(mitarbeiter.getGehalt().toString());
            // Setze Rolle vorausgewählt – Suche den Rollennamen anhand der Rollen-ID
            for (Map.Entry<String, Integer> entry : rolleMap.entrySet()) {
                if (entry.getValue() == mitarbeiter.getRolleId()) {
                    rolleComboBox.setValue(entry.getKey());
                    break;
                }
            }
        } else {
            vornameField.clear();
            nachnameField.clear();
            telefonField.clear();
            emailField.clear();
            strasseField.clear();
            hausnummerField.clear();
            plzField.clear();
            ortField.clear();
            gehaltField.clear();
            rolleComboBox.getSelectionModel().clearSelection();
        }
    }

    /**
     * Überprüft, ob alle Pflichtfelder ausgefüllt sind.
     */
    private boolean validateForm() {
        String email = emailField.getText().trim();
        // Einfaches Email-Format prüfen: mindestens ein Zeichen, @, mindestens ein Zeichen, Punkt, mindestens zwei Zeichen
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return false;
        }
        // PLZ muss genau 5 Ziffern enthalten
        if (!plzField.getText().trim().matches("\\d{5}")) {
            return false;
        }
        return !vornameField.getText().trim().isEmpty()
                && !nachnameField.getText().trim().isEmpty()
                && !telefonField.getText().trim().isEmpty()
                && !email.isEmpty()
                && !strasseField.getText().trim().isEmpty()
                && !hausnummerField.getText().trim().isEmpty()
                && !plzField.getText().trim().isEmpty()
                && !ortField.getText().trim().isEmpty()
                && !gehaltField.getText().trim().isEmpty()
                && rolleComboBox.getValue() != null;
    }

    /**
     * Speichert den Mitarbeiter in die Datenbank (INSERT oder UPDATE).
     */
    @FXML
    private void handleSave() {
        if (!validateForm()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte alle Pflichtfelder korrekt ausfüllen!\n(E-Mail muss dem Format ***@***.*** entsprechen, PLZ genau 5 Ziffern.)");
            alert.showAndWait();
            return;
        }

        String vorname = vornameField.getText().trim();
        String nachname = nachnameField.getText().trim();
        String telefon = telefonField.getText().trim();
        String email = emailField.getText().trim();
        String strasse = strasseField.getText().trim();
        String hausnummer = hausnummerField.getText().trim();
        String plz = plzField.getText().trim();
        String ort = ortField.getText().trim();

        // Bonus: Ersetze Komma durch Punkt beim Gehalt
        String gehaltText = gehaltField.getText().trim().replace(',', '.');
        BigDecimal gehalt;
        try {
            gehalt = new BigDecimal(gehaltText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte geben Sie ein gültiges Gehalt ein!");
            alert.showAndWait();
            return;
        }

        String rolleName = rolleComboBox.getValue();
        if (!rolleMap.containsKey(rolleName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ungültige Rolle ausgewählt!");
            alert.showAndWait();
            return;
        }
        int rolleId = rolleMap.get(rolleName);

        String sql;
        if (mitarbeiter == null) {
            // Neuer Mitarbeiter
            sql = "INSERT INTO mitarbeiter (rolle_id, vorname, nachname, telefon, email, strasse, hausnummer, plz, ort, gehalt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Bestehender Mitarbeiter aktualisieren
            sql = "UPDATE mitarbeiter SET rolle_id=?, vorname=?, nachname=?, telefon=?, email=?, strasse=?, hausnummer=?, plz=?, ort=?, gehalt=? WHERE mitarbeiter_id=?";
        }

        System.out.println("FINAL SQL: " + sql);
        System.out.println("Werte: " + rolleId + ", " + vorname + ", " + nachname + ", " + telefon + ", " + email
                + ", " + strasse + ", " + hausnummer + ", " + plz + ", " + ort + ", " + gehalt);

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, rolleId);
            ps.setString(2, vorname);
            ps.setString(3, nachname);
            ps.setString(4, telefon);
            ps.setString(5, email);
            ps.setString(6, strasse);
            ps.setString(7, hausnummer);
            ps.setString(8, plz);
            ps.setString(9, ort);
            ps.setBigDecimal(10, gehalt);

            if (mitarbeiter != null) {
                ps.setInt(11, mitarbeiter.getMitarbeiterId());
            }

            int affectedRows = ps.executeUpdate();
            System.out.println("Betroffene Zeilen: " + affectedRows);
            if (affectedRows == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kein Datensatz wurde gespeichert!");
                alert.showAndWait();
                return;
            }

            if (mitarbeiter == null) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    // Erstelle ein neues Mitarbeiter-Objekt; Mitarbeiterrolle wird später über die Rolle ermittelt
                    mitarbeiter = new Mitarbeiter(newId, rolleId, vorname, nachname, telefon, email, strasse, hausnummer, plz, ort, gehalt, "");
                    System.out.println("Neuer Mitarbeiter mit ID: " + newId + " wurde hinzugefügt.");
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