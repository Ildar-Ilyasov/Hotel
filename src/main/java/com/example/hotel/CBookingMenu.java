package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CBookingMenu {
    @FXML
    private DatePicker Checkin;
    @FXML
    private DatePicker Exit;
    @FXML
    private Button B1;
    @FXML
    private Button B2;
    @FXML
    private Button B3;
    @FXML
    private ChoiceBox<String> Class;
    @FXML
    private ChoiceBox<String> CountPeople;
    @FXML
    private Label Cost;
    @FXML
    private ImageView imageView;
    @FXML
    private Button BackImage;
    @FXML
    private Button NextImage;
    private final ObservableList<String> ClassList = FXCollections.observableArrayList("Эконом", "Люкс", "Бизнес");
    private final ObservableList<String> CountPeopleList = FXCollections.observableArrayList("1", "2", "3", "4");
    private int currentImageIndex = 0;
    private final String[] images = {"C:\\Files\\project\\Hotel\\src\\main\\resources\\com\\example\\hotel\\res/Economy.jpg", "C:\\Files\\project\\Hotel\\src\\main\\resources\\com\\example\\hotel\\res/Luxury.jpg", "C:\\Files\\project\\Hotel\\src\\main\\resources\\com\\example\\hotel\\res/Business.jpg"};

    @FXML
    void initialize() {
        Checkin.setValue(LocalDate.now());
        Class.setValue("Выберите класс");
        CountPeople.setValue("Выберите количество");
        Class.setItems(ClassList);
        CountPeople.setItems(CountPeopleList);
        imageView.setImage(new Image(images[currentImageIndex]));

        BackImage.setOnAction(e -> showPreviousImage());
        NextImage.setOnAction(e -> showNextImage());
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
            if (!Class.getValue().equals("Выберите класс") && !CountPeople.getValue().equals("Выберите количество") &&
                    Exit.getValue() != null && Exit.getValue().isAfter(Checkin.getValue())) {
                LocalDate date1 = Checkin.getValue();
                LocalDate date2 = Exit.getValue();
                int classNum;
                int countPeople;
                int costResult;
                long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                switch (Class.getValue()) {
                    case "Эконом":
                        classNum = 1150;
                        break;
                    case "Люкс":
                        classNum = 2500;
                        break;
                    case "Бизнес":
                        classNum = 3550;
                        break;
                    default:
                        classNum = 0;
                        break;
                }
                switch (CountPeople.getValue()) {
                    case "1":
                        countPeople = 800;
                        break;
                    case "2":
                        countPeople = 1200;
                        break;
                    case "3":
                        countPeople = 1750;
                        break;
                    case "4":
                        countPeople = 2500;
                        break;
                    default:
                        countPeople = 0;
                        break;
                }
                costResult = (int) (daysBetween * (classNum + countPeople));
                Cost.setText(String.valueOf(costResult));
            } else {
                showAlert("Ошибка", "Поля не могут быть пустыми!");
            }
        });
        B3.setOnAction(e -> {
            if (!Class.getValue().equals("Выберите класс") && !CountPeople.getValue().equals("Выберите количество") &&
                    !Cost.getText().isEmpty() && Exit.getValue().isAfter(Checkin.getValue())) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hotel", "postgres", "1111")) {
                    String query = "INSERT INTO booking (arrival_date, departure_date, amount_people, quality, cost, fk_user_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
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
                    showAlert("Заявка принята!", "Хорошего отдыха!");
                } catch (SQLException ex) {
                    System.out.println("Ошибка при работе с базой данных: " + ex.getMessage());
                }
            } else {
                showAlert("Ошибка", "Поля не могут быть пустыми!");
            }
        });
    }

    private void showPreviousImage() {
        currentImageIndex = (currentImageIndex - 1 + images.length) % images.length;
        imageView.setImage(new Image(images[currentImageIndex]));
    }

    private void showNextImage() {
        currentImageIndex = (currentImageIndex + 1) % images.length;
        imageView.setImage(new Image(images[currentImageIndex]));
    }

    private void showAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
