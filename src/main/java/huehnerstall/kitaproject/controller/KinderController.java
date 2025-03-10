package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Kind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class KinderController {

    @FXML
    private TableView<Kind> kinderTable;

    private ObservableList<Kind> kinderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadKinder();
    }

    private void loadKinder() {
        // Erwartet, dass vw_kind_info die Spalten kind_id, vorname, nachname, geburtstag, gruppenname und standort liefert.
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
}