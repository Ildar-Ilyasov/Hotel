package com.example.hotel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CRegistration {
    @FXML private Button B1;
    @FXML private Button BClose;
    @FXML private TextField TF1;
    @FXML private TextField TF2;
    @FXML private TextField TF3;
    @FXML private TextField TF4;

    public CRegistration() {
    }

    @FXML
    void initialize() {
        B1.setOnAction(e -> {
            String name = TF1.getText();
            String login = TF2.getText();
            String password = TF3.getText();
            String passport = TF4.getText();
            com.example.hotel.Start start = new com.example.hotel.Start();
            String hashedPassword = hashString(password);
            if (!name.isEmpty() && !login.isEmpty() && !password.isEmpty() && passport.length() == 10) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hotel", "postgres", "1111")) {
                    String query = String.format("INSERT INTO user_date (name, login, password, passport) VALUES ('%s', '%s', '%s', '%s')",name,login,hashedPassword,passport);
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    /*preparedStatement.setString(1, name);
                    preparedStatement.setString(2, login);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, passport);*/
                    preparedStatement.executeUpdate();
                    try {
                        start.start(new Stage());
                        B1.getScene().getWindow().hide();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (SQLException ex) {
                    System.out.println("Error when working with the database: " + ex.getMessage());
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Ошибка");
                alert.setContentText("Поля не могут быть пустыми!");
                alert.showAndWait();
            }
        });
        BClose.setOnAction(e -> {
            com.example.hotel.Start start = new com.example.hotel.Start();
            try {
                start.start(new Stage());
                BClose.getScene().getWindow().hide();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private String hashString(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte a : hashBytes) {
                String hex = Integer.toHexString(0xff & a);
                if (hex.length() == 1) {hexString.append('0');}
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}