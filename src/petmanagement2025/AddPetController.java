package petmanagement2025;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class AddPetController {
    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField ageField;
    @FXML private Label messageLabel;

    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(
            "Dog", "Cat", "Bird", "Fish", "Rat", "Rabit", "Other"
        ));
    }

    @FXML
    private void handleAddPet() {
        String name = nameField.getText();
        String type = typeComboBox.getValue();
        String ageText = ageField.getText();

        if (name.isEmpty() || type == null || ageText.isEmpty()) {
            messageLabel.setText("Please fill all fields");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            
            try (Connection conn = DBUtil.getConnection()) {
                String query = "INSERT INTO pets (name, type, age) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, type);
                pst.setInt(3, age);
                
                pst.executeUpdate();
                
                logAction("Added new pet: " + name);
                messageLabel.setText("Pet added successfully!");
                clearFields();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Age must be a number");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void logAction(String action) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO history_log (action) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, action);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        typeComboBox.setValue(null);
        ageField.clear();
    }

    @FXML private void showDashboard() { loadScreen("DashboardScreen.fxml"); }
    @FXML private void showAddPet() { loadScreen("AddPetScreen.fxml"); }
    @FXML private void showPetList() { loadScreen("PetListScreen.fxml"); }
    @FXML private void showDeletePet() { loadScreen("DeletePetScreen.fxml"); }
    @FXML private void showHistory() { loadScreen("HistoryScreen.fxml"); }
    @FXML private void handleLogout() { loadScreen("LoginScreen.fxml"); }

    private void loadScreen(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}