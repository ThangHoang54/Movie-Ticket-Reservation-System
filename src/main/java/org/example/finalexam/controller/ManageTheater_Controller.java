package org.example.finalexam.controller;

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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.controller.pop_up.ViewScreenList_Controller;
import org.example.finalexam.dao.TheaterDAO;
import org.example.finalexam.daoImplement.TheaterController;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.example.finalexam.utils.FXMLSupport;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.example.finalexam.utils.FXMLSupport.showAlert;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ManageTheater_Controller implements Initializable {

    private List<Screen> scenesList = new ArrayList<>();
    private final TheaterDAO theaterDAO = new TheaterController();
    private final ObservableList<Theater> theaterList = FXCollections.observableArrayList();

    @FXML
    private Button bt_return;
    @FXML
    private Button bt_reset_field;
    @FXML
    private TableColumn<Theater, String> column_address;
    @FXML
    private TableColumn<Theater, Integer> column_id;
    @FXML
    private TableColumn<Theater, String> column_name;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView<Theater> tableView;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_search_name;
    @FXML
    private TextField tf_search_address;
    @FXML
    private ImageView iv_image;

    private void setUPButton() {
        bt_return.setOnAction(e -> {
            FXMLSupport.changeScene(e,"/org/example/finalexam/Welcome_Page.fxml", "Welcome Page");
        });
        bt_reset_field.setOnAction(e -> {
            tf_id.setText("");
            tf_name.setText("");
            tf_address.setText("");
            scenesList.clear();
        });
    }

    private void setupTextFieldFilter() {
        tf_search_name.textProperty().addListener((observable, oldValue, newValue) -> filterTheater());
        tf_search_address.textProperty().addListener((observable, oldValue, newValue) -> filterTheater());
    }

    @FXML
    void addTheater(ActionEvent event) {
        if (tf_id.getText().isEmpty()) {
            progressBar.setVisible(true); // Show the progress bar
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_name.getText().isEmpty() || tf_address.getText().isEmpty()) {
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
                        String name = tf_name.getText();
                        String address = tf_address.getText();

                        Theater theater = new Theater.Builder()
                                .setName(name)
                                .setAddress(address)
                                .build();

                        Thread.sleep(500);
                        theaterDAO.addTheater(theater);

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
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Theater added successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.progressProperty().unbind(); // Unbind the progress property
                    progressBar.setProgress(0); // Reset progress
                    showAlert(Alert.AlertType.ERROR, "Add new Theater fail", "An error occurred while adding the theater.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.ERROR, "Add new Theater fail", "The id text field must be empty.");
        }
    }

    @FXML
    void deleteTheater(ActionEvent event) {
        try {
            Theater theater = tableView.getSelectionModel().getSelectedItem();
            if (theater != null && !tf_id.getText().isEmpty()) {
                progressBar.setVisible(true); // Show the progress bar
                progressBar.progressProperty().unbind(); // Unbind the progress property
                progressBar.setProgress(0); // Reset progress

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            Thread.sleep(500);
                            theaterDAO.deleteTheater(theater.getId());
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
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Theater deleted successfully.");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        progressBar.setVisible(false); // Hide the progress bar
                        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the theater.");
                    }
                };
                // Bind the progress bar to the task's progress
                progressBar.progressProperty().bind(task.progressProperty());
                // Run the task in a new thread
                new Thread(task).start();
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a theater to delete.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    @FXML
    void updateTheater(ActionEvent event) {
        if (!tf_id.getText().isEmpty()) {
            // Show progress bar
            progressBar.setVisible(true);
            progressBar.progressProperty().unbind(); // Unbind the progress property
            progressBar.setProgress(0); // Reset progress

            if (tf_name.getText().isEmpty() || tf_address.getText().isEmpty()) {
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
                    String name = tf_name.getText();
                    String address = tf_address.getText();

                    Theater currentTheater = theaterDAO.getTheaterById(id);
                    if (currentTheater == null) {
                        showAlert(Alert.AlertType.ERROR, "Update Error", "Theater not found with ID: " + id);
                        return null;
                    }
                    Theater theater = new Theater.Builder()
                            .setID(id)
                            .setName(name)
                            .setAddress(address)
                            .build();

                    Thread.sleep(500);
                    theaterDAO.updateTheater(theater);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false); // Hide the progress bar
                    refreshTableView(); // Refresh the table view after update
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Theater updated successfully.");
                }

                @Override
                protected void failed() {
                    super.failed();
                    progressBar.setVisible(false); // Hide the progress bar
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the theater.");
                }
            };
            // Bind the progress bar to the task's progress
            progressBar.progressProperty().bind(task.progressProperty());
            // Run the task in a new thread
            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a theater to update.");
        }
    }

    @FXML
    public void handlingViewScreen(ActionEvent event) {
        try {
            if (!tf_id.getText().isEmpty()) {
                if (!scenesList.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/finalexam/Pop_Up_Window/Screen.fxml"));
                    Parent root = loader.load();
                    ViewScreenList_Controller controller = loader.getController();
                    controller.setupTableColumns();

                    controller.setScreens(scenesList);

                    Stage stage = new Stage();
                    stage.setTitle("Screen List");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, " List Announce", "There is no screen for this theater.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a theater to view it screen(s).");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void setupTableViewListeners() {
        tableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                // Set text on the text field
                tf_id.setText(String.valueOf(newSelection.getId()));
                tf_name.setText(newSelection.getName());
                tf_address.setText(newSelection.getAddress());

                // Get all attribute list of the specific selected user for a suitable pop-up window
                scenesList.clear();

                // Check if the screens list is not null before adding
                if (newSelection.getScreens() != null) {
                    scenesList.addAll(newSelection.getScreens());
                } else {
                    System.out.println("The screen list of the select theater is null");
                }
            }
        });
    }

    private void loadData() {
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        Task<ObservableList<Theater>> task = new Task<>() {
            @Override
            protected ObservableList<Theater> call() throws Exception {
                // Simulate a delay for loading data
                Thread.sleep(500); // Simulate delay for demonstration purposes

                // Fetch data from the database
                List<Theater> theaters = theaterDAO.getAllTheaters();
                theaterList.addAll(theaters);
                // Creating a Comparator for sorting by id
                Comparator<Theater> compareByID = Comparator.comparingInt(Theater::getId);
                // Sorting the ObservableList using the Comparator
                FXCollections.sort(theaterList, compareByID);
                return theaterList;
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
        theaterList.clear();
        try {
            List<Theater> theaters = theaterDAO.getAllTheaters();
            theaterList.addAll(theaters);
            // Creating a Comparator for sorting by id
            Comparator<Theater> compareByID = Comparator.comparingInt(Theater::getId);
            // Sorting the ObservableList using the Comparator
            FXCollections.sort(theaterList, compareByID);

            Platform.runLater(() -> {
                tableView.setItems(theaterList);
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
        tableView.getItems().addListener((javafx.collections.ListChangeListener<Theater>) _ -> tableView.refresh());
    }

    private void filterTheater() {
        String name = tf_search_name.getText().toLowerCase();
        String address = tf_search_address.getText().toLowerCase();

        List<Theater> filteredTheaters = theaterList.stream()
                .filter(property -> property.getName().toLowerCase().contains(name))
                .sorted(Comparator.comparing(Theater::getName))
                .filter(property -> property.getAddress().toLowerCase().contains(address))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredTheaters));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUPButton();
        setupTableColumns();
        setupTableViewListeners();
        refreshTableViewListener();
        loadData();
        setupTextFieldFilter();

       FXMLSupport.setImage(iv_image,"/org/example/finalexam/Image/theater.jpeg");
    }
}
