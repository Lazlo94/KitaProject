package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class MitarbeiterController {

    @FXML
    private TableView<Mitarbeiter> mitarbeiterTable;

    private final ObservableList<Mitarbeiter> mitarbeiterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadMitarbeiter();
    }

    /**
     * Lädt alle Mitarbeiter aus der Datenbank anhand der View vw_mitarbeiter.
     */
    private void loadMitarbeiter() {
        mitarbeiterList.clear();
        String sql = "SELECT mitarbeiter_id, rolle_id, vorname, nachname, telefon, email, strasse, hausnummer, plz, ort, gehalt, mitarbeiter_rolle FROM vw_mitarbeiter";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("mitarbeiter_id");
                int rolleId = rs.getInt("rolle_id");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                String strasse = rs.getString("strasse");
                String hausnummer = rs.getString("hausnummer");
                String plz = rs.getString("plz");
                String ort = rs.getString("ort");
                BigDecimal gehalt = rs.getBigDecimal("gehalt");
                String beruf = rs.getString("mitarbeiter_rolle");

                Mitarbeiter mitarbeiter = new Mitarbeiter(id, rolleId, vorname, nachname, telefon, email, strasse, hausnummer, plz, ort, gehalt, beruf);
                mitarbeiterList.add(mitarbeiter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mitarbeiterTable.setItems(mitarbeiterList);
    }

    /**
     * Öffnet das Popup zum Hinzufügen eines neuen Mitarbeiters.
     */
    @FXML
    private void handleAdd() {
        openMitarbeiterEditPopup(null);
    }

    /**
     * Öffnet das Popup zum Bearbeiten des ausgewählten Mitarbeiters.
     */
    @FXML
    private void handleEdit() {
        Mitarbeiter selected = mitarbeiterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Mitarbeiter ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Mitarbeiter zum Bearbeiten aus.");
            alert.showAndWait();
            return;
        }
        openMitarbeiterEditPopup(selected);
    }

    /**
     * Löscht den ausgewählten Mitarbeiter nach Bestätigung.
     */
    @FXML
    private void handleDelete() {
        Mitarbeiter selected = mitarbeiterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Mitarbeiter ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Mitarbeiter zum Löschen aus.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Mitarbeiter löschen");
        confirm.setHeaderText("Möchten Sie den ausgewählten Mitarbeiter wirklich löschen?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM mitarbeiter WHERE mitarbeiter_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selected.getMitarbeiterId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadMitarbeiter();
        }
    }

    /**
     * Öffnet das Mitarbeiter-Edit-Popup. Wird im Hinzufügen-Modus (Parameter null) oder Bearbeiten-Modus (existierender Mitarbeiter) verwendet.
     */
    private void openMitarbeiterEditPopup(Mitarbeiter mitarbeiter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/mitarbeiterEdit.fxml"));
            Parent root = loader.load();
            MitarbeiterEditController editController = loader.getController();
            // Übergebe das bestehende Mitarbeiter-Objekt oder null, wenn neu
            editController.setMitarbeiter(mitarbeiter);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(mitarbeiter == null ? "Mitarbeiter hinzufügen" : "Mitarbeiter bearbeiten");
            stage.setScene(new Scene(root, 500, 400)); // Größe anpassen
            stage.setResizable(false);
            stage.showAndWait();
            // Nach dem Schließen des Popups aktualisieren wir die Tabelle
            loadMitarbeiter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}