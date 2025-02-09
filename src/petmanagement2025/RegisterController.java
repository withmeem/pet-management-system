package petmanagement2025;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }
        
        if (password.isEmpty() || username.isEmpty()) {
            messageLabel.setText("Fields cant be empty.");
            return;
        }
        
        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            
            pst.executeUpdate();
            messageLabel.setText("Registration successful!");
            switchToLogin();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                messageLabel.setText("Username already exists");
            } else {
                messageLabel.setText("Error: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void switchToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}