package org.example.finalexam.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class WelcomePage_Controller implements Initializable {

    @FXML
    private Button bt_exits;
    @FXML
    private ImageView iv_image1;
    @FXML
    private ImageView iv_image2;
    @FXML
    private ImageView iv_image3;
    @FXML
    private ImageView iv_image4;
    @FXML
    private ImageView iv_image5;
    @FXML
    private ImageView iv_image6;
    @FXML
    private ImageView iv_image7;
    @FXML
    private Button bt_login;
    @FXML
    private Button bt_signup;

    private void setupImage() {
        iv_image1.setImage(new Image("/org/example/finalexam/Image/welcomepage4.png"));
        iv_image2.setImage(new Image("/org/example/finalexam/Image/welcomepage2.png"));
        iv_image3.setImage(new Image("/org/example/finalexam/Image/welcomepage6.png"));
        iv_image4.setImage((new Image("/org/example/finalexam/Image/movieticket.png")));
        iv_image5.setImage((new Image("/org/example/finalexam/Image/welcomepage3.png")));
        iv_image6.setImage((new Image("/org/example/finalexam/Image/welcomepage1.png")));
        iv_image7.setImage(new Image("/org/example/finalexam/Image/welcomepage5.png"));
        iv_image4.setRotate(350);
    }
    /**
     * Initializes the landing page controller.
     * Sets up button actions for navigation and application closure.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not available.
     * @param resourceBundle The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupImage();
        bt_login.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/LoginPage.fxml", "Login Page");
        });
        bt_signup.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/SignupPage.fxml", "Signup Page");
        });
        // Close the app when user click on "Exit Application button"
        bt_exits.setOnAction(event ->{
            // Get the stage and close the application
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close(); // Close the window
        });

    }
}
