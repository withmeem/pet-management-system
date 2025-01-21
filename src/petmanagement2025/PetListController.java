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

public class PetListController {
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
            e.printStackTrace();
        }
        
        petTable.setItems(pets);
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
            Stage stage = (Stage) petTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}