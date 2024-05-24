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
    @FXML private TableColumn<TableUser, String> idColumn;
    @FXML private TableColumn<TableUser, String> NameColumn;
    @FXML private TableColumn<TableUser, String> LoginColumn;
    @FXML private TableColumn<TableUser, String> PasswordColumn;
    @FXML private TableColumn<TableUser, String> PassportColumn;

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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LoginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        PasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        PassportColumn.setCellValueFactory(new PropertyValueFactory<>("passport"));

        ObservableList<TableUser> prodList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            long Id = resultSet.getLong("id");
            String Name = resultSet.getString("name");
            String Login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String passport = resultSet.getString("passport");
            TableUser prod = new TableUser(Id, Name, Login, password, passport);
            prodList.add(prod);
        }
        UserView.setItems(prodList);
    }
}
