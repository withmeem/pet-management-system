package petmanagement2025;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeletePetController {
    @FXML private TextField petIdField;
    @FXML private Label messageLabel;
    @FXML private TableView<Pet> petTable;
    @FXML private TableColumn<Pet, Integer> idColumn;
    @FXML private TableColumn<Pet, String> nameColumn;
    @FXML private TableColumn<Pet, String> typeColumn;
    @FXML private TableColumn<Pet, Integer> ageColumn;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        loadPets();
    }

    @FXML
    private void handleDeletePet() {
        try {
            int petId = Integer.parseInt(petIdField.getText());
            
            try (Connection conn = DBUtil.getConnection()) {
                String query = "DELETE FROM pets WHERE pet_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, petId);
                
                int affected = pst.executeUpdate();
                if (affected > 0) {
                    logAction("Deleted pet with ID: " + petId);
                    messageLabel.setText("Pet deleted successfully!");
                    loadPets();
                } else {
                    messageLabel.setText("Pet not found!");
                }
                petIdField.clear();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid ID");
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void loadPets() {
        ObservableList<Pet> pets = FXCollections.observableArrayList();
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM pets";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                pets.add(new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("age")
                ));
            }
        } catch (SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
        petTable.setItems(pets);
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

    @FXML private void showDashboard() { loadScreen("DashboardScreen.fxml"); }
    @FXML private void showAddPet() { loadScreen("AddPetScreen.fxml"); }
    @FXML private void showPetList() { loadScreen("PetListScreen.fxml"); }
    @FXML private void showDeletePet() { loadScreen("DeletePetScreen.fxml"); }
    @FXML private void showHistory() { loadScreen("HistoryScreen.fxml"); }
    @FXML private void handleLogout() { loadScreen("LoginScreen.fxml"); }

    private void loadScreen(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) petIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}