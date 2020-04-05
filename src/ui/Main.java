package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/main.fxml")));
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Ocean Project");
        stage.setScene(scene);
        stage.show();
    }
}
