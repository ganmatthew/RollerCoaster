package com.csopesy.group1.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    final boolean USE_GRAPHICAL_INTERFACE = true;
    public final int WIDTH = 1000;
    public final int HEIGHT = 760;

    private final Controller controller = new Controller();

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("display.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 720);
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        stage.setResizable(false);
        if (USE_GRAPHICAL_INTERFACE) {
            controller.getInputFromView(scene);
        } else {
            controller.getInputFromCLI(scene);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}