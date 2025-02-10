package org.example.finalexam.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.finalexam.dao.UserDAO;
import org.example.finalexam.dao.UserInfoDAO;
import org.example.finalexam.daoImplement.UserController;
import org.example.finalexam.daoImplement.UserInfoController;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.FXMLSupport;
import org.example.finalexam.utils.GenerateInput;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.finalexam.utils.FXMLSupport.loadWithPersistentUser;
import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class SignUp_Controller implements Initializable {
    @FXML
    private TextField tf_fullname;
    @FXML
    private TextField tf_email;
    @FXML
    private Button bt_signup;
    @FXML
    private Button bt_log_in;
    @FXML
    private Button bt_return;

    private final UserDAO userDAO = new UserController();
    private final UserInfoDAO userInfoDAO = new UserInfoController();
    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bt_log_in.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/LoginPage.fxml", "Login Page")
        );
        bt_return.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/Welcome_Page.fxml", "Booking Account Page")
        );
        bt_signup.setOnAction(this::handleSignUp);
    }

    /**
     * Handles the sign-up process when the sign-up button is clicked.
     *
     * <p>The method retrieves the username, password, full name, birth date, email, phone number, and role from the respective input fields.
     * It checks if any of these fields are empty and displays an error alert if they are.
     * If the fields are not empty, it validates the input and proceeds to create a new user based on the provided role.
     * The method creates a user (Owner, Tenant, or Host) with the provided details and saves it to the database.
     * It then changes the scene to the respective home page based on the user's role.
     * If an invalid role is selected or a database error occurs, it displays an error alert.</p>
     *
     * @param event the event triggered by the sign-up button click

     * <p>Error alerts are displayed in cases where input fields are empty, input validation fails,
     * an invalid role is selected, or a database error occurs.</p>
     */
    private void handleSignUp(ActionEvent event) {

        String fullName = tf_fullname.getText();
        String email = tf_email.getText();

        if (fullName.isEmpty() || email.isEmpty() || selectedFile == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields and upload your profile image");
            return;
        }
//        if (!validateSignUp(fullName, email)) {
//            return;
//        }

        try {
            User user = new User.Builder()
                    .setName(fullName)
                    .setContactInfo(email)
                    .setBookingHistory("None")
                    .build();
            userDAO.addUser(user);
            saveImage();
            selectedFile = null;
            User userSession = getUserDetails(fullName, email);
            loadWithPersistentUser(event, userSession, "/org/example/finalexam/UserUI/BookMovieTicket.fxml", "Movie Ticket Reservation Page");

            showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "You have successfully signed up as the User!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    @FXML
    void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );
        selectedFile = fileChooser.showOpenDialog(new Stage());
//        if (selectedFile != null) {
//            System.out.println("Uploaded file: " + selectedFile.getAbsolutePath());
//        }
    }

    private void saveImage() {
        if (selectedFile == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName(GenerateInput.getSimplifyFullName(tf_fullname.getText()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );
        File fileToSave = fileChooser.showSaveDialog(new Stage());
        if (fileToSave != null) {
            try {
                Files.copy(selectedFile.toPath(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                //System.out.println("File saved to: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Save Image Error", "An error occurred while saving the image.");
            }
        }
    }

    private User getUserDetails(String fullname, String email) {
        try {
            return userInfoDAO.getUserByFullNameAndEmail(fullname, email);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occured while retrieving user details.");
            return null;
        }

    }
}
