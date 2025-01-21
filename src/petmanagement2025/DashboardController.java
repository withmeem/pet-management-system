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

public class DashboardController {
    @FXML private Label totalPetsLabel;
    @FXML private Label petTypesLabel;
    @FXML private Label maxAgeLabel;
    @FXML private TableView<HistoryEntry> recentActivityTable;
    @FXML private TableColumn<HistoryEntry, String> timeColumn;
    @FXML private TableColumn<HistoryEntry, String> actionColumn;

    public void initialize() {
        updateDashboardStats();
        setupRecentActivityTable();
    }

    private void updateDashboardStats() {
        try (Connection conn = DBUtil.getConnection()) {
            // Total Pets
            String countQuery = "SELECT COUNT(*) FROM pets";
            PreparedStatement pst = conn.prepareStatement(countQuery);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalPetsLabel.setText(String.valueOf(rs.getInt(1)));
            }

            // Pet Types
            String typesQuery = "SELECT COUNT(DISTINCT type) FROM pets";
            pst = conn.prepareStatement(typesQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                petTypesLabel.setText(String.valueOf(rs.getInt(1)));
            }

            // Max Age
            String maxAgeQuery = "SELECT MAX(age) FROM pets";
            pst = conn.prepareStatement(maxAgeQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                maxAgeLabel.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupRecentActivityTable() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        
        loadRecentActivity();
    }

    private void loadRecentActivity() {
        ObservableList<HistoryEntry> activities = FXCollections.observableArrayList();
        
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT h.timestamp, h.action FROM history_log h ORDER BY h.timestamp DESC LIMIT 5";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                activities.add(new HistoryEntry(
                    rs.getTimestamp("timestamp").toString(),
                    rs.getString("action")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        recentActivityTable.setItems(activities);
    }

    @FXML
    private void showAddPet() {
        loadScreen("AddPetScreen.fxml");
    }

    @FXML
    private void showPetList() {
        loadScreen("PetListScreen.fxml");
    }

    @FXML
    private void showDeletePet() {
        loadScreen("DeletePetScreen.fxml");
    }

    @FXML
    private void showHistory() {
        loadScreen("HistoryScreen.fxml");
    }

    @FXML
    private void handleLogout() {
        loadScreen("LoginScreen.fxml");
    }

    @FXML
    private void showDashboard() {
        updateDashboardStats();
        loadRecentActivity();
    }

    private void loadScreen(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) totalPetsLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}