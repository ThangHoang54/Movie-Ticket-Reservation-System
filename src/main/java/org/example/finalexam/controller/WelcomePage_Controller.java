package org.example.finalexam.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class WelcomePage_Controller implements Initializable {
    @FXML
    private Button bt_user;
    @FXML
    private Button bt_screen;
    @FXML
    private Button bt_booking;
    @FXML
    private Button bt_theater;
    @FXML
    private Button bt_exits;

    /**
     * Initializes the landing page controller.
     * Sets up button actions for navigation and application closure.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if not available.
     * @param resourceBundle The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bt_user.setOnAction(event ->
            FXMLSupport.changeScene(event, "/org/example/finalexam/ManageUser.fxml", "Manage User")
        );
        bt_booking.setOnAction(event ->
            FXMLSupport.changeScene(event, "/org/example/finalexam/ManageBooking.fxml", "Manage Booking")
        );
        bt_screen.setOnAction(event ->
            FXMLSupport.changeScene(event, "/org/example/finalexam/ManageScreen.fxml", "Manage Screen")
        );
        bt_theater.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/ManageTheater.fxml", "Manage Theater")
        );
        // Close the app when user click on "Exit Application button"
        bt_exits.setOnAction(event ->{
            // Get the stage and close the application
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close(); // Close the window
        });
    }
}
