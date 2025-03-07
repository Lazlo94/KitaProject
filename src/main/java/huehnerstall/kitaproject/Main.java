package huehnerstall.kitaproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

import java.io.IOException;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Kita");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try (Connection conn = JDBC.getConnection()) {
            if (conn.isValid(0)) {
                System.out.println("Datenbankverbindung erfolgreich hergestellt!");
            }

            // SQL Queries hierhin!

        } catch (SQLException e) {
            e.printStackTrace();
        }

        launch();
    }
}