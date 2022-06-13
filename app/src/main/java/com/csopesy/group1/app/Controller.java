package com.csopesy.group1.app;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    static int RUNTIME_IN_SEC = 10;
    static int MIN_RANDOM_IN_SEC = 1;
    static int MAX_RANDOM_IN_SEC = 20;

    static int numberOfPassengers, numberOfCars, capacityOfCars;
    static private final ArrayList<Passenger> passenger = new ArrayList<>();
    static private final ArrayList<Car> car = new ArrayList<>();

    @FXML
    Circle car1, car2;

    @FXML
    Button start;

    public void initialize(){
        runCar();
    }
    
    public void runCar() {
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
        transition.setNode(car1);
        transition.setDuration(Duration.seconds(10));
        transition.setPath(polyline);
        //transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();
    }

    void getInputFromTextFile() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter input file name: ");
        String textFile = scan.nextLine();
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
                Monitor monitor = new Monitor(0, capacityOfCars, numberOfCars, numberOfPassengers);
                //Thread[] carThread = new Thread[numberOfCars];
                //Thread[] passThread = new Thread[numberOfPassengers];

                for (int i = 0; i < numberOfPassengers; i++){
                    passenger.add(new Passenger(i, MIN_RANDOM_IN_SEC, MAX_RANDOM_IN_SEC, monitor));
                    Thread thread = new Thread(passenger.get(i), Integer.toString(i));
                    thread.start();
                }

                for (int i = 0; i < numberOfCars; i++){
                    car.add(new Car(i, capacityOfCars, RUNTIME_IN_SEC, monitor));
                    Thread thread = new Thread(car.get(i), Integer.toString(i));
                    thread.start();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}