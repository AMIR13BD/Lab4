package labapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // ***** must match login.fxml *****
    @FXML
    private TextField username;        // fx:id="username"

    @FXML
    private PasswordField password;    // fx:id="password"

    @FXML
    private Label errorLabel;          // fx:id="errorLabel"
    // *********************************

    private final UsersRepo usersRepo = new UsersRepo();

    private final LoginState loginState =
            new LoginState(MainApp.getMaxAttempts(), MainApp.getBlockSeconds());

    private final BlockWatcher blockWatcher =
            new BlockWatcher(loginState);

    private boolean countdownRunning = false;

    @FXML
    public void initialize() {
        blockWatcher.start();
    }

    @FXML
    private void onLogin() {

        if (loginState.isBlocked()) {
            startCountdownIfNeeded();
            return;
        }

        String user = username.getText();
        String pwd  = password.getText();

        boolean ok = usersRepo.checkUser(user, pwd);

        if (ok) {
            loginState.registerSuccess();
            errorLabel.setText("");  // clear error
            openWelcomeScreen(user);
        } else {
            // 3. wrong details – update attempts using its own thread
            AttemptsUpdater updater = new AttemptsUpdater(loginState);
            updater.start();
            try {
                updater.join(); // wait until attempts have been updated
            } catch (InterruptedException ignored) { }

            int remaining = updater.getRemainingAfter();

            if (remaining > 0) {
                // still have attempts left
                errorLabel.setText("פרטים שגויים. נותרו " + remaining + " ניסיונות.");
            } else {
                // user just got blocked NOW (no attempts left)
                errorLabel.setText("אין יותר ניסיונות. המשתמש יחסם למשך " +
                        loginState.getBlockSeconds() + " שניות.");
                startCountdownIfNeeded();   // start countdown immediately
            }
        }
    }

    // starts the 10→1 countdown on the label (only once per block)
    private void startCountdownIfNeeded() {
        if (countdownRunning) return;

        countdownRunning = true;

        new Thread(() -> {
            int t = loginState.getBlockSeconds();
            for (int i = t; i >= 1; i--) {
                int secondsLeft = i;
                Platform.runLater(() ->
                        errorLabel.setText("המשתמש חסום. נסה/י שוב בעוד " +
                                secondsLeft + " שניות.")
                );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
            }

            countdownRunning = false;


            Platform.runLater(() ->
                    errorLabel.setText("הכנס שם משתמש וסיסמה")
            );
        }).start();
    }

    private void openWelcomeScreen(String user) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(MainApp.class.getResource("/welcome.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Welcome");
                stage.setScene(scene);



                stage.show();

                username.getScene().getWindow().hide();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
