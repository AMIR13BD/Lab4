package labapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WelcomeController {
    @FXML private Label welcomeLabel;

    public void init(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    void onClose() {
        Stage s = (Stage) welcomeLabel.getScene().getWindow();
        s.close();
    }
}
