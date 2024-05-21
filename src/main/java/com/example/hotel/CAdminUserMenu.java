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

public class CAdminUserMenu {
    @FXML
    private Button BackButton;
    @FXML
    private TableView<TableUser> UserView;
    @FXML private TableColumn<TableUser, String> IdColumn;
    @FXML private TableColumn<TableUser, String> NameColumn;
    @FXML private TableColumn<TableUser, String> LoginColumn;
    @FXML private TableColumn<TableUser, String> PasswordColumn;
    @FXML private TableColumn<TableUser, String> PassportColumn;
    @FXML private TableColumn<TableUser, String> RoomColumn;
    @FXML private TableColumn<TableUser, String> CheckinColumn;
    @FXML private TableColumn<TableUser, String> DepartureColumn;
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

    }
    private void UpdateDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hotel", "postgres", "1111");
        String fQuery = "SELECT id, name, login, password, passport, fk_room, fk_booking FROM user_date";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(fQuery);
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LoginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        PasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        PassportColumn.setCellValueFactory(new PropertyValueFactory<>("passport"));
        RoomColumn.setCellValueFactory(new PropertyValueFactory<>("fk_room"));
        CheckinColumn.setCellValueFactory(new PropertyValueFactory<>("fk_booking"));
        DepartureColumn.setCellValueFactory(new PropertyValueFactory<>("fk_booking"));

        ObservableList<TableUser> prodList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            long Id = resultSet.getLong("id");
            String Name = resultSet.getString("name");
            String Login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String passport = resultSet.getString("passport");
            long Room = resultSet.getLong("fk_room");
            long Checkin = resultSet.getLong("fk_booking");
            long Departure = resultSet.getLong("fk_booking");
            TableUser prod = new TableUser(Id, Name, Login, password, passport, Room, Checkin, Departure);
            prodList.add(prod);
        }
        UserView.setItems(prodList);
    }
}
