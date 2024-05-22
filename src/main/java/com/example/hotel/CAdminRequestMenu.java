
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
    @FXML private TableColumn<TableRequest, String> countColumn;
    @FXML private TableColumn<TableRequest, String> costColumn;
    @FXML private TableColumn<TableRequest, String> roomColumn;

    @FXML
    void initialize() throws SQLException {
        UpdateDatabase();
        BackButton.setOnAction(e -> {
            BackButton.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AdminMainMenu.fxml"));
                loader.load();
                Parent root = loader.getRoot();
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
            System.out.println("Reject button pressed");
        });
    }

    private void UpdateDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Hotel";
        String user = "postgres";
        String password = "1111";

        String requestQuery = "SELECT * FROM request_date";
        String userQuery = "SELECT * FROM user_date";
        String bookingQuery = "SELECT * FROM booking";
        String roomQuery = "SELECT * FROM room_date";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet requestResultSet = statement.executeQuery(requestQuery);
             Statement userStatement = connection.createStatement();
             ResultSet userResultSet = userStatement.executeQuery(userQuery);
             Statement bookingStatement = connection.createStatement();
             ResultSet bookingResultSet = bookingStatement.executeQuery(bookingQuery);
             Statement roomStatement = connection.createStatement();
             ResultSet roomResultSet = roomStatement.executeQuery(roomQuery)) {

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            entryColumn.setCellValueFactory(new PropertyValueFactory<>("arrival_date"));
            exitColumn.setCellValueFactory(new PropertyValueFactory<>("departure_date"));
            classColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
            roomColumn.setCellValueFactory(new PropertyValueFactory<>("room_number"));

            ObservableList<TableRequest> requestList = FXCollections.observableArrayList();

            while (requestResultSet.next()) {
                long id = requestResultSet.getLong("id");
                long userId = requestResultSet.getLong("user_id");
                long bookingId = requestResultSet.getLong("booking_id");
                long roomId = requestResultSet.getLong("room_id");

                // Get data from user_date
                userResultSet.beforeFirst();
                String name = null;
                while (userResultSet.next()) {
                    if (userResultSet.getLong("id") == userId) {
                        name = userResultSet.getString("name");
                        break;
                    }
                }

                // Get data from booking
                bookingResultSet.beforeFirst();
                String arrivalDate = null;
                String departureDate = null;
                String quality = null;
                String count = "";
                String cost = "";
                while (bookingResultSet.next()) {
                    if (bookingResultSet.getLong("id") == bookingId) {
                        arrivalDate = bookingResultSet.getString("arrival_date");
                        departureDate = bookingResultSet.getString("departure_date");
                        quality = bookingResultSet.getString("quality");
                        count = bookingResultSet.getString("count");
                        cost = bookingResultSet.getString("cost");
                        break;
                    }
                }

                // Get data from room_date
                roomResultSet.beforeFirst();
                String roomNumber = null;
                while (roomResultSet.next()) {
                    if (roomResultSet.getLong("id") == roomId) {
                        roomNumber = roomResultSet.getString("room_number");
                        break;
                    }
                }

                TableRequest request = new TableRequest(id, name, arrivalDate, departureDate, quality, count,  cost, roomNumber);
                requestList.add(request);
            }
            RequestView.setItems(requestList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}