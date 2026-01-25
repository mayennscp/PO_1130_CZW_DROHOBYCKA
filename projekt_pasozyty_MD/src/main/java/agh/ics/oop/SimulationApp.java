package agh.ics.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/config.fxml"));
        ScrollPane root = loader.load();

        primaryStage.setTitle("Darwin World");
        primaryStage.setScene(new Scene(root, 600, 800));
        primaryStage.show();
    }
}