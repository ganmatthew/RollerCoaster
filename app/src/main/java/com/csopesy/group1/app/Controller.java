package com.csopesy.group1.app;

import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    int RUNTIME_IN_SEC = 10;
    int MIN_RANDOM_IN_SEC = 1;
    int MAX_RANDOM_IN_SEC = 20;

    int numberOfPassengers, numberOfCars, capacityOfCars;
    private final ArrayList<Passenger> passenger = new ArrayList<>();
    private final ArrayList<Car> car = new ArrayList<>();

    @FXML
    private TableView<TableString> passRoam, passQueue, numCars, passUnboard;

    @FXML
    private TableColumn<TableString, String> passRoamCol, passQueueCol, numCarsCol, passUnboardCol;


    @FXML
    Circle car1, car2;

    @FXML
    Button start, test;

    @FXML
    Pane TrackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        passRoamCol.setCellValueFactory(new PropertyValueFactory<TableString, String>("string"));

    }

    public void updatePassRoam(Scene scene, int n){
        TableView<TableString> passRoam = (TableView<TableString>) scene.lookup("#passRoam");

        ObservableList<TableString> tableStrings = passRoam.getItems();
        tableStrings.add(new TableString("Passenger " +n));
        passRoam.setItems(tableStrings);
    }


    public void AddCarAndRun(Scene scene){
        Circle circle = new Circle();
        circle.setRadius(16);
        Pane pane = (Pane) scene.lookup("#TrackPane");
        pane.getChildren().add(circle);
        circle.setFill(Color.DODGERBLUE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        circle.setLayoutX(266);
        circle.setLayoutY(294);
        runCar(circle);
    }
    
    public void runCar(Circle car) {
        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(new Double[]{
                0.0, 0.0,
                170.0, 1.0,
                220.0, -30.0,
                230.0, -70.0,
                230.0, -220.0,
                200.0, -260.0,
                160.0, -275.0,
                -80.0, -275.0,
                -120.0, -255.0,
                -140.0, -215.0,
                -140.0, -60.0,
                -120.0, -20.0,
                -70.0, 0.0,
                0.0, 0.0
        });
        PathTransition transition = new PathTransition();
        transition.setNode(car);
        transition.setDuration(Duration.seconds(10));
        transition.setPath(polyline);
        //transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();
    }

    void getFilenameFromView(Scene scene) {
        Button startButton = (Button) scene.lookup("#start");
        TextField inputField = (TextField) scene.lookup("#inputField");
        startButton.setOnAction(e -> {
            inputField.setEditable(false);
            startButton.setDisable(true);
            startButton.setText("Running...");
            readInputTextFile(scene, inputField.getText());
        });
    }

    void getFilenameFromCLI(Scene scene) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter input file name: ");
        String textFile = scan.nextLine();
        readInputTextFile(scene, textFile);
    }

    void readInputTextFile(Scene scene, String textFile) {
        File file = new File(textFile);
        try {
            Scanner fileScanner = new Scanner(file);
            if (file.exists()){
                String[] inputs = new String[0];
                while(fileScanner.hasNextLine()){
                    inputs = fileScanner.nextLine().split(" ");
                }
                numberOfPassengers = Integer.parseInt(inputs[0]);
                capacityOfCars = Integer.parseInt(inputs[1]);
                numberOfCars = Integer.parseInt(inputs[2]);
                runRollerCoaster(scene);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void runRollerCoaster(Scene scene) {
        Monitor monitor = new Monitor(0, capacityOfCars, numberOfCars, numberOfPassengers);

        for (int i = 0; i < numberOfPassengers; i++){
            passenger.add(new Passenger(i, MIN_RANDOM_IN_SEC, MAX_RANDOM_IN_SEC, monitor, scene, this));
            Thread thread = new Thread(passenger.get(i), Integer.toString(i));
            thread.start();
        }

        for (int i = 0; i < numberOfCars; i++){
            car.add(new Car(i, capacityOfCars, RUNTIME_IN_SEC, monitor, this, scene));
            Thread thread = new Thread(car.get(i), Integer.toString(i));
            thread.start();
        }
    }


}