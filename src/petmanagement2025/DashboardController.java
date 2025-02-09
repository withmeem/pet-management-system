package petmanagement2025;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {
    @FXML private Label totalPetsLabel;

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField petIdField;
    @FXML private Label messageLabel;
    
    @FXML private TableView<Pet> petTable;
    @FXML private TableColumn<Pet, Integer> idColumn;
    @FXML private TableColumn<Pet, String> nameColumn;
    @FXML private TableColumn<Pet, String> typeColumn;
    @FXML private TableColumn<Pet, Integer> ageColumn;
    @FXML private TableColumn<Pet, LocalDate> bdColumn;

    
    private ObservableList<Pet> petData = FXCollections.observableArrayList();

    public void initialize() {
        updateDashboardStats();
        setupTable();
        loadPetData();
        typeComboBox.setItems(FXCollections.observableArrayList(
            "Dog", "Cat", "Bird", "Fish", "Rat", "Rabbit", "Other"
        ));
    }

    private void updateDashboardStats() {
        try (Connection conn = DBUtil.getConnection()) {
            // Total Pets
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM pets");
            if (rs.next()) totalPetsLabel.setText("Total Pets: " +String.valueOf(rs.getInt(1)));


        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), "red");
        }
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        // Calculate age from birthdate
        ageColumn.setCellValueFactory(cellData -> {
            LocalDate birthdate = cellData.getValue().getBirthdate();
            int age = Period.between(birthdate, LocalDate.now()).getYears();
            return new SimpleIntegerProperty(age).asObject();
        });
        bdColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        petTable.setItems(petData);
    }

    private void loadPetData() {
        petData.clear();
        try (Connection conn = DBUtil.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM pets");
            while (rs.next()) {
                petData.add(new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getDate("birthdate").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), "red");
        }
    }

    @FXML
    private void handleAddPet() {
        String name = nameField.getText();
        String type = typeComboBox.getValue();
        LocalDate birthdate = birthdatePicker.getValue();

        if (name.isEmpty() || type == null || birthdate == null) {
            showMessage("Please fill all fields", "red");
            return;
        }

        try {

            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO pets (name, type, birthdate) VALUES (?, ?, ?)");
                pst.setString(1, name);
                pst.setString(2, type);
                pst.setDate(3, Date.valueOf(birthdate));
                pst.executeUpdate();
                
                showMessage("Pet added successfully!", "green");
                nameField.clear();
                typeComboBox.setValue(null);
                birthdatePicker.setValue(null);
                updateDashboardStats();
                loadPetData();
            }
        } catch (NumberFormatException e) {
            showMessage("Input correctly", "red");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), "red");
        }
    }

    @FXML
    private void handleDeletePet() {
        String idText = petIdField.getText();
        if (idText.isEmpty()) {
            showMessage("Please enter a Pet ID", "red");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM pets WHERE pet_id = ?");
                pst.setInt(1, id);
                int affected = pst.executeUpdate();
                
                if (affected > 0) {
                    showMessage("Pet deleted successfully!", "green");
                    petIdField.clear();
                    updateDashboardStats();
                    loadPetData();
                } else {
                    showMessage("No pet found with ID: " + id, "red");
                }
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid Pet ID format", "red");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), "red");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Stage stage = (Stage) petTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showMessage("Error loading login screen", "red");
        }
    }

    private void showMessage(String text, String color) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}