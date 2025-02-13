package org.example.finalexam.utils;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class InputValidation {

    /**
     * Validates a full name.
     * The name must not be null, should be trimmed of leading and trailing whitespaces,
     * have a length greater than 0, and contain only alphabetic characters and spaces.
     *
     * @param name the full name to validate
     * @return true if the full name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                name.matches("[a-zA-Z\\s]+");
    }

    /**
     * Validates a movie name.
     * This method checks if the given movie name string is not null, not empty, within a valid length range,
     * and contains only valid characters (letters, numbers, spaces).
     *
     * @param name the movie name to validate
     * @return true if the full name is valid, false otherwise
     */
    public static boolean isValidMovieName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                name.matches("[a-zA-Z0-9\\s]+");
    }

    /**
     * Validates an email address.
     * The email must contain only alphanumeric characters before the '@' symbol.
     * The domain part must contain at least one dot (.) and at most two dots (.).
     * Only '@' and '.' are allowed as special characters.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9]+@[A-Za-z0-9]+(\\.[A-Za-z0-9]+){1,2}$";
        return email != null && Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * Validates an address string.
     * This method checks if the given address string is not null, not empty, within a valid length range,
     * and contains only valid characters (letters, numbers, spaces, commas, periods, slashes, and hyphens).
     *
     * @param address the address string to be validated
     * @return true if the address is valid, false otherwise
     */
    public static boolean isValidAddress(String address) {
        // Check if the address is null or empty
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        // Check if the address length is within a valid range (e.g., 5 to 100 characters)
        if (address.length() < 5 || address.length() > 100) {
            return false;
        }
        // Check if the address contains only valid characters (e.g., letters, numbers, spaces, commas, periods, slashes, and hyphens)
        String regex = "^[a-zA-Z0-9\\s,./-]+$";
        return address.matches(regex);
    }

    /**
     * Validates the sign-up information.
     * This method checks the validity of full name and email.
     * If any of the inputs are invalid, it constructs detailed error messages and shows an alert.
     *
     * @param fullname the full name to validate
     * @param email the email address to validate
     * @return true if all inputs are valid, false otherwise
     */
    public static boolean validateSignUp(String fullname, String email) {
        StringBuilder errorMessage = new StringBuilder();

        if ( isValidEmail(email) && isValidName(fullname)) {
            return true;
        } else {
            if (!isValidName(fullname)) {
                errorMessage.append("Full name is not valid! It should contain only alphabetic characters and spaces.\n\n");
            }

            if (!isValidEmail(email)) {
                errorMessage.append("Email is not valid! Ensure it follows the correct format like example@domain.com " +
                        "and the domain part contains at least one dot.\n\n"
                );
            }

            if (!errorMessage.toString().isEmpty()) {
                FXMLSupport.showAlert(Alert.AlertType.ERROR, "Validate Fail", errorMessage + " Please try again and good luck");
                return false;
            }
            return true;
        }
    }

    /**
     * Validates the user's name and contact information.
     *
     * @param name the full name of the user to validate.
     * @param contact_info the contact information (email) of the user to validate.
     * @return true if both the name and contact information are valid, false otherwise.
     * <p>
     * The method checks if both the email and name are valid. If not, it accumulates error messages
     * indicating the specific validation failures. If there are validation errors, an alert
     * with the error messages is displayed, and the method returns false. If there are no errors,
     * the method returns true.
     *
     */
    public static boolean validateManageUser(String name, String contact_info) {
        StringBuilder errorMessage = new StringBuilder();

        if ( isValidEmail(contact_info) && isValidName(name)) {
            return true;
        } else {
            if (!isValidName(name)) {
                errorMessage.append("Full name is not valid! It should contain only alphabetic characters and spaces.\n\n");
            }

            if (!isValidEmail(contact_info)) {
                errorMessage.append("Email is not valid! Ensure it follows the correct format like example@domain.com " +
                        "and the domain part contains at least one dot.\n\n"
                );
            }

            if (!errorMessage.toString().isEmpty()) {
                FXMLSupport.showAlert(Alert.AlertType.ERROR, "Validate Fail", errorMessage + " Please try again and good luck");
                return false;
            }
            return true;
        }
    }

    /**
     * Validates the theater's name and address.
     *
     * @param name the full name of the theater to validate.
     * @param address the address of the theater to validate.
     * @return true if both the name and address are valid, false otherwise.
     * <p>
     * The method checks if both the address and name are valid. If not, it accumulates error messages
     * indicating the specific validation failures. If there are validation errors, an alert
     * with the error messages is displayed, and the method returns false. If there are no errors,
     * the method returns true.
     */
    public static boolean validateManageTheater(String name, String address) {
        StringBuilder errorMessage = new StringBuilder();

        if ( isValidAddress(address) && isValidName(name)) {
            return true;
        } else {
            if (!isValidName(name)) {
                errorMessage.append("Full name is not valid! It should contain only alphabetic characters and spaces.\n\n");
            }

            if (!isValidAddress(address)) {
                errorMessage.append("Invalid address format. Address only allow (letters, numbers, spaces, commas, periods, slashes, and hyphens)\n");
            }

            if (!errorMessage.toString().isEmpty()) {
                FXMLSupport.showAlert(Alert.AlertType.ERROR, "Validate Fail", errorMessage + " Please try again and good luck");
                return false;
            }
            return true;
        }
    }

    /**
     * Validates the movie name.
     *
     * @param movie_name the name of the movie to validate.
     * @return true if the movie name is valid, false otherwise.
     * <p>
     * The method checks if the movie name is valid. If not, it accumulates error messages
     * indicating the specific validation failure. If there are validation errors, an alert
     * with the error messages is displayed, and the method returns false. If there are no errors,
     * the method returns true.
     */
    public static boolean validateManageScreen(String movie_name) {
        StringBuilder errorMessage = new StringBuilder();

        if ( isValidMovieName(movie_name)) {
            return true;
        } else {
            if (!isValidMovieName(movie_name)) {
                errorMessage.append("Movie name is not valid! It should contain only alphabetic characters, number and spaces.\n\n");
            }

            if (!errorMessage.toString().isEmpty()) {
                FXMLSupport.showAlert(Alert.AlertType.ERROR, "Validate Fail", errorMessage + " Please try again and good luck");
                return false;
            }
            return true;
        }
    }

}
