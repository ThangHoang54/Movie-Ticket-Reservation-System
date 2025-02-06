package org.example.finalexam.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Hoang Minh Thang - s3999925
 */
public class PreAccountLandingPage_Controller implements Initializable {
    @FXML
    private Button bt_login;
    @FXML
    private Button bt_signup;
    @FXML
    private Button bt_return;

    private void setUpButton() {
        bt_login.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/LoginPage.fxml", "Login Page");
        });
        bt_signup.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/SignupPage.fxml", "Signup Page");
        });
        bt_return.setOnAction(event -> {
            FXMLSupport.changeScene(event, "/org/example/finalexam/Welcome_Page.fxml", "Welcome Page");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpButton();
    }
}
