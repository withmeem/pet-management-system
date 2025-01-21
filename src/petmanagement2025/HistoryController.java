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

public class HistoryController {
    @FXML private TableView<HistoryEntry> historyTable;
    @FXML private TableColumn<HistoryEntry, String> timeColumn;
    @FXML private TableColumn<HistoryEntry, String> actionColumn;

    public void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        loadHistory();
    }

    private void loadHistory() {
        ObservableList<HistoryEntry> history = FXCollections.observableArrayList();
        
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT timestamp, action FROM history_log ORDER BY timestamp DESC";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                history.add(new HistoryEntry(
                    rs.getTimestamp("timestamp").toString(),
                    rs.getString("action")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        historyTable.setItems(history);
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
            Stage stage = (Stage) historyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}