package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Kind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class KinderEditController {

    @FXML
    private TextField vornameField;
    @FXML
    private TextField nachnameField;
    @FXML
    private DatePicker geburtstagPicker;
    @FXML
    private ComboBox<String> standortComboBox, gruppenComboBox;
    @FXML
    private Button saveButton;

    private Kind kind; // Bestehendes Kind beim Bearbeiten, null beim Hinzufügen

    private final ObservableList<String> standortList = FXCollections.observableArrayList();
    private final ObservableList<String> gruppenList = FXCollections.observableArrayList();
    private final Map<String, Integer> gruppenMap = new HashMap<>(); // Gruppenname → Gruppen-ID
    private final Map<String, Integer> standortMap = new HashMap<>(); // Standortname → Standort-ID

    @FXML
    public void initialize() {
        loadStandorte();
        standortComboBox.setItems(standortList);
        gruppenComboBox.setItems(gruppenList);

        // Standort-Wechsel aktualisiert die Gruppen-ComboBox
        standortComboBox.setOnAction(event -> handleStandortChange());

        // Pflichtfelder-Überprüfung
        vornameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        nachnameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        geburtstagPicker.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        standortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        gruppenComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    /**
     * Lädt alle Standorte aus der Datenbank.
     */
    private void loadStandorte() {
        standortList.clear();
        standortMap.clear();

        String sql = "SELECT standort_id, standort_name FROM standort";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int standortId = rs.getInt("standort_id");
                String standortName = rs.getString("standort_name");
                standortList.add(standortName);
                standortMap.put(standortName, standortId); // Map für Zuordnung speichern
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wird aufgerufen, wenn ein Standort ausgewählt wird.
     * Lädt die passenden Gruppen für diesen Standort.
     */
    @FXML
    private void handleStandortChange() {
        String selectedStandort = standortComboBox.getValue();
        gruppenList.clear();
        gruppenMap.clear();
        gruppenComboBox.getSelectionModel().clearSelection();

        if (selectedStandort != null && standortMap.containsKey(selectedStandort)) {
            int standortId = standortMap.get(selectedStandort);

            String sql = "SELECT gruppe_id, gruppe_name FROM gruppe WHERE standort_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, standortId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int gruppeId = rs.getInt("gruppe_id");
                    String gruppeName = rs.getString("gruppe_name");
                    gruppenList.add(gruppeName);
                    gruppenMap.put(gruppeName, gruppeId); // Map für Zuordnung speichern
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            gruppenComboBox.setItems(gruppenList);
        }
    }

    /**
     * Wird aufgerufen, um das Formular mit einem vorhandenen Kind zu füllen (Bearbeiten).
     */
    public void setKind(Kind kind) {
        this.kind = kind;
        if (kind != null) {
            // Beim Bearbeiten: Felder mit den Werten des existierenden Kindes befüllen
            vornameField.setText(kind.getVorname());
            nachnameField.setText(kind.getNachname());
            geburtstagPicker.setValue(kind.getGeburtstag());
            if (kind.getStandort() != null) {
                standortComboBox.setValue(kind.getStandort());
                handleStandortChange(); // Lade die Gruppen für diesen Standort
            }
            if (kind.getGruppenName() != null) {
                gruppenComboBox.setValue(kind.getGruppenName());
            }
        } else {
            // Beim Hinzufügen: Felder leer lassen
            vornameField.clear();
            nachnameField.clear();
            geburtstagPicker.setValue(null);
            standortComboBox.getSelectionModel().clearSelection();
            gruppenComboBox.getSelectionModel().clearSelection();
        }
    }

    /**
     * Überprüft, ob alle Pflichtfelder ausgefüllt sind und aktiviert oder deaktiviert den Speichern-Button.
     */
    private void validateForm() {
        boolean isValid = !vornameField.getText().trim().isEmpty()
                && !nachnameField.getText().trim().isEmpty()
                && geburtstagPicker.getValue() != null
                && standortComboBox.getValue() != null
                && gruppenComboBox.getValue() != null;
        saveButton.setDisable(!isValid);
    }

    /**
     * Speichert das Kind in die Datenbank (Neuer Eintrag oder Update).
     */
    @FXML
    private void handleSave() {
        String vorname = vornameField.getText().trim();
        String nachname = nachnameField.getText().trim();
        LocalDate geburtstag = geburtstagPicker.getValue();
        String gruppe = gruppenComboBox.getValue();

        if (gruppe == null || vorname.isEmpty() || nachname.isEmpty() || geburtstag == null) {
            System.out.println("Fehler: Alle Felder müssen ausgefüllt sein!");
            return;
        }

        if (!gruppenMap.containsKey(gruppenComboBox.getValue())) {
            System.out.println("Fehler: Gruppen-ID konnte nicht gefunden werden für " + gruppenComboBox.getValue());
            return;
        }

        int gruppenId = gruppenMap.get(gruppenComboBox.getValue());

        String sql;
        if (kind == null) {
            sql = "INSERT INTO kind (vorname, nachname, geburtstag, gruppe_id) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE kind SET vorname=?, nachname=?, geburtstag=?, gruppe_id=? WHERE kind_id=?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, vorname);
            ps.setString(2, nachname);
            ps.setDate(3, Date.valueOf(geburtstag));
            ps.setInt(4, gruppenId);

            if (kind != null) {
                ps.setInt(5, kind.getKindId());
            }

            int affectedRows = ps.executeUpdate();
            System.out.println("Betroffene Zeilen: " + affectedRows);
            if (affectedRows == 0) {
                System.out.println("Fehler: Kein neuer Datensatz wurde in die Datenbank eingefügt!");
                return;
            }

            // Falls ein neues Kind eingefügt wurde, aktualisiere das Java-Objekt
            if (kind == null) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    kind = new Kind(newId, gruppenId, vorname, nachname, geburtstag);
                    System.out.println("Neues Kind mit ID: " + newId + " wurde hinzugefügt.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Fehler: " + e.getMessage());
            e.printStackTrace();
        }

        ((Stage) saveButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) saveButton.getScene().getWindow()).close();
    }

}