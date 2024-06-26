package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CBookingMenu {
    @FXML private DatePicker Checkin;
    @FXML private DatePicker Exit;
    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private ChoiceBox<String> Class;
    @FXML private ChoiceBox<String> CountPeople;
    @FXML private Label Cost;
    ObservableList<String> ClassList = FXCollections.observableArrayList("Эконом", "Люкс", "Бизнес");
    ObservableList<String> CountPeopleList = FXCollections.observableArrayList("1", "2", "3", "4");
    @FXML
    void initialize(){
        String ClassRoom =  Class.getValue();
        String Count = CountPeople.getValue();
        String cost = Cost.getText();
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
            if (ClassRoom != "Выберите класс" && Count != "Выберите количество" && Exit.getValue() != null && Exit.getValue().isAfter(Checkin.getValue())) {
                if (Checkin != null && Exit != null) {
                    LocalDate date1 = Checkin.getValue();
                    LocalDate date2 = Exit.getValue();
                    int classNum = 0;
                    int countPeople = 0;
                    int costResult = 0;
                    long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                    if (Class.getValue() == "Эконом")
                        classNum = 1150;
                    else if (Class.getValue() == "Люкс")
                        classNum = 2500;
                    else if (Class.getValue() == "Бизнес")
                        classNum = 3550;

                    if (CountPeople.getValue() == "1")
                        countPeople = 800;
                    else if (CountPeople.getValue() == "2")
                        countPeople = 1200;
                    else if (CountPeople.getValue() == "3")
                        countPeople = 1750;
                    else if (CountPeople.getValue() == "4")
                        countPeople = 2500;

                    costResult = (int) (daysBetween * (classNum + countPeople));
                    Cost.setText(String.valueOf(costResult));
                }
            }
            else {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("ErrorReg.fxml"));
                    loader.load();
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        B3.setOnAction(e -> {
            if (ClassRoom != "Выберите класс" && Count != "Выберите количество" && !cost.isEmpty() && Exit.getValue().isAfter(Checkin.getValue())) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hotel", "postgres", "1111")) {
                    String query = "INSERT INTO booking (arrival_date, departure_date, amoun_people, quality, cost, fk_user_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setDate(1, java.sql.Date.valueOf(Checkin.getValue()));
                        preparedStatement.setDate(2, java.sql.Date.valueOf(Exit.getValue()));
                        preparedStatement.setInt(3, Integer.parseInt(CountPeople.getValue()));
                        preparedStatement.setString(4, Class.getValue());
                        preparedStatement.setBigDecimal(5, new java.math.BigDecimal(Cost.getText()));
                        preparedStatement.setLong(6, Session.getCurrentUserId());

                        try (ResultSet rs = preparedStatement.executeQuery()) {
                            if (rs.next()) {
                                long bookingId = rs.getLong("id");
                                String requestQuery = "INSERT INTO request_date (user_id, booking_id) VALUES (?, ?)";
                                try (PreparedStatement requestStatement = connection.prepareStatement(requestQuery)) {
                                    requestStatement.setLong(1, Session.getCurrentUserId());
                                    requestStatement.setLong(2, bookingId);
                                    requestStatement.executeUpdate();
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Error when working with the database: " + ex.getMessage());
                }
            }
            else {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("ErrorReg.fxml"));
                    loader.load();
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
