package org.example.finalexam.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.finalexam.dao.BookingDAO;
import org.example.finalexam.dao.ScreenDAO;
import org.example.finalexam.dao.UserDAO;
import org.example.finalexam.daoImplement.BookingController;
import org.example.finalexam.daoImplement.ScreenController;
import org.example.finalexam.daoImplement.UserController;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ManageBooking_Controller implements Initializable {

    private final BookingDAO bookingDAO = new BookingController();
    private final UserDAO userDAO = new UserController();
    private final ScreenDAO screenDAO = new ScreenController();
    private final ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    @FXML
    private Button bt_return;
    @FXML
    private Button bt_reset_field;
    @FXML
    private TableColumn<Booking, Integer> column_id;
    @FXML
    private TableColumn<Booking, Integer> column_reserved_seat;
    @FXML
    private TableColumn<Booking, String> column_screen_movie_name;
    @FXML
    private TableColumn<Booking, String> column_username;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView<Booking> tableView;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_movie_name;
    @FXML
    private TextField tf_reserved_seat;
    @FXML
    private TextField tf_user_name;
    @FXML
    private TextField tf_search_id;
    @FXML
    private TextField tf_search_screen_movie_name;
    @FXML
    private ImageView iv_image;

    private void setUPButton() {
        bt_return.setOnAction(e -> {
            FXMLSupport.changeScene(e,"/org/example/finalexam/Welcome_Page.fxml", "Welcome Page");
        });
        bt_reset_field.setOnAction(e -> {
            tf_id.setText("");
            tf_movie_name.setText("");
            tf_reserved_seat.setText("");
            tf_user_name.setText("");
        });
    }

    private void setupTextFieldFilter() {
        tf_search_id.textProperty().addListener((observable, oldValue, newValue) -> filterBooking());
        tf_search_screen_movie_name.textProperty().addListener((observable, oldValue, newValue) -> filterBooking());
    }

    @FXML
    private void addBooking(ActionEvent event) {
        if (tf_id.getText().isEmpty()) {
            progressBar.setVisible(true); // Show the progress bar
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_reserved_seat.getText().isEmpty() || tf_movie_name.getText().isEmpty() || tf_user_name.getText().isEmpty()) {
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
                        int reserved_seat = Integer.parseInt(tf_reserved_seat.getText());
                        String movie_name = tf_movie_name.getText();
                        String user_name = tf_user_name.getText();

                        int screenID; int userID;
                        try {
                            userID = userDAO.getUserIDByName(user_name);
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "User ID not found: " + e.getMessage());
                            return null;
                        }
                        try {
                            screenID = screenDAO.getScreenIDByScreenName(movie_name);
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Screen ID not found: " + e.getMessage());
                            return null;
                        }


                        User user = new User.Builder()
                                .setID(userID)
                                .setName(user_name)
                                .build();

                        Screen screen = new Screen.Builder()
                                .setId(screenID)
                                .setMovieName(movie_name)
                                .build();

                        Booking booking = new Booking.Builder()
                                .setReserved_seat(reserved_seat)
                                .setScreen(screen)
                                .setUser(user)
                                .build();

                        Thread.sleep(500);
                        bookingDAO.addBooking(booking);

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
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Booking added successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress
                    showAlert(Alert.AlertType.ERROR, "Add new Booking fail", "An error occurred while adding the booking.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Add new Booking fail", "The id text field must be empty.");
        }
    }

    @FXML
    private void updateBooking(ActionEvent event) {
        if (!tf_id.getText().isEmpty()) {
            // Show progress bar
            progressBar.setVisible(true);
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_reserved_seat.getText().isEmpty() || tf_movie_name.getText().isEmpty() || tf_user_name.getText().isEmpty()) {
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
                    int reserved_seat = Integer.parseInt(tf_reserved_seat.getText());
                    String movie_name = tf_movie_name.getText();
                    String user_name = tf_user_name.getText();

                    Booking currentBooking = bookingDAO.getBookingById(id);
                    if (currentBooking == null) {
                        showAlert(Alert.AlertType.ERROR, "Update Error", "Booking not found with ID: " + id);
                        return null;
                    }

                    int screenID, userID;
                    try {
                        userID = userDAO.getUserIDByName(user_name);
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "User ID not found: " + e.getMessage());
                        return null;
                    }
                    try {
                        screenID = screenDAO.getScreenIDByScreenName(movie_name);
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Screen ID not found: " + e.getMessage());
                        return null;
                    }


                    User user = new User.Builder()
                            .setID(userID)
                            .setName(user_name)
                            .build();

                    Screen screen = new Screen.Builder()
                            .setId(screenID)
                            .setMovieName(movie_name)
                            .build();

                    Booking booking = new Booking.Builder()
                            .setReserved_seat(reserved_seat)
                            .setScreen(screen)
                            .setUser(user)
                            .build();

                    Thread.sleep(500);
                    bookingDAO.updateBooking(booking);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false); // Hide the progress bar
                    refreshTableView(); // Refresh the table view after update
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Booking updated successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the booking.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select the booking to update.");
        }
    }

    @FXML
    private void deleteBooking(ActionEvent event) {
        try {
            Booking booking = tableView.getSelectionModel().getSelectedItem();
            if (booking != null && !tf_id.getText().isEmpty()) {
                progressBar.setVisible(true); // Show the progress bar
                progressBar.progressProperty().unbind(); // Unbind the progress property
                progressBar.setProgress(0); // Reset progress

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            Thread.sleep(500);
                            bookingDAO.deleteBooking(booking.getId());
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        progressBar.setVisible(false); // Hide the progress bar
                        refreshTableView(); // Refresh the table view after deletion
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully.");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        progressBar.setVisible(false); // Hide the progress bar
                        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the booking.");
                    }
                };
                // Bind the progress bar to the task's progress
                progressBar.progressProperty().bind(task.progressProperty());
                // Run the task in a new thread
                new Thread(task).start();
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a booking to delete.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    private void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_reserved_seat.setCellValueFactory(new PropertyValueFactory<>("reserved_seat"));
        column_username.setCellValueFactory(cellDataFeatures -> {
            Booking booking = cellDataFeatures.getValue();
            return new SimpleStringProperty(booking.getUser().getName());
        });
        column_screen_movie_name.setCellValueFactory(cellDataFeatures -> {
            Booking booking = cellDataFeatures.getValue();
            return  new SimpleStringProperty(booking.getScreen().getMovie_name());
        });
    }

    private void setupTableViewListeners() {
        tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                // Set text on the text field
                tf_id.setText(String.valueOf(newSelection.getId()));
                tf_reserved_seat.setText(String.valueOf(newSelection.getReserved_seat()));
                tf_user_name.setText(newSelection.getUser().getName());
                tf_movie_name.setText(newSelection.getScreen().getMovie_name());
            }
        });
    }

    private void loadData() {
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        Task<ObservableList<Booking>> task = new Task<>() {
            @Override
            protected ObservableList<Booking> call() throws Exception {
                // Simulate a delay for loading data
                Thread.sleep(500); // Simulate delay for demonstration purposes

                // Fetch data from the database
                List<Booking> bookings = bookingDAO.getAllBookings();
                bookingList.addAll(bookings);
                return bookingList;
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

    private void refreshTableView() {
        bookingList.clear();
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            bookingList.addAll(bookings);

            Platform.runLater(() -> {
                tableView.setItems(bookingList);
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
        tableView.getItems().addListener((javafx.collections.ListChangeListener<Booking>) _ -> tableView.refresh());
    }

    private void  filterBooking() {
        String idText = tf_search_id.getText();
        String screen_movie_name = tf_search_screen_movie_name.getText().toLowerCase();
        int iD = -1;
        boolean invalidIDInput = false;

        try {
            iD = idText.isEmpty() ? -1 : Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            invalidIDInput = true;
        }

        if (invalidIDInput) {
            tableView.setItems(FXCollections.observableArrayList());
            return;
        }
        int finalID = iD;

        List<Booking> filteredBookings = bookingList.stream()
                .filter(property -> finalID == -1 || property.getId() == finalID)
                .filter(property -> property.getScreen().getMovie_name().toLowerCase().contains(screen_movie_name))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredBookings));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUPButton();
        setupTableColumns();
        setupTableViewListeners();
        refreshTableViewListener();
        loadData();
        setupTextFieldFilter();

       FXMLSupport.setImage(iv_image,"/org/example/finalexam/Image/booking.png");
    }
}
