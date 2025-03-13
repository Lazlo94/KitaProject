package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Dokumentation;
import huehnerstall.kitaproject.model.Kind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class DokumentationController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableView<Dokumentation> dokumentationTable;

    private ObservableList<Dokumentation> documentationList = FXCollections.observableArrayList();

    private Kind kind;

    public void setKind(Kind kind) {
        this.kind = kind;
        loadDokumentation();
    }

    // Lädt alle Dokumentationen für das übergebene Kind
    private void loadDokumentation() {
        documentationList.clear();
        String sql = "SELECT doku_id, doku_datum, anwesenheit, bemerkung FROM dokumentation " +
                "WHERE kind_id = ? ORDER BY doku_datum DESC";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kind.getKindId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int dokuId = rs.getInt("doku_id");
                LocalDate dokuDatum = rs.getDate("doku_datum").toLocalDate();
                String anwesenheit = rs.getString("anwesenheit");
                String bemerkung = rs.getString("bemerkung");
                Dokumentation doc = new Dokumentation(dokuId, kind.getKindId(), dokuDatum, anwesenheit, bemerkung);
                documentationList.add(doc);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dokumentationTable.setItems(documentationList);
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) {
            loadDokumentation();
            return;
        }
        documentationList.clear();
        String sql = "SELECT doku_id, doku_datum, anwesenheit, bemerkung FROM dokumentation " +
                "WHERE kind_id = ? AND doku_datum BETWEEN ? AND ? ORDER BY doku_datum DESC";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kind.getKindId());
            ps.setDate(2, java.sql.Date.valueOf(start));
            ps.setDate(3, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int dokuId = rs.getInt("doku_id");
                LocalDate dokuDatum = rs.getDate("doku_datum").toLocalDate();
                String anwesenheit = rs.getString("anwesenheit");
                String bemerkung = rs.getString("bemerkung");
                Dokumentation doc = new Dokumentation(dokuId, kind.getKindId(), dokuDatum, anwesenheit, bemerkung);
                documentationList.add(doc);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dokumentationTable.setItems(documentationList);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        openDokumentationEditPopup(null);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Dokumentation selectedDoc = dokumentationTable.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            System.out.println("Bitte wählen Sie einen Dokumentationseintrag zum Bearbeiten aus!");
            return;
        }
        openDokumentationEditPopup(selectedDoc);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        // Hole den ausgewählten Dokumentationseintrag
        Dokumentation selectedDoc = dokumentationTable.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            System.out.println("Bitte wählen Sie einen Eintrag zum Löschen aus!");
            return;
        }

        // Bestätigungsdialog anzeigen
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Dokumentation löschen");
        alert.setHeaderText("Möchten Sie den ausgewählten Eintrag wirklich löschen?");
        alert.setContentText("Diese Aktion kann nicht rückgängig gemacht werden.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM dokumentation WHERE doku_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, selectedDoc.getDokuId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Tabelle neu laden, um die Löschung zu reflektieren
            loadDokumentation();
        }
    }

    private void openDokumentationEditPopup(Dokumentation doc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/huehnerstall/kitaproject/dokumentationEdit.fxml"));
            Parent root = loader.load();
            DokumentationEditController editController = loader.getController();
            if (doc == null) {
                // Add-Modus: Übergibt das Kind, damit ein neuer Eintrag erstellt werden kann.
                editController.setKind(kind);
            } else {
                // Edit-Modus: Übergibt den ausgewählten Eintrag.
                editController.setDocumentation(doc);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(doc == null ? "Neue Dokumentation hinzufügen" : "Dokumentation bearbeiten");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Nach dem Schließen des Popups die Liste neu laden.
            loadDokumentation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}