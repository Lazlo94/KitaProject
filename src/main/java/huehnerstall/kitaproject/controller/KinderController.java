package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Kind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class KinderController {

    @FXML
    private TableView<Kind> kinderTable;

    private ObservableList<Kind> kinderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadKinder();
        addDokumentationButtonColumn();
    }

    private void loadKinder() {
        kinderList.clear();
        String sql = "SELECT kind_id, vorname, nachname, geburtstag, gruppenname, standort FROM vw_kind_info";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int kindId = rs.getInt("kind_id");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                LocalDate geburtstag = rs.getDate("geburtstag").toLocalDate();
                String gruppenName = rs.getString("gruppenname");
                String standort = rs.getString("standort");

                Kind kind = new Kind();
                kind.setKindId(kindId);
                kind.setVorname(vorname);
                kind.setNachname(nachname);
                kind.setGeburtstag(geburtstag);
                kind.setGruppenName(gruppenName);
                kind.setStandort(standort);

                kinderList.add(kind);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        kinderTable.setItems(kinderList);
    }

    /**
     * Öffnet das Popup zum Hinzufügen eines neuen Kindes.
     */
    @FXML
    private void handleAdd(ActionEvent event) {
        openKinderEditPopup(null);
    }

    /**
     * Öffnet das Popup zum Bearbeiten des ausgewählten Kind-Eintrags.
     */
    @FXML
    private void handleEdit(ActionEvent event) {
        Kind selectedKind = kinderTable.getSelectionModel().getSelectedItem();
        if (selectedKind == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Kind ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Kind-Eintrag zum Bearbeiten aus.");
            alert.showAndWait();
            return;
        }
        openKinderEditPopup(selectedKind);
    }

    /**
     * Löscht den ausgewählten Kind-Eintrag nach Bestätigung.
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        Kind selectedKind = kinderTable.getSelectionModel().getSelectedItem();
        if (selectedKind == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kein Kind ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie einen Kind-Eintrag zum Löschen aus.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Kind löschen");
        confirm.setHeaderText("Möchten Sie den ausgewählten Eintrag wirklich löschen?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM kind WHERE kind_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selectedKind.getKindId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadKinder();
        }
    }

    private void openKinderEditPopup(Kind kind) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/kinderEdit.fxml"));
            Parent root = loader.load();
            KinderEditController editController = loader.getController();

            editController.setKind(kind);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(kind == null ? "Kind hinzufügen" : "Kind bearbeiten");
            stage.setScene(new Scene(root, 300, 250));
            stage.setResizable(false);
            stage.showAndWait();
            loadKinder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDokumentationButtonColumn() {
        TableColumn<Kind, Void> actionCol = new TableColumn<>("Dokumentation");
        Callback<TableColumn<Kind, Void>, TableCell<Kind, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Kind, Void> call(final TableColumn<Kind, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Dokumentation");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Kind kind = getTableView().getItems().get(getIndex());
                            openDokumentationPopup(kind);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        actionCol.setCellFactory(cellFactory);
        kinderTable.getColumns().add(actionCol);
    }

    private void openDokumentationPopup(Kind kind) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/dokumentation.fxml"));
            Parent root = loader.load();
            DokumentationController docController = loader.getController();
            docController.setKind(kind);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Blockiert das Hauptfenster, bis das Popup geschlossen wird
            stage.setTitle("Dokumentation für " + kind.getVorname() + " " + kind.getNachname());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}