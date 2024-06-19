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
public class CAdminMainMenu {
    @FXML private Button BackButton;
    @FXML private Button ButtonApplication;
    @FXML private Button ButtonUser;
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
    void initialize() {
        UpdateDatabase();
        BackButton.setOnAction(e -> {
            BackButton.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Start.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ButtonApplication.setOnAction(e -> {
            ButtonApplication.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AdminRequestMenu.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ButtonUser.setOnAction(e -> {
            ButtonUser.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AdminUserMenu.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            entryColumn.setCellValueFactory(new PropertyValueFactory<>("entry"));
            exitColumn.setCellValueFactory(new PropertyValueFactory<>("exit"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            classColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
            roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));

            ObservableList<TableRequest> requestList = FXCollections.observableArrayList();

            while (requestResultSet.next()) {
                long requestId = requestResultSet.getLong("id");
                long userId = requestResultSet.getLong("user_id");
                long bookingId = requestResultSet.getLong("booking_id");
                String roomId = requestResultSet.getString("room_id");

                String userName = "";
                userResultSet.beforeFirst();
                while (userResultSet.next()) {
                    if (userResultSet.getLong("id") == userId) {
                        userName = userResultSet.getString("name");
                        break;
                    }
                }

                String arrivalDate = "";
                String departureDate = "";
                String quality = "";
                String amountPeople = "";
                String cost = "";
                bookingResultSet.beforeFirst();
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

                if(roomId != null){
                    TableRequest request = new TableRequest(requestId, userName, arrivalDate, departureDate, quality, amountPeople, cost, roomId, userId, bookingId);
                    requestList.add(request);
                    // Отладка для вывода
                    System.out.println("Added request: " + request);
                }}

            RequestView.setItems(requestList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}