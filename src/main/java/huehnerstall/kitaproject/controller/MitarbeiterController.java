package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MitarbeiterController {

    @FXML
    private TableView<Mitarbeiter> mitarbeiterTable;

    private ObservableList<Mitarbeiter> mitarbeiterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadMitarbeiter();
    }

    private void loadMitarbeiter() {
        String sql = "SELECT mitarbeiter_id, " +
                "rolle_id, " +
                "vorname, " +
                "nachname, " +
                "telefon, " +
                "email, " +
                "strasse, " +
                "hausnummer, " +
                "plz, " +
                "ort, " +
                "mitarbeiter_rolle " +
                "FROM vw_mitarbeiter";
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
                String beruf = rs.getString("mitarbeiter_rolle");

                Mitarbeiter mitarbeiter = new Mitarbeiter(id, rolleId, vorname, nachname, telefon, email, strasse, hausnummer, plz, ort, beruf);
                mitarbeiterList.add(mitarbeiter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mitarbeiterTable.setItems(mitarbeiterList);
    }
}
