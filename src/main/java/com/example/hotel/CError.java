package com.example.hotel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CError {
    @FXML private Button B1;
    @FXML
    void initialize(){
        B1.setOnAction(e -> {
            B1.getScene().getWindow().hide();
        });
    }
}