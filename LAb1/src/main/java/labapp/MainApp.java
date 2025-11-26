package labapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static int maxAttempts;   // n
    private static int blockSeconds;  // t

    public static void main(String[] args) {

        if (args.length >= 2) {
            maxAttempts  = Integer.parseInt(args[0]);
            blockSeconds = Integer.parseInt(args[1]);
        } else {
            maxAttempts  = 3;
            blockSeconds = 10;
        }

        launch(args);
    }

    public static int getMaxAttempts() {
        return maxAttempts;
    }

    public static int getBlockSeconds() {
        return blockSeconds;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
