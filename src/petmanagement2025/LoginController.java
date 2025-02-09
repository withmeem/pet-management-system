package petmanagement2025;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT user_id, username FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                loadDashboard();
            } else {
                messageLabel.setText("Invalid credentials");
            }
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
    
   
    
    @FXML
    private void switchToRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegisterScreen.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
    
    private void loadDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("DashboardScreen.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}