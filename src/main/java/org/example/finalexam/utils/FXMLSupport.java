package org.example.finalexam.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class FXMLSupport {

    /**
     * Shows an alert message.
     *
     * @param alertType the type of alert
     * @param title the title of the alert
     * @param content the content of the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Changes the current scene of the application to a new scene defined by the specified FXML file.
     *
     * @param event   The ActionEvent that triggered the scene change, used to retrieve the current stage.
     * @param fxmlFile The path to the FXML file that defines the new scene.
     * @param title   The title to set for the new stage.
     * @throws RuntimeException if an IOException occurs while loading the FXML file.
     */
    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(FXMLSupport.class.getResource(fxmlFile)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title); // set title for screen
        stage.setResizable(false); // Unable resize
        if (root != null) {
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
