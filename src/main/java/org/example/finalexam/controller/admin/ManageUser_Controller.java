package org.example.finalexam.controller.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.controller.pop_up.ViewBookingList_Controller;
import org.example.finalexam.dao.UserDAO;
import org.example.finalexam.daoImplement.UserController;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.FXMLSupport;
import org.example.finalexam.utils.GenerateInput;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ManageUser_Controller implements Initializable {

    private List<Booking> bookingsList = new ArrayList<>();
    private final UserDAO userDAO = new UserController();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private Button bt_return;
    @FXML
    private Button bt_reset_field;
    @FXML
    private TableColumn<User, String> column_booking_history;
    @FXML
    private TableColumn<User, String> column_fullname;
    @FXML
    private TableColumn<User, Integer> column_id;
    @FXML
    private TableColumn<User, String> colunm_contact_info;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField tf_booking;
    @FXML
    private TextField tf_contact_info;
    @FXML
    private TextField tf_fullname;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_search_name;
    @FXML
    private TextField tf_search_contact_info;
    @FXML
    public ImageView iv_image;

    private void setUPButton() {
        bt_return.setOnAction(e -> {
            FXMLSupport.changeScene(e, "/org/example/finalexam/AdminUI/AdminHomePage.fxml", "Admin Homepage");
        });
        bt_reset_field.setOnAction(e -> {
            tf_id.setText("");
            tf_fullname.setText("");
            tf_booking.setText("");
            tf_contact_info.setText("");
            bookingsList.clear();
            iv_image.setImage(new Image("/org/example/finalexam/Image/admin.jpg"));
        });
    }

    private void setupTextFieldFilter() {
        tf_search_name.textProperty().addListener((observable, oldValue, newValue) -> filterUser());
        tf_search_contact_info.textProperty().addListener((observable, oldValue, newValue) -> filterUser());
    }

    @FXML
    void addUser(ActionEvent event) {
        if (tf_id.getText().isEmpty()) {
            progressBar.setVisible(true); // Show the progress bar
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_id.getText().isEmpty() || tf_fullname.getText().isEmpty() || tf_contact_info.getText().isEmpty() || tf_booking.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all text fields except an id text field.");
                progressBar.setVisible(false);
                return;
            }
//            if(!validateSignUp(tf_username.getText(), null, tf_fullname.getText(), tf_email.getText(), tf_phone.getText())) {
//                progressBar.setVisible(false);
//                return;
//            }

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    try {

                        String name = tf_fullname.getText();
                        String contact_info = tf_contact_info.getText();
                        String booking = tf_booking.getText();
                        User user = new User.Builder()
                                .setName(name)
                                .setContactInfo(contact_info)
                                .setBookingHistory(booking)
                                .build();

                        Thread.sleep(500);
                        userDAO.addUser(user);

                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress
                    refreshTableView();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress
                    showAlert(Alert.AlertType.ERROR, "Add new User fail", "An error occurred while adding the user.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Add new User fail", "The id text field must be empty.");
        }
    }

    @FXML
    void updateUser(ActionEvent event) {
        if (!tf_id.getText().isEmpty()) {
            // Show progress bar
            progressBar.setVisible(true);
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_fullname.getText().isEmpty() || tf_contact_info.getText().isEmpty() || tf_booking.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all text fields except an id text field.");
                progressBar.setVisible(false);
                return;
            }
//            if (!validateSignUp(tf_username.getText(), null, tf_fullname.getText(), tf_email.getText(), tf_phone.getText())) {
//                progressBar.setVisible(false);
//                return;
//            }

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    int id = Integer.parseInt(tf_id.getText());
                    String name = tf_fullname.getText();
                    String contact_info = tf_contact_info.getText();
                    String booking = tf_booking.getText();

                    User currentUser = userDAO.getUserById(id);
                    if (currentUser == null) {
                        showAlert(Alert.AlertType.ERROR, "Update Error", "User not found with ID: " + id);
                        return null;
                    }

                    User user = new User.Builder()
                            .setID(id)
                            .setName(name)
                            .setContactInfo(contact_info)
                            .setBookingHistory(booking)
                            .build();

                    Thread.sleep(500);
                    userDAO.updateUser(user);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false); // Hide the progress bar
                    refreshTableView(); // Refresh the table view after update
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the user.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an user to update.");
        }
    }

    @FXML
    void handleViewBookings(ActionEvent event) {
       try {
            if (!tf_id.getText().isEmpty()) {
                if (!bookingsList.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/finalexam/Pop_Up_Window/Booking.fxml"));
                    Parent root = loader.load();
                    ViewBookingList_Controller controller = loader.getController();
                    controller.setupTableColumns();

                    controller.setBookings(bookingsList);

                    Stage stage = new Stage();
                    stage.setTitle("Booking List");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Booking List Announce", "There is no booking for this user.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an user to view it booking(s).");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_fullname.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_booking_history.setCellValueFactory(new PropertyValueFactory<>("booking_history"));
        colunm_contact_info.setCellValueFactory(new PropertyValueFactory<>("contact_info"));
    }

    private void refreshTableView() {
        userList.clear();
        try {
            List<User> users = userDAO.getAllUsers();
            userList.addAll(users);
            // Creating a Comparator for sorting by id
            Comparator<User> compareByID = Comparator.comparingInt(User::getId);
            // Sorting the ObservableList using the Comparator
            FXCollections.sort(userList, compareByID);

            Platform.runLater(() -> {
                tableView.setItems(userList);
                tableView.refresh();
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    /**
     * Adds a listener to automatically refresh the TableView when changes occur.
     */
    private void refreshTableViewListener() {
        tableView.getItems().addListener((javafx.collections.ListChangeListener<User>) _ -> tableView.refresh());
    }

    private void loadData() {
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        Task<ObservableList<User>> task = new Task<>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                // Simulate a delay for loading data
                Thread.sleep(500); // Simulate delay for demonstration purposes

                // Fetch data from the database
                List<User> users = userDAO.getAllUsers();
                userList.addAll(users);
                // Creating a Comparator for sorting by id
                Comparator<User> compareByID = Comparator.comparingInt(User::getId);
                // Sorting the ObservableList using the Comparator
                FXCollections.sort(userList, compareByID);
                return userList;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                // Update the TableView with the loaded data
                tableView.setItems(getValue());
                progressBar.setVisible(false); // Hide the progress bar
            }
            @Override
            protected void failed() {
                super.failed();
                Throwable e = getException();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database: " + e.getMessage());
                progressBar.setVisible(false); // Hide the progress bar
            }
        };

        // Update the progress bar during the task
        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start(); // Start the task in a new thread
    }

    private void setupTableViewListeners() {
        tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                // Set text on the text field
                tf_id.setText(String.valueOf(newSelection.getId()));
                tf_fullname.setText(newSelection.getName());
                tf_booking.setText(newSelection.getBooking_history());
                tf_contact_info.setText(newSelection.getContact_info());
                FXMLSupport.setImage(iv_image,"/org/example/finalexam/User_Image/" + GenerateInput.getSimplifyFullName(tf_fullname.getText()) + ".jpg");


                // Get all attribute list of the specific selected user for a suitable pop-up window
                bookingsList.clear();

                // Check if the bookings list is not null before adding
                if (newSelection.getBookings() != null) {
                    bookingsList.addAll(newSelection.getBookings());
                } else {
                    System.out.println("The booking list of the select user is null");
                }
            }
        });
    }

    private void filterUser() {
        String name = tf_search_name.getText().toLowerCase();
        String contact_info = tf_search_contact_info.getText().toLowerCase();

        List<User> filteredUsers = userList.stream()
                .filter(property -> property.getName().toLowerCase().contains(name))
                .filter(property -> property.getContact_info().toLowerCase().contains(contact_info))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredUsers));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUPButton();
        setupTableColumns();
        loadData();
        setupTableViewListeners();
        refreshTableViewListener();
        setupTextFieldFilter();

        FXMLSupport.setImage(iv_image, "/org/example/finalexam/Image/admin.jpg");
    }
}
