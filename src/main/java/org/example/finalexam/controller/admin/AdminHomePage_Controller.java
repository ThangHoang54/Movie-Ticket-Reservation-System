package org.example.finalexam.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminHomePage_Controller implements Initializable {
    @FXML
    private Button bt_user;
    @FXML
    private Button bt_screen;
    @FXML
    private Button bt_booking;
    @FXML
    private Button bt_theater;
    @FXML
    private Button bt_logout;
    @FXML
    private ImageView iv_image1;

    /**
     * Initializes the admin homepage controller.
     * Sets up button actions for navigation and application closure.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not available.
     * @param resourceBundle The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iv_image1.setImage(new Image("/org/example/finalexam/Image/admin.jpg"));
        bt_user.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/AdminUI/ManageUser.fxml", "Manage User")
        );
        bt_booking.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/AdminUI/ManageBooking.fxml", "Manage Booking")
        );
        bt_screen.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/AdminUI/ManageScreen.fxml", "Manage Screen")
        );
        bt_theater.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/AdminUI/ManageTheater.fxml", "Manage Theater")
        );
        bt_logout.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/Welcome_Page.fxml", "Welcome Page");
        });
    }
}
