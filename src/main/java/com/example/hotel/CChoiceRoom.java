package com.example.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.sql.*;

public class CChoiceRoom {
    @FXML private Button B1;
    @FXML private ChoiceBox<Long> RoomNumber;
    private TableRequest selectedProduct;
    private CAdminRequestMenu adminRequestMenuController;
    @FXML
    void initialize() {
        B1.setOnAction(e -> {
            Long selectedRoomId = RoomNumber.getSelectionModel().getSelectedItem();
            if (selectedRoomId != null) {
                updateRequestWithSelectedRoom(selectedProduct, selectedRoomId);
            }
            B1.getScene().getWindow().hide();
            adminRequestMenuController.UpdateDatabase();
        });
    }
    public void initData(TableRequest selectedProduct, CAdminRequestMenu adminRequestMenuController) {
        this.selectedProduct = selectedProduct;
        this.adminRequestMenuController = adminRequestMenuController;
        loadRoomDataAndPopulateChoiceBox();
    }
    private void loadRoomDataAndPopulateChoiceBox() {
        String url = "jdbc:postgresql://localhost:5432/Hotel";
        String user = "postgres";
        String password = "1111";
        String roomQuery = "SELECT id, is_busy, amoun_people, quality FROM room_date";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement roomStatement = connection.createStatement();
             ResultSet roomResultSet = roomStatement.executeQuery(roomQuery)) {
             ObservableList<Long> availableRooms = FXCollections.observableArrayList();
            while (roomResultSet.next()) {
                boolean isBusy = roomResultSet.getBoolean("is_busy");
                String roomQuality = roomResultSet.getString("quality");
                int roomCount = roomResultSet.getInt("amoun_people");
                if (!isBusy && roomCount >= Integer.parseInt(selectedProduct.getCount()) && roomQuality.equals(selectedProduct.getQuality())) {
                    long roomId = roomResultSet.getLong("id");
                    availableRooms.add(roomId);
                }
            }
            RoomNumber.setItems(availableRooms);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateRequestWithSelectedRoom(TableRequest selectedProduct, Long selectedRoomId) {
        String url = "jdbc:postgresql://localhost:5432/Hotel";
        String user = "postgres";
        String password = "1111";
        String updateQuery = "UPDATE request_date SET room_id = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setLong(1, selectedRoomId);
            updateStatement.setLong(2, selectedProduct.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}