package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Dokumentation;
import huehnerstall.kitaproject.model.Kind;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class DokumentationEditController {

    @FXML
    private DatePicker dokuDatePicker;
    @FXML
    private ComboBox<String> anwesenheitCombo;
    @FXML
    private TextArea bemerkungField;

    private Kind kind;  // Das Kind, dem diese Dokumentation zugeordnet ist
    private Dokumentation documentation; // Falls bereits ein Eintrag existiert (Bearbeitung), sonst null

    @FXML
    public void initialize() {
        anwesenheitCombo.setItems(FXCollections.observableArrayList("Anwesend", "Entschuldigt", "Unentschuldigt"));
    }

    /**
     * Wird aufgerufen, wenn ein neuer Dokumentationseintrag hinzugefügt wird.
     * Setzt das aktuelle Kind und Standardwerte.
     */
    public void setKind(Kind kind) {
        this.kind = kind;
        // Setze Standardwerte für ein neues Dokumentationselement
        dokuDatePicker.setValue(LocalDate.now());
        anwesenheitCombo.setValue("Anwesend");
    }

    /**
     * Wird aufgerufen, wenn ein bestehender Dokumentationseintrag bearbeitet wird.
     * Füllt die Formularfelder mit den vorhandenen Werten.
     */
    public void setDocumentation(Dokumentation documentation) {
        this.documentation = documentation;
        dokuDatePicker.setValue(documentation.getDokuDatum());
        anwesenheitCombo.setValue(documentation.getAnwesenheit());
        bemerkungField.setText(documentation.getBemerkung());
    }

    /**
     * Speichert den Dokumentationseintrag in der Datenbank.
     * Führt einen INSERT durch, wenn documentation == null, ansonsten ein UPDATE.
     */
    @FXML
    private void handleSave() {
        LocalDate date = dokuDatePicker.getValue();
        String status = anwesenheitCombo.getValue();
        String bemerkung = bemerkungField.getText();

        if (date == null || status == null) {
            return;
        }

        if (documentation == null) {
            // Neuen Eintrag einfügen (INSERT)
            String sql = "INSERT INTO dokumentation (kind_id, doku_datum, anwesenheit, bemerkung) VALUES (?, ?, ?, ?)";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, kind.getKindId());
                ps.setDate(2, java.sql.Date.valueOf(date));
                ps.setString(3, status);
                ps.setString(4, bemerkung);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Das Einfügen des Dokumentationseintrags hat keinen Datensatz erstellt.");
                }
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        documentation = new Dokumentation(newId, kind.getKindId(), date, status, bemerkung);
                    } else {
                        throw new SQLException("Kein generierter Schlüssel erhalten.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Bestehenden Eintrag aktualisieren (UPDATE)
            String sql = "UPDATE dokumentation SET doku_datum = ?, anwesenheit = ?, bemerkung = ? WHERE doku_id = ?";
            try (Connection conn = JDBC.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, java.sql.Date.valueOf(date));
                ps.setString(2, status);
                ps.setString(3, bemerkung);
                ps.setInt(4, documentation.getDokuId());
                ps.executeUpdate();
                // Aktualisiere das Objekt
                documentation.setDokuDatum(date);
                documentation.setAnwesenheit(status);
                documentation.setBemerkung(bemerkung);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Schließe das Popup-Fenster
        Stage stage = (Stage) dokuDatePicker.getScene().getWindow();
        stage.close();
    }

    /**
     * Schließt das Popup ohne Änderungen vorzunehmen.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) dokuDatePicker.getScene().getWindow();
        stage.close();
    }
}