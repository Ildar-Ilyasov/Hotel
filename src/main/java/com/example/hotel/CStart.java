package com.example.hotel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class CStart {
    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private TextField TF1;
    @FXML private PasswordField PF1;
    @FXML
    void initialize() {
        B1.setOnAction(e -> {
            String login = TF1.getText();
            String password = PF1.getText();
            String HashedPassword = hashString(password);
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hotel", "postgres", "1111");
                String query = "SELECT login,password FROM user_date WHERE login = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);
                statement.setString(2, HashedPassword);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("Menu.fxml"));
                    loader.load();
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    B1.getScene().getWindow().hide();
                } else {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("Error.fxml"));
                    loader.load();
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        B2.setOnAction(e -> {
            B2.getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Registration.fxml"));
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}