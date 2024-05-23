
        package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CAdminRequestMenu {
    @FXML private Button BackButton;
    @FXML private Button ButtonApprove;
    @FXML private Button ButtonReject;
    @FXML private TableView<TableRequest> RequestView;
    @FXML private TableColumn<TableRequest, Long> idColumn;
    @FXML private TableColumn<TableRequest, String> nameColumn;
    @FXML private TableColumn<TableRequest, String> entryColumn;
    @FXML private TableColumn<TableRequest, String> exitColumn;
    @FXML private TableColumn<TableRequest, String> classColumn;
    @FXML private TableColumn<TableRequest, String> costColumn;
    @FXML private TableColumn<TableRequest, String> countColumn;
    @FXML private TableColumn<TableRequest, String> roomColumn;

    @FXML
    void initialize() throws SQLException {
        UpdateDatabase();
        BackButton.setOnAction(e -> {
            BackButton.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMainMenu.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ButtonApprove.setOnAction(e -> {
            System.out.println("Approve button pressed");
        });
        ButtonReject.setOnAction(e -> {
            deleteSelectedItem();
        });
    }
    private void deleteSelectedItem() {
        TableRequest selectedProduct = RequestView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            RequestView.getItems().remove(selectedProduct);
            deleteFromDatabase(selectedProduct);
        }
    }

   private void deleteFromDatabase(TableRequest selectedProduct) {
        String requestQuery = "DELETE FROM request_date WHERE id = ?";
        String userQuery = "DELETE FROM user_date WHERE id = ?";
        String bookingQuery = "DELETE FROM booking WHERE id = ?";
        String roomQuery = "DELETE FROM room_date WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UserData", "postgres", "1111")) {
            try (PreparedStatement requestStatement = connection.prepareStatement(requestQuery)) {
                requestStatement.setLong(1, selectedProduct.getId());
                int rowsDeletedRequest = requestStatement.executeUpdate();
                System.out.println("Rows deleted from request_date: " + rowsDeletedRequest);
            }
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setLong(1, selectedProduct.getUserId());
                int rowsDeletedUser = userStatement.executeUpdate();
                System.out.println("Rows deleted from user_date: " + rowsDeletedUser);
            }
            try (PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery)) {
                bookingStatement.setLong(1, selectedProduct.getBookingId());
                int rowsDeletedBooking = bookingStatement.executeUpdate();
                System.out.println("Rows deleted from booking: " + rowsDeletedBooking);
            }
            try (PreparedStatement roomStatement = connection.prepareStatement(roomQuery)) {
                roomStatement.setString(1, selectedProduct.getRoom());
                int rowsDeletedRoom = roomStatement.executeUpdate();
                System.out.println("Rows deleted from room_date: " + rowsDeletedRoom);
            }
        } catch (SQLException ex) {
            System.out.println("Error when working with the database: " + ex.getMessage());
        }
    }

    private void UpdateDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Hotel";
        String user = "postgres";
        String password = "1111";

        String requestQuery = "SELECT id, user_id, booking_id, room_id FROM request_date";
        String userQuery = "SELECT id, name FROM user_date";
        String bookingQuery = "SELECT id, arrival_date, departure_date, quality, amoun_people, cost FROM booking";
        String roomQuery = "SELECT id FROM room_date";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement requestStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet requestResultSet = requestStatement.executeQuery(requestQuery);
             Statement userStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet userResultSet = userStatement.executeQuery(userQuery);
             Statement bookingStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet bookingResultSet = bookingStatement.executeQuery(bookingQuery);
             Statement roomStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet roomResultSet = roomStatement.executeQuery(roomQuery)) {

            // Prepare cell value factories
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            entryColumn.setCellValueFactory(new PropertyValueFactory<>("entry"));
            exitColumn.setCellValueFactory(new PropertyValueFactory<>("exit"));
            classColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
            roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));

            ObservableList<TableRequest> requestList = FXCollections.observableArrayList();

            while (requestResultSet.next()) {
                long requestId = requestResultSet.getLong("id");
                long userId = requestResultSet.getLong("user_id");
                long bookingId = requestResultSet.getLong("booking_id");
                String roomId = requestResultSet.getString("room_id");

                String userName = "";
                userResultSet.beforeFirst(); // Reset ResultSet
                while (userResultSet.next()) {
                    if (userResultSet.getLong("id") == userId) {
                        userName = userResultSet.getString("name");
                        break;
                    }
                }

                // Get booking details
                String arrivalDate = "";
                String departureDate = "";
                String quality = "";
                String amountPeople = "";
                String cost = "";
                bookingResultSet.beforeFirst(); // Reset ResultSet
                while (bookingResultSet.next()) {
                    if (bookingResultSet.getLong("id") == bookingId) {
                        arrivalDate = bookingResultSet.getString("arrival_date");
                        departureDate = bookingResultSet.getString("departure_date");
                        quality = bookingResultSet.getString("quality");
                        amountPeople = bookingResultSet.getString("amoun_people");
                        cost = bookingResultSet.getString("cost");
                        break;
                    }
                }

                TableRequest request = new TableRequest(requestId, userName, arrivalDate, departureDate, amountPeople, quality, cost, roomId, userId, bookingId);
                requestList.add(request);
                // Debug output
                System.out.println("Added request: " + request);
            }

            RequestView.setItems(requestList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}