package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Standort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class StandortController {

    @FXML
    private TableView<Standort> standortTable;

    private final ObservableList<Standort> standortList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadStandorte();
    }

    private void loadStandorte() {
        standortList.clear();
        String sql = "SELECT standort_id, standort_name, strasse, hausnummer, plz, ort FROM standort";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("standort_id");
                String name = rs.getString("standort_name");
                String strasse = rs.getString("strasse");
                String hausnummer = rs.getString("hausnummer");
                String plz = rs.getString("plz");
                String ort = rs.getString("ort");

                Standort s = new Standort(id, name, strasse, hausnummer, plz, ort);
                standortList.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        standortTable.setItems(standortList);
    }

    @FXML
    private void handleAdd() {
        openStandortEditPopup(null);
    }

    @FXML
    private void handleEdit() {
        Standort selected = standortTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Standort ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Standort zum Bearbeiten aus.");
            alert.showAndWait();
            return;
        }
        openStandortEditPopup(selected);
    }

    @FXML
    private void handleDelete() {
        Standort selected = standortTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Standort ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Standort zum Löschen aus.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Standort löschen");
        confirm.setHeaderText("Möchten Sie den ausgewählten Standort wirklich löschen?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM standort WHERE standort_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selected.getStandortId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadStandorte();
        }
    }

    private void openStandortEditPopup(Standort standort) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/standortEdit.fxml"));
            Parent root = loader.load();
            StandortEditController editController = loader.getController();
            editController.setStandort(standort);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(standort == null ? "Standort hinzufügen" : "Standort bearbeiten");
            stage.setScene(new Scene(root, 400, 300));
            stage.setResizable(false);
            stage.showAndWait();
            loadStandorte();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}