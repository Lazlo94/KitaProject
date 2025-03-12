package huehnerstall.kitaproject.controller;

import huehnerstall.kitaproject.JDBC;
import huehnerstall.kitaproject.model.DienstplanRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DienstplanController {

    @FXML
    private TableView<DienstplanRow> dienstplanTable;
    @FXML
    private TableColumn<DienstplanRow, String> groupColumn;
    @FXML
    private TableColumn<?, ?> monColumn;
    @FXML
    private TableColumn<?, ?> tueColumn;
    @FXML
    private TableColumn<?, ?> wedColumn;
    @FXML
    private TableColumn<?, ?> thuColumn;
    @FXML
    private TableColumn<?, ?> friColumn;
    @FXML
    private TableColumn<?, ?> satColumn;
    @FXML
    private TableColumn<?, ?> sunColumn;
    @FXML
    private Button prevWeekButton;
    @FXML
    private Button nextWeekButton;

    private LocalDate currentMonday;

    @FXML
    public void initialize() {
        // Setze currentMonday auf den aktuellen Montag
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        currentMonday = monday;
        updateColumnHeaders();

        ObservableList<DienstplanRow> rowData = loadGroupRows();
        dienstplanTable.setItems(rowData);
    }

    /**
     * Aktualisiert die Spaltenüberschriften:
     * - Die Überschrift zeigt den Wochentag in der ersten Zeile und das Datum in der zweiten Zeile (mit Zeilenumbruch).
     * - Samstag und Sonntag werden ausgegraut.
     */
    private void updateColumnHeaders() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        monColumn.setText("Montag\n" + currentMonday.format(formatter));
        tueColumn.setText("Dienstag\n" + currentMonday.plusDays(1).format(formatter));
        wedColumn.setText("Mittwoch\n" + currentMonday.plusDays(2).format(formatter));
        thuColumn.setText("Donnerstag\n" + currentMonday.plusDays(3).format(formatter));
        friColumn.setText("Freitag\n" + currentMonday.plusDays(4).format(formatter));
        satColumn.setText("Samstag\n" + currentMonday.plusDays(5).format(formatter));
        sunColumn.setText("Sonntag\n" + currentMonday.plusDays(6).format(formatter));

        // Samstag und Sonntag ausgrauen
        // satColumn.setStyle("-fx-background-color: lightgray;");
        // sunColumn.setStyle("-fx-background-color: lightgray;");
    }

    @FXML
    private void handlePrevWeek(ActionEvent event) {
        currentMonday = currentMonday.minusWeeks(1);
        updateColumnHeaders();
        // Hier könnt ihr bei Bedarf den Dienstplan neu laden
    }

    @FXML
    private void handleNextWeek(ActionEvent event) {
        currentMonday = currentMonday.plusWeeks(1);
        updateColumnHeaders();
        // Hier könnt ihr bei Bedarf den Dienstplan neu laden
    }

    private ObservableList<DienstplanRow> loadGroupRows() {
        ObservableList<DienstplanRow> rows = FXCollections.observableArrayList();
        String sql = "SELECT gruppe_name FROM gruppe ORDER BY gruppe_name";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String groupName = rs.getString("gruppe_name");
                rows.add(new DienstplanRow(groupName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

}