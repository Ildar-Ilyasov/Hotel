package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class CBookingMenu {
    @FXML private DatePicker Checkin;
    @FXML private DatePicker Exit;
    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private ChoiceBox<String> Class;
    @FXML private ChoiceBox<String> CountPeople;
    @FXML private TextField Cost;
    ObservableList<String> ClassList = FXCollections.observableArrayList("Эконом", "Люкс", "Бизнес");
    ObservableList<String> CountPeopleList = FXCollections.observableArrayList("1", "2", "3", "4");
    @FXML
    void initialize(){
        Checkin.setValue(LocalDate.now());
        Class.setValue("Выберите класс");
        CountPeople.setValue("Выберите количество");
        Class.setItems(ClassList);
        CountPeople.setItems(CountPeopleList);
        B1.setOnAction(e -> {
            Start start = new Start();
            try {
                start.start(new Stage());
                B1.getScene().getWindow().hide();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        B2.setOnAction(e -> {
            System.out.println("Cost");
        });
        B3.setOnAction(e -> {
            System.out.println("To book");
        });
    }
}
