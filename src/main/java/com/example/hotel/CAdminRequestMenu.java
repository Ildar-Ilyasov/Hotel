package com.example.hotel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CAdminRequestMenu {
    @FXML private Button BackButton;
    @FXML private Button ButtonApprove;
    @FXML private Button ButtonReject;

    @FXML
    void initialize() {
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
}
