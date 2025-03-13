package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Mitgliedsbeitrag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MitgliedsbeitragController {

    @FXML
    TableView<Mitgliedsbeitrag> mitgliedsbeitragTable;

    private final ObservableList<Mitgliedsbeitrag> mitgliedsbeitragList = FXCollections.observableArrayList();

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
}
