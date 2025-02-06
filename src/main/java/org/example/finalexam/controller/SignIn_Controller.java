package org.example.finalexam.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.finalexam.dao.UserInfoDAO;
import org.example.finalexam.daoImplement.UserInfoController;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.DatabaseConnection;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.example.finalexam.utils.FXMLSupport.loadWithPersistentUser;
import static org.example.finalexam.utils.FXMLSupport.showAlert;


/**
 * @author Hoang Minh Thang - s3999925
 */
public class SignIn_Controller implements Initializable {

    @FXML
    private TextField tf_fullname;
    @FXML
    private TextField tf_email;
    @FXML
    private Button bt_login;
    @FXML
    private Button bt_sign_up;
    @FXML
    private Button bt_return;

    private final UserInfoDAO userInfoDAO = new UserInfoController();

    /**
     * Initializes the controller.
     * Sets up button actions for navigation and login functionality.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if not available.
     * @param resourceBundle The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bt_sign_up.setOnAction(event ->
           FXMLSupport.changeScene(event, "/org/example/finalexam/SignUpPage.fxml", "Sign Up Page")
        );

        bt_login.setOnAction(this::handleLogin);
//        bt_login.setOnAction(event ->
//                FXMLSupport.changeScene(event, "/org/example/finalexam/BookMovieTicket.fxml", "Movie Ticket Reservation Page")
//        );
        bt_return.setOnAction(event ->
                FXMLSupport.changeScene(event, "/org/example/finalexam/PreBookingTicket_Page.fxml", "Booking Account Page")
        );
    }

    /**
     * Handles the login process when the login button is clicked.
     * @param event the event triggered by the login button click
     * <p>The method retrieves the username, password, and role from the respective input fields.
     * It checks if any of these fields are empty and displays an error alert if they are.
     * If the fields are not empty, it authenticates the user based on the provided username,
     * password, and role. If authentication is successful, it retrieves the user details
     * (personSession) and loads the appropriate home page based on the user's role.
     * If user details cannot be retrieved, it displays an error alert.
     * If authentication fails, it displays an error alert.
     * Error alerts are displayed in cases where input fields are empty,
     * authentication fails, or user details cannot be retrieved.</p>
     * */
    private void handleLogin(ActionEvent event) {
        String fullnameText = tf_fullname.getText();
        String emailText = tf_email.getText();

        if (fullnameText.isEmpty() || emailText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please do not leave any field empty.");
            return;
        }
        if (authenticate(fullnameText, emailText)) {
            User userSession = getUserDetails(fullnameText, emailText);
            if (userSession != null) {
                loadWithPersistentUser(event, userSession, "/org/example/finalexam/BookMovieTicket.fxml", "Movie Ticket Reservation Page");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "User details could not be retrieved.");
            }
         }
        else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Not match username and password and role. Please try again.");
        }
    }

    private User getUserDetails(String fullname, String email) {
        try {
            return userInfoDAO.getUserByFullNameAndEmail(fullname, email);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while retrieving user details.");
            return null;
        }
    }

    /**
     *  Authenticates a user based on the provided fullname and email.
     *
     *
     * <p>This method prepares a SQL query to select a user from the database
     * where the fullname, email match the provided values.
     * It attempts to execute the query and checks if there is a matching
     * record in the result set.
     * If an SQL exception occurs during this process, an error alert is
     * displayed, and the method returns false.
     * An error alert is displayed if an SQL exception occurs while connecting to the database.</p>
     *
     * @param name the fullname of the user
     * @param email the password of the user
     * @return true if the user is authenticated successfully, false otherwise.

     */
    private boolean authenticate(String name, String email) {
        String query = "SELECT * FROM User_ WHERE name = ? AND contact_info = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, email);

            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
            return false;
        }
    }
}
