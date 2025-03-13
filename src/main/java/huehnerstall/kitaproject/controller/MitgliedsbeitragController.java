package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.DienstplanRow;
import huehnerstall.kitaproject.model.Mitgliedsbeitrag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.transform.Scale;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class MitgliedsbeitragController {

    @FXML
    TableView<Mitgliedsbeitrag> mitgliedsbeitragTable;

    private final ObservableList<Mitgliedsbeitrag> mitgliedsbeitragList = FXCollections.observableArrayList();

    @FXML
    public Button printButton;

    @FXML
    public void initialize() {
        loadMitgliedsbeitraege();
    }

    private void loadMitgliedsbeitraege() {
        mitgliedsbeitragList.clear();
        String sql = "SELECT m.beitrag_id, m.kind_id, m.betrag, m.faelligkeitsdatum, m.zahlungsstatus, m.zahlungsart, k.vorname, k.nachname " +
                "FROM mitgliedsbeitrag m JOIN kind k ON m.kind_id = k.kind_id";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int beitragId = rs.getInt("beitrag_id");
                int kindId = rs.getInt("kind_id");
                BigDecimal betrag = rs.getBigDecimal("betrag");
                LocalDate faelligkeitsdatum = rs.getDate("faelligkeitsdatum").toLocalDate();
                String zahlungsstatus = rs.getString("zahlungsstatus");
                String zahlungsart = rs.getString("zahlungsart");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");

                Mitgliedsbeitrag mitgliedsbeitrag = new Mitgliedsbeitrag(beitragId, kindId, betrag, faelligkeitsdatum, zahlungsstatus, zahlungsart, vorname, nachname);
                mitgliedsbeitragList.add(mitgliedsbeitrag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mitgliedsbeitragTable.setItems(mitgliedsbeitragList);
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(mitgliedsbeitragTable.getScene().getWindow())) {
            // Optional: Tabelle skalieren, um sie an die Seitenbreite anzupassen
            double printableWidth = job.getJobSettings().getPageLayout().getPrintableWidth();
            double scaleX = printableWidth / mitgliedsbeitragTable.getWidth();
            Scale scale = new Scale(scaleX, scaleX);
            mitgliedsbeitragTable.getTransforms().add(scale);

            boolean success = job.printPage(mitgliedsbeitragTable);
            if (success) {
                job.endJob();
            }

            mitgliedsbeitragTable.getTransforms().remove(scale);
        }
    }

}
