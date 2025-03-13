package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Vormund;
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

public class VormundController {

    @FXML
    private TableView<Vormund> vormundTable;
    @FXML
    private TableColumn<Vormund, Integer> idColumn;
    @FXML
    private TableColumn<Vormund, String> vornameColumn;
    @FXML
    private TableColumn<Vormund, String> nachnameColumn;
    @FXML
    private TableColumn<Vormund, String> rolleColumn;
    @FXML
    private TableColumn<Vormund, String> telefonColumn;
    @FXML
    private TableColumn<Vormund, String> emailColumn;
    @FXML
    private TableColumn<Vormund, String> strasseColumn;
    @FXML
    private TableColumn<Vormund, String> hausnummerColumn;
    @FXML
    private TableColumn<Vormund, String> plzColumn;
    @FXML
    private TableColumn<Vormund, String> ortColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final ObservableList<Vormund> vormundList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadVormunde();
    }

    /**
     * Lädt alle Vormund-Einträge aus der Datenbank.
     */
    private void loadVormunde() {
        vormundList.clear();
        String sql = "SELECT vormund_id, vorname, nachname, rolle, telefon, email, strasse, hausnummer, plz, ort FROM vormund";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("vormund_id");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                String rolle = rs.getString("rolle");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                String strasse = rs.getString("strasse");
                String hausnummer = rs.getString("hausnummer");
                String plz = rs.getString("plz");
                String ort = rs.getString("ort");

                Vormund v = new Vormund(id, vorname, nachname, rolle, telefon, email, strasse, hausnummer, plz, ort);
                vormundList.add(v);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        vormundTable.setItems(vormundList);
    }

    @FXML
    private void handleAdd() {
        openVormundEditPopup(null);
    }

    @FXML
    private void handleEdit() {
        Vormund selected = vormundTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Vormund zum Bearbeiten aus.");
            alert.showAndWait();
            return;
        }
        openVormundEditPopup(selected);
    }

    @FXML
    private void handleDelete() {
        Vormund selected = vormundTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Vormund zum Löschen aus.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Vormund löschen");
        confirm.setHeaderText(null);
        confirm.setContentText("Möchten Sie den ausgewählten Vormund wirklich löschen?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM vormund WHERE vormund_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selected.getVormundId());
                ps.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
            }
            loadVormunde();
        }
    }

    /**
     * Öffnet das Edit-Popup für Vormunde.
     * @param vormund Bestehender Vormund für den Bearbeitungsmodus, oder null für Hinzufügen.
     */
    private void openVormundEditPopup(Vormund vormund) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/vormundEdit.fxml"));
            Parent root = loader.load();
            // Der Edit-Controller muss die Methode setVormund(Vormund) implementieren.
            VormundEditController editController = loader.getController();
            editController.setVormund(vormund);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(vormund == null ? "Vormund hinzufügen" : "Vormund bearbeiten");
            stage.setScene(new Scene(root, 400, 500)); // Größe anpassen
            stage.setResizable(false);
            stage.showAndWait();
            loadVormunde();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}