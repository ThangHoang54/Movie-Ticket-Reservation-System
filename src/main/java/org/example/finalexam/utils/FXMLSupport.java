package org.example.finalexam.utils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.model.User;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class FXMLSupport {
    private static final Logger LOGGER = Logger.getLogger(FXMLSupport.class.getName());

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
     * @param event The ActionEvent that triggered the scene change, used to retrieve the current stage.
     * @param fxmlFile The path to the FXML file that defines the new scene.
     * @param title The title to set for the new stage.
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

    /** * Loads an FXML file and initializes the controller with a persistent Person data.
     * @param <T> the type parameter of the controller
     * @param event the ActionEvent triggering the load
     * @param person the Person object to be passed to the controller
     * @param path the path to the FXML file
     * @param title the title for the new stage/window */
    public static <T> void loadWithPersistentUser(ActionEvent event, User person, String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLSupport.class.getResource(path));
            Parent root = loader.load();

            T controller = loader.getController();
            Method setPersonMethod = controller.getClass().getMethod("setUser", User.class);
            Platform.runLater(() -> {
                try {
                    setPersonMethod.invoke(controller, person);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not load persistent User data.");
                }
            });

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title); // set title for screen
            stage.setScene(new Scene(root));
            stage.setResizable(false); // Unable to resize
            stage.show();
        } catch (IOException | NoSuchMethodException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load persistent User data.");
        }
    }

    /**
     * Sets an image for the given ImageView from the provided FXML file path.
     *
     * @param iv_image The ImageView to set the image for.
     * @param fxmlFile The path to the FXML file containing the image.
     */
    public static void setImage(ImageView iv_image, String fxmlFile) {
        try {
            URL imageUrl = FXMLSupport.class.getResource(fxmlFile);
            if (imageUrl != null) {
                iv_image.setImage(new Image(imageUrl.toExternalForm()));
                iv_image.setStyle("-fx-alignment: center;");
                iv_image.setPreserveRatio(true);
            } else {
                LOGGER.log(Level.SEVERE, "Image file not found: " + fxmlFile);
                throw new ImageNotFoundException("Image file not found: " + fxmlFile);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting image", e);
        }
    }

    public static class ImageNotFoundException extends Exception {
        public ImageNotFoundException(String message) {
            super(message);
        }
    }


}

