package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CBookingMenu {
    @FXML private DatePicker Checkin;
    @FXML private DatePicker Exit;
    @FXML private Button B1;
    @FXML private Button B2;//посчитать цену
    @FXML private Button B3;
    @FXML private ChoiceBox<String> Class;
    @FXML private ChoiceBox<String> CountPeople;
    @FXML private Label Cost;//цена
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
            if (Checkin != null && Exit != null) {
                LocalDate date1 = Checkin.getValue();
                LocalDate date2 = Exit.getValue();
                int classNum = 0;
                int countPeople = 0;
                int costResult = 0;
                long daysBetween = ChronoUnit.DAYS.between(date1,date2);
                if(Class.getValue() == "Эконом")
                    classNum = 1150;
                else if (Class.getValue() == "Люкс")
                    classNum = 2500;
                else if (Class.getValue() == "Бизнес")
                    classNum = 3550;

                if(CountPeople.getValue() == "1")
                    countPeople = 800;
                else if (CountPeople.getValue() == "2")
                    countPeople = 1200;
                else if (CountPeople.getValue() == "3")
                    countPeople = 1750;
                else if (CountPeople.getValue() == "4")
                    countPeople = 2500;

                costResult = (int) (daysBetween*(classNum+countPeople));
                Cost.setText(String.valueOf(costResult));
            }

        });
        B3.setOnAction(e -> {
            System.out.println("To book");
        });
    }

}
