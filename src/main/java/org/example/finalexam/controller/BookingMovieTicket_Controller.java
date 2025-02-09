package org.example.finalexam.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.controller.pop_up.ViewBookingList_Controller;
import org.example.finalexam.dao.BookingDAO;
import org.example.finalexam.dao.ScreenDAO;
import org.example.finalexam.dao.TheaterDAO;
import org.example.finalexam.dao.UserDAO;
import org.example.finalexam.daoImplement.BookingController;
import org.example.finalexam.daoImplement.ScreenController;
import org.example.finalexam.daoImplement.TheaterController;
import org.example.finalexam.daoImplement.UserController;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.User;
import org.example.finalexam.service.UserService;
import org.example.finalexam.utils.FXMLSupport;
import org.example.finalexam.utils.GenerateInput;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class BookingMovieTicket_Controller implements Initializable {

    private final ScreenDAO screenDAO = new ScreenController();
    private final TheaterDAO theaterDAO = new TheaterController();
    private final UserDAO userDAO = new UserController();
    private final BookingDAO bookingDAO = new BookingController();
    private final UserService userService = new UserService();
    private final ObservableList<Screen> screensList = FXCollections.observableArrayList();
    private User userSession;
    private int totalAvailableSeats;
    private List<Integer> bookedSeats;

    @FXML
    private Button bt_return;
    @FXML
    private Button bt_reset_field;
    @FXML
    private Label lb_fullname;
    @FXML
    private Label lb_contact_info;
    @FXML
    private Label lb_welcome;
    @FXML
    private Label lb_price;
    @FXML
    private Label lb_total_price;
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
    private TextField tf_theater_name;
    @FXML
    private TextField tf_movie_name;
    @FXML
    private TextField tf_theater_address;
    @FXML
    private Spinner<Integer> sn_quantity;
    @FXML
    private TextField tf_search_movie_name;
    @FXML
    private TextField tf_search_theater_name;
    @FXML
    private ImageView iv_image;

    /**
     * Sets a user session for the controller and populates the profile data
     * @param userSession The user session to set
     */
    public void setUser(User userSession) {
        this.userSession = userSession;
        loadUserInfo();
    }

    private void loadUserInfo() {
        lb_welcome.setText("Welcome\n" + userSession.getName());
        lb_fullname.setText(userSession.getName());
        lb_contact_info.setText(userSession.getContact_info());
        FXMLSupport.setImage(iv_image,"/org/example/finalexam/User_Image/" + GenerateInput.getSimplifyFullName(userSession.getName()) + ".jpg");
    }

    private void setupSpinner() {
        // Create a spinner with a range from 1 to 10
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1);
        sn_quantity.setValueFactory(valueFactory);
    }

    private void setUPButton() {
        bt_return.setOnAction(e -> {
            FXMLSupport.changeScene(e,"/org/example/finalexam/Welcome_Page.fxml", "Welcome Page");
        });
        bt_reset_field.setOnAction(_ -> {
            tf_theater_name.setText("");
            tf_movie_name.setText("");
            tf_theater_address.setText("");
            lb_price.setText("$0.0");
            lb_total_price.setText("$0.0");
            sn_quantity.getValueFactory().setValue(0);
        });
    }

    private void setupTextFieldFilter() {
        tf_search_movie_name.textProperty().addListener((observable, oldValue, newValue) -> filterScreen());
        tf_search_theater_name.textProperty().addListener((observable, oldValue, newValue) -> filterScreen());
    }

    @FXML
    void bookingTicket() {
        if (Double.parseDouble(lb_total_price.getText().substring(1)) > 0.0) {
            progressBar.setVisible(true); // Show the progress bar
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_theater_name.getText().isEmpty() || tf_movie_name.getText().isEmpty() || tf_theater_address.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Booking a Ticket Error", "Please choose 1 screen from the table to perform booking ticket.");
                progressBar.setVisible(false);
                return;
            }

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        for (int i = 0; i < sn_quantity.getValue(); i++) {

                            String movie_name = tf_movie_name.getText();
                            String user_name = userSession.getName();
                            java.sql.Date booking_date = Date.valueOf(LocalDate.now());
                            totalAvailableSeats = screenDAO.getScreenTotalSeatByMovieName(movie_name);
                            bookedSeats = bookingDAO.getBookingAlreadyBookSeatByScreenID(screenDAO.getScreenIDByScreenName(movie_name));
                            int reserved_seat = GenerateInput.getRandomAvailableSeatNumber(totalAvailableSeats, bookedSeats);

                            int screenID;
                            int userID;
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
                                    .setBooking_date(booking_date)
                                    .setScreen(screen)
                                    .setUser(user)
                                    .build();

                            Thread.sleep(500);
                            bookingDAO.addBooking(booking);
                        }
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
                    refreshTableViewScreen();
                    refreshTableViewBooking();
                    bookedSeats.clear();
                    totalAvailableSeats = 0;
                    showAlert(Alert.AlertType.INFORMATION, "Success", "You had books the ticket" + ((sn_quantity.getValue() > 1) ?"s ":" ") + "successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress

                    showAlert(Alert.AlertType.ERROR, "Add new Booking fail", "Please choose 1 screen from the table to perform booking ticket.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Booking a Ticket Fail", "Please choose 1 screen from the table to perform booking ticket.");
        }

    }

    @FXML
    void handleViewBookings(ActionEvent event) {
        try {
            if (!userSession.getBookings().isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/finalexam/Pop_Up_Window/Booking.fxml"));
                Parent root = loader.load();
                ViewBookingList_Controller controller = loader.getController();
                controller.setupTableColumns();

                controller.setBookings(userSession.getBookings());

                Stage stage = new Stage();
                stage.setTitle("Booking List");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Booking List Announce", "There is no booking for this user.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupTableColumns() {
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
                tf_theater_name.setText(newSelection.getTheater().getName());
                tf_movie_name.setText(newSelection.getMovie_name());
                tf_theater_address.setText(newSelection.getTheater().getAddress());
                lb_price.setText("$" + newSelection.getPrice());
                lb_total_price.setText("$" + newSelection.getPrice() * sn_quantity.getValue());

                totalAvailableSeats = newSelection.getSeat_available();
            }
        });
    }

    private void loadScreenDataTable() {
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

    private void refreshTableViewScreen() {
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

    private void refreshTableViewBooking() {
        userSession.getBookings().clear();

        try {
            List<Booking> bookings = userService.getBookingsByUserId(userSession.getId()) ;
            userSession.setBookings(bookings);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    private void totalPriceListener() {
        // Add a listener to the spinner to update the total price whenever the value changes
        sn_quantity.valueProperty().addListener((_, _, newValue) -> {
            double total_price = newValue * Double.parseDouble(lb_price.getText().substring(1));
            lb_total_price.setText("$" + total_price);
        });
    }

    /**
     * Adds a listener to automatically refresh the TableView when changes occur.
     */
    private void refreshTableViewScreenListener() {
        tableView.getItems().addListener((javafx.collections.ListChangeListener<Screen>) _ -> tableView.refresh());
    }

    private void  filterScreen() {
        String movie_name = tf_search_movie_name.getText().toLowerCase();
        String theater_name = tf_search_theater_name.getText().toLowerCase();

        List<Screen> filteredScreens = screensList.stream()
                .filter(property -> property.getMovie_name().toLowerCase().contains(movie_name))
                .filter(property -> property.getTheater().getName().toLowerCase().contains(theater_name))
                .sorted(Comparator.comparingInt(Screen::getTiming))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredScreens));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_total_price.setText("$0.0");
        lb_price.setText("$0.0");
        setUPButton();
        setupSpinner();
        setupTableColumns();
        totalPriceListener();
        setupTableViewListeners();
        refreshTableViewScreenListener();
        loadScreenDataTable();
        setupTextFieldFilter();
    }
}
