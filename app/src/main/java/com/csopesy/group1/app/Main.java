package com.csopesy.group1.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private final Controller controller = new Controller();
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 760;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("display.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1009, 606);
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        stage.setResizable(false);
        //controller.getInputFromTextFile();
    }

    public static void main(String[] args) {
        launch();
    }
}