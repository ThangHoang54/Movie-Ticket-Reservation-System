package org.example.finalexam.controller.admin;

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
import javafx.scene.image.ImageView;
import org.example.finalexam.dao.BookingDAO;
import org.example.finalexam.dao.ScreenDAO;
import org.example.finalexam.dao.TheaterDAO;
import org.example.finalexam.daoImplement.BookingController;
import org.example.finalexam.daoImplement.ScreenController;
import org.example.finalexam.daoImplement.TheaterController;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ManageScreen_Controller implements Initializable {

    private final ScreenDAO screenDAO = new ScreenController();
    private final TheaterDAO theaterDAO = new TheaterController();
    private final BookingDAO bookingDAO = new BookingController();
    private final ObservableList<Screen> screensList = FXCollections.observableArrayList();

    @FXML
    private Button bt_return;
    @FXML
    private Button bt_reset_field;
    @FXML
    private TableColumn<Screen, Integer> column_id;
    @FXML
    private TableColumn<Screen, String> column_movie_name;
    @FXML
    private TableColumn<Screen, Integer> column_seat_available;
    @FXML
    private TableColumn<Screen, String> column_theater_name;
    @FXML
    private TableColumn<Screen, Integer> column_timing;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView<Screen> tableView;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_movie_name;
    @FXML
    private TextField tf_seat_available;
    @FXML
    private TextField tf_theater_name;
    @FXML
    private TextField tf_timing;
    @FXML
    private TextField tf_price;
    @FXML
    private TextField tf_search_movie_name;
    @FXML
    private ImageView iv_image;

    private void setUPButton() {
        bt_return.setOnAction(e -> {
            FXMLSupport.changeScene(e, "/org/example/finalexam/AdminUI/AdminHomePage.fxml", "Admin Homepage");
        });
        bt_reset_field.setOnAction(_ -> {
            tf_id.setText("");
            tf_movie_name.setText("");
            tf_seat_available.setText("");
            tf_theater_name.setText("");
            tf_timing.setText("");
            tf_price.setText("");
        });
    }

    private void setupTextFieldFilter() {
        tf_search_movie_name.textProperty().addListener((observable, oldValue, newValue) -> filterScreen());
    }

    @FXML
    void addScreen(ActionEvent event) {
        if (tf_id.getText().isEmpty()) {
            progressBar.setVisible(true); // Show the progress bar
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_timing.getText().isEmpty() || tf_movie_name.getText().isEmpty() || tf_seat_available.getText().isEmpty() || tf_theater_name.getText().isEmpty() || tf_price.getText().isEmpty()) {
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
                        int timing = Integer.parseInt(tf_timing.getText());
                        String movie_name = tf_movie_name.getText();
                        int seat_available = Integer.parseInt(tf_seat_available.getText());
                        String theater_name = tf_theater_name.getText();
                        double price = Double.parseDouble(tf_price.getText());

                        int theaterID;
                        try {
                            theaterID = theaterDAO.getTheaterIDByName(theater_name);
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Theater ID not found: " + e.getMessage());
                            return null;
                        }

                        Theater theater = new Theater.Builder()
                                .setID(theaterID)
                                .setName(theater_name)
                                .build();

                        Screen screen = new Screen.Builder()
                                .setTiming(timing)
                                .setMovieName(movie_name)
                                .setPrice(price)
                                .setSeatAvailable(seat_available)
                                .setTheater(theater)
                                .build();

                        Thread.sleep(500);
                        screenDAO.addScreen(screen);

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
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Screen added successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress
                    showAlert(Alert.AlertType.ERROR, "Add new Screen fail", "An error occurred while adding the screen.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Add new Screen fail", "The id text field must be empty.");
        }
    }

    @FXML
    void deleteScreen(ActionEvent event) {
        try {
            Screen screen = tableView.getSelectionModel().getSelectedItem();
            if (screen != null && !tf_id.getText().isEmpty()) {
                progressBar.setVisible(true); // Show the progress bar
                progressBar.progressProperty().unbind(); // Unbind the progress property
                progressBar.setProgress(0); // Reset progress

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            Thread.sleep(500);
                            screenDAO.deleteScreen(screen.getId());
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
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Screen deleted successfully.");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        progressBar.setVisible(false); // Hide the progress bar
                        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the screen.");
                    }
                };
                // Bind the progress bar to the task's progress
                progressBar.progressProperty().bind(task.progressProperty());
                // Run the task in a new thread
                new Thread(task).start();
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select the screen to delete.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    @FXML
    void updateScreen(ActionEvent event) {
        if (!tf_id.getText().isEmpty()) {
            // Show progress bar
            progressBar.setVisible(true);
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_timing.getText().isEmpty() || tf_movie_name.getText().isEmpty() || tf_seat_available.getText().isEmpty() || tf_theater_name.getText().isEmpty() || tf_price.getText().isEmpty()) {
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
                    int timing = Integer.parseInt(tf_timing.getText());
                    String movie_name = tf_movie_name.getText();
                    int seat_available = Integer.parseInt(tf_seat_available.getText());
                    String theater_name = tf_theater_name.getText();
                    double price = Double.parseDouble(tf_price.getText());

                    Screen currentScreen = screenDAO.getScreenById(id);
                    if (currentScreen == null) {
                        showAlert(Alert.AlertType.ERROR, "Update Error", "Screen not found with ID: " + id);
                        return null;
                    }

                    int theaterID;
                    try {
                        theaterID = theaterDAO.getTheaterIDByName(theater_name);
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Theater ID not found: " + e.getMessage());
                        return null;
                    }

                    Theater theater = new Theater.Builder()
                            .setID(theaterID)
                            .setName(theater_name)
                            .build();

                    Screen screen = new Screen.Builder()
                            .setId(id)
                            .setTiming(timing)
                            .setMovieName(movie_name)
                            .setPrice(price)
                            .setSeatAvailable(seat_available)
                            .setTheater(theater)
                            .build();

                    Thread.sleep(500);
                    screenDAO.updateScreen(screen);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false); // Hide the progress bar
                    refreshTableView(); // Refresh the table view after update
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Screen updated successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the screen.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select the screen to update.");
        }
    }

    public void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_timing.setCellValueFactory(new PropertyValueFactory<>("timing"));
        column_movie_name.setCellValueFactory(new PropertyValueFactory<>("movie_name"));
        column_theater_name.setCellValueFactory(cellDataFeatures -> {
            Screen screen = cellDataFeatures.getValue();
            return new SimpleStringProperty(screen.getTheater().getName());
        });
        column_seat_available.setCellValueFactory(new PropertyValueFactory<>("seat_available"));
    }

    private void setupTableViewListeners() {
        tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                // Set text on the text field
                tf_id.setText(String.valueOf(newSelection.getId()));
                tf_timing.setText(String.valueOf(newSelection.getTiming()));
                tf_movie_name.setText(newSelection.getMovie_name());
                tf_seat_available.setText(String.valueOf(newSelection.getSeat_available()));
                tf_theater_name.setText(newSelection.getTheater().getName());
                tf_price.setText(String.valueOf(newSelection.getPrice()));
            }
        });
    }

    private void loadData() {
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        Task<ObservableList<Screen>> task = new Task<>() {
            @Override
            protected ObservableList<Screen> call() throws Exception {
                // Simulate a delay for loading data
                Thread.sleep(500); // Simulate delay for demonstration purposes

                // Fetch data from the database
                List<Screen> screens = screenDAO.getAllScreens();
                for (Screen screen : screens) {
                    screen.setSeatAvailable(screen.getSeat_available() - bookingDAO.getBookingAlreadyBookSeatByScreenID(screen.getId()).size());
                    if (screen.getSeat_available() <= 0) {
                        screens.remove(screen);
                    }
                }
                screensList.addAll(screens);

                for (Screen screen : screensList) {
                    screen.setSeatAvailable(screen.getSeat_available() - bookingDAO.getBookingAlreadyBookSeatByScreenID(screen.getId()).size());
                }

                // Creating a Comparator for sorting by id
                Comparator<Screen> compareById = Comparator.comparingInt(Screen::getId);
                // Sorting the ObservableList using the Comparator
                FXCollections.sort(screensList, compareById);
                return screensList;
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
        screensList.clear();
        try {
            List<Screen> screens = screenDAO.getAllScreens();
            for (Screen screen : screens) {
                screen.setSeatAvailable(screen.getSeat_available() - bookingDAO.getBookingAlreadyBookSeatByScreenID(screen.getId()).size());
                if (screen.getSeat_available() <= 0) {
                    screens.remove(screen);
                }
            }
            screensList.addAll(screens);

            // Creating a Comparator for sorting by id
            Comparator<Screen> compareByID = Comparator.comparingInt(Screen::getId);
            // Sorting the ObservableList using the Comparator
            FXCollections.sort(screensList, compareByID);

            Platform.runLater(() -> {
                tableView.setItems(screensList);
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
        tableView.getItems().addListener((javafx.collections.ListChangeListener<Screen>) _ -> tableView.refresh());
    }

    private void  filterScreen() {
        String movie_name = tf_search_movie_name.getText().toLowerCase();

        List<Screen> filteredScreens = screensList.stream()
                .filter(property -> property.getMovie_name().toLowerCase().contains(movie_name))
                .sorted(Comparator.comparingInt(Screen::getTiming))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredScreens));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUPButton();
        setupTableColumns();
        setupTableViewListeners();
        refreshTableViewListener();
        loadData();
        setupTextFieldFilter();

       FXMLSupport.setImage(iv_image,"/org/example/finalexam/Image/screen.jpeg");
    }
}
