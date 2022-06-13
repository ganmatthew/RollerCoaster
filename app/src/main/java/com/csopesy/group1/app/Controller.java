package com.csopesy.group1.app;

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