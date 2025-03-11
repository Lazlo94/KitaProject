package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Gruppe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class GruppeEditController {

    @FXML
    private TextField gruppenNameField;
    @FXML
    private ComboBox<String> standortComboBox;
    @FXML
    private Button saveButton;

    private Gruppe gruppe; // Bestehender Datensatz beim Bearbeiten, null beim Hinzufügen

    // ObservableList und Map für Standorte
    private final ObservableList<String> standortList = FXCollections.observableArrayList();
    private final java.util.Map<String, Integer> standortMap = new java.util.HashMap<>();

    @FXML
    public void initialize() {
        loadStandorte();
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
                int id = rs.getInt("standort_id");
                String name = rs.getString("standort_name");
                standortList.add(name);
                standortMap.put(name, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        standortComboBox.setItems(standortList);
    }

    /**
     * Setzt den Gruppen-Datensatz zum Bearbeiten. Bei null werden Felder geleert (Add-Modus).
     */
    public void setGruppe(Gruppe gruppe) {
        this.gruppe = gruppe;
        if (gruppe != null) {
            gruppenNameField.setText(gruppe.getGruppeName());
            // Falls der Standortname vorhanden ist, vorauswählen
            if (gruppe.getStandortName() != null) {
                standortComboBox.setValue(gruppe.getStandortName());
            }
        } else {
            gruppenNameField.clear();
            standortComboBox.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleSave() {
        String gruppenName = gruppenNameField.getText().trim();
        String standortName = standortComboBox.getValue();

        if (gruppenName.isEmpty() || standortName == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte alle Felder ausfüllen!");
            alert.showAndWait();
            return;
        }

        int standortId = standortMap.get(standortName);

        String sql;
        if (gruppe == null) {
            sql = "INSERT INTO gruppe (gruppe_name, standort_id) VALUES (?, ?)";
        } else {
            sql = "UPDATE gruppe SET gruppe_name=?, standort_id=? WHERE gruppe_id=?";
        }

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, gruppenName);
            ps.setInt(2, standortId);
            if (gruppe != null) {
                ps.setInt(3, gruppe.getGruppeId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kein Datensatz wurde gespeichert!");
                alert.showAndWait();
                return;
            }

            if (gruppe == null) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    gruppe = new Gruppe(newId, gruppenName, standortId, standortName);
                    System.out.println("Neue Gruppe mit ID: " + newId + " wurde hinzugefügt.");
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