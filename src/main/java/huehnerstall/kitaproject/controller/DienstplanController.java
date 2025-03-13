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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    // Map zur Zuordnung: Gruppen-ID -> Gruppenname
    private final Map<Integer, String> groupIdNameMap = new HashMap<>();

    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();
        currentMonday = today.with(DayOfWeek.MONDAY);
        updateColumnHeaders();

        // Lade die Gruppendaten (DienstplanRow-Objekte) und speichere die Zuordnung
        ObservableList<DienstplanRow> rowData = loadGroupRows();
        dienstplanTable.setItems(rowData);

        // Lade Mitarbeiterzuweisungen aus der Mitarbeiter-Tabelle (nur Erzieher)
        loadAssignmentsFromMitarbeiter();
    }

    /**
     * Aktualisiert die Spaltenüberschriften mit Wochentagen und Datum im Format TT.MM.JJJJ.
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
    }

    @FXML
    private void handlePrevWeek(ActionEvent event) {
        currentMonday = currentMonday.minusWeeks(1);
        updateColumnHeaders();
    }

    @FXML
    private void handleNextWeek(ActionEvent event) {
        currentMonday = currentMonday.plusWeeks(1);
        updateColumnHeaders();
    }

    /**
     * Lädt alle Gruppennamen aus der Tabelle 'gruppe' und erstellt für jede einen DienstplanRow.
     * Dabei wird auch die groupIdNameMap befüllt.
     */
    private ObservableList<DienstplanRow> loadGroupRows() {
        ObservableList<DienstplanRow> rows = FXCollections.observableArrayList();
        String sql = "SELECT gruppe_id, gruppe_name FROM gruppe ORDER BY gruppe_name";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int groupId = rs.getInt("gruppe_id");
                String groupName = rs.getString("gruppe_name");
                rows.add(new DienstplanRow(groupName));
                groupIdNameMap.put(groupId, groupName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Lädt alle Erzieher aus der 'mitarbeiter'-Tabelle (basierend auf der Rolle 'Erzieher') und füllt
     * die Zellen der Dienstplantabelle. Für jeden Mitarbeiter wird:
     * - Der Name
     * - Die Startzeit (im Format HH:mm)
     * - Die Endzeit, errechnet als Startzeit + Arbeitszeit (ebenfalls HH:mm)
     * in der Zelle der Gruppe eingetragen, zu der der Mitarbeiter gehört.
     * Nur für Montag bis Freitag werden die Zellen befüllt.
     */
    private void loadAssignmentsFromMitarbeiter() {
        String sql = "SELECT m.mitarbeiter_id, m.vorname, m.nachname, m.startzeit, m.arbeitszeit, m.gruppe " +
                "FROM mitarbeiter m " +
                "JOIN rolle r ON m.rolle_id = r.rolle_id " +
                "WHERE r.beruf = 'Erzieher'";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int gruppeId = rs.getInt("gruppe");
                if (gruppeId <= 0) continue;

                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                Time sqlStart = rs.getTime("startzeit");
                Time sqlArbeits = rs.getTime("arbeitszeit");
                if (sqlStart == null || sqlArbeits == null) continue;
                LocalTime startTime = sqlStart.toLocalTime();
                LocalTime duration = sqlArbeits.toLocalTime();
                // Berechne Endzeit: addiere Stunden und Minuten von duration zur Startzeit
                LocalTime endTime = startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
                // Formatiere Zeiten als HH:mm
                String startStr = startTime.toString().substring(0, 5);
                String endStr = endTime.toString().substring(0, 5);
                String cellText = vorname + " " + nachname + "\n" + startStr + "-" + endStr;

                // Finde den Gruppennamen anhand der Gruppen-ID aus groupIdNameMap
                String groupName = groupIdNameMap.get(gruppeId);
                if (groupName == null) continue;

                // Finde in der TableView den DienstplanRow, dessen groupName mit dem aus der DB übereinstimmt
                for (DienstplanRow row : dienstplanTable.getItems()) {
                    if (row.getGroupName().equalsIgnoreCase(groupName)) {
                        // Für Montag bis Freitag füllen wir die Zellen (Tag 1 bis Tag 5)
                        // Hier wird angenommen, dass der Mitarbeiter an jedem Arbeitstag (Mo-Fr) dieselben Zeiten hat
                        row.setDay1(cellText);
                        row.setDay2(cellText);
                        row.setDay3(cellText);
                        row.setDay4(cellText);
                        row.setDay5(cellText);
                        // Samstag und Sonntag bleiben unbefüllt bzw. können später ausgegraut werden.
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dienstplanTable.refresh();
    }
}