package org.example.finalexam.controller.pop_up;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.finalexam.model.Screen;

import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ViewScreenList_Controller {
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
    private TableView<Screen> tableView;

    public void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_timing.setCellValueFactory(new PropertyValueFactory<>("timing"));
        column_movie_name.setCellValueFactory(new PropertyValueFactory<>("movie_name"));
        column_seat_available.setCellValueFactory(new PropertyValueFactory<>("seat_available"));
        column_theater_name.setCellValueFactory(cellDataFeatures -> {
            Screen screen = cellDataFeatures.getValue();
            return new SimpleStringProperty(screen.getTheater().getName());
        });
    }

    public void setScreens(List<Screen> screens) {
        // Populate the table with screen entity
        for (Screen screen : screens) {
            // Add data to the table
            tableView.getItems().add(screen);
        }
    }

    public void setTableView(List<Screen> screens) {
            ObservableList<Screen> screenList = FXCollections.observableArrayList();
            screenList.addAll(screens);
            tableView.setItems(screenList);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }
}
