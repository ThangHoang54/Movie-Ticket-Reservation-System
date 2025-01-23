package org.example.finalexam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.finalexam.utils.DatabaseConnection;

import java.io.IOException;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ReservationSystem extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Welcome_Page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Welcome Page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeDataSource();
    }

    public static void main(String[] args) {
        launch(args);
    }
}