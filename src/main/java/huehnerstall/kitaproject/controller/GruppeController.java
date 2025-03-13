package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Gruppe;
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
import java.sql.*;
import java.util.Optional;

public class GruppeController {

    @FXML
    private TableView<Gruppe> gruppeTable;

    private final ObservableList<Gruppe> gruppeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadGruppen();
    }

    private void loadGruppen() {
        gruppeList.clear();
        // Wir nutzen die View vw_gruppen_standorte, die folgende Spalten liefert: gruppe_id, gruppenname, standortname.
        String sql = "SELECT gruppe_id, gruppenname, standortname FROM vw_gruppen_standorte";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("gruppe_id");
                String gruppenname = rs.getString("gruppenname");
                String standortname = rs.getString("standortname");
                Gruppe gruppe = new Gruppe(id, gruppenname, 0, standortname);
                gruppeList.add(gruppe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gruppeTable.setItems(gruppeList);
    }

    @FXML
    private void handleAdd() {
        openGruppeEditPopup(null);
    }

    @FXML
    private void handleEdit() {
        Gruppe selected = gruppeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Keine Gruppe ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie eine Gruppe zum Bearbeiten aus.");
            alert.showAndWait();
            return;
        }
        openGruppeEditPopup(selected);
    }

    @FXML
    private void handleDelete() {
        Gruppe selected = gruppeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Keine Gruppe ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie eine Gruppe zum Löschen aus.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Gruppe löschen");
        confirm.setHeaderText("Möchten Sie die ausgewählte Gruppe wirklich löschen?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM gruppe WHERE gruppe_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selected.getGruppeId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadGruppen();
        }
    }

    private void openGruppeEditPopup(Gruppe gruppe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/gruppeEdit.fxml"));
            Parent root = loader.load();
            GruppeEditController editController = loader.getController();
            editController.setGruppe(gruppe);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(gruppe == null ? "Gruppe hinzufügen" : "Gruppe bearbeiten");
            stage.setScene(new Scene(root, 400, 300));
            stage.setResizable(false);
            stage.showAndWait();
            loadGruppen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}