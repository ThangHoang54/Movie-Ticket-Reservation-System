package org.example.finalexam.controller.pop_up;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.finalexam.model.Booking;

import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ViewBookingList_Controller {
    @FXML
    private TableView<Booking> tableView;
    @FXML
    private TableColumn<Booking, Integer> column_reserved_seat;
    @FXML
    private TableColumn<Booking, String> column_screen_movie_name;
    @FXML
    private TableColumn<Booking, String> column_user_fullname;
    @FXML
    private TableColumn<Booking, Integer> column_id;

    public void setupTableColumns() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_reserved_seat.setCellValueFactory(new PropertyValueFactory<>("reserved_seat"));
        column_screen_movie_name.setCellValueFactory(cellDataFeatures -> {
            Booking booking = cellDataFeatures.getValue();
            return new SimpleStringProperty(booking.getScreen().getMovie_name());
        });
        column_user_fullname.setCellValueFactory(cellDataFeatures -> {
            Booking booking = cellDataFeatures.getValue();
            return new SimpleStringProperty(booking.getUser().getName());
        });
    }

    public void setBookings(List<Booking> bookings) {
        // Populate the table with booking records
        for (Booking booking : bookings) {
            // Add data to the table
            tableView.getItems().add(booking);
        }
    }

    public void setTableView(List<Booking> bookings) {
            ObservableList<Booking> bookingList = FXCollections.observableArrayList();
            bookingList.addAll(bookings);
            tableView.setItems(bookingList);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }
}

