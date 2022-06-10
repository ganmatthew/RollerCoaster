package com.csopesy.group1;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

class Main {
    static int numberOfPassengers;
    static int numberOfCars;
    static int capacityOfCars;
    static private ArrayList<Passenger> passenger = new ArrayList<>();
    static private ArrayList<Car> car = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Text file input: ");
        String textFile = scan.nextLine();
        File file = new File(textFile);
        try {
            Scanner fileScanner = new Scanner(file);
            if(file.exists()){
                String[] inputs = new String[0];
                while(fileScanner.hasNextLine()){
                    inputs = fileScanner.nextLine().split(" ");
                }
                numberOfPassengers = Integer.parseInt(inputs[0]);
                capacityOfCars = Integer.parseInt(inputs[1]);
                numberOfCars = Integer.parseInt(inputs[2]);
                Helper helper = new Helper(0);

                for(int i = 0; i < numberOfPassengers; i++){
                    passenger.add(new Passenger(i, helper));
                    Thread thread = new Thread(passenger.get(i), Integer.toString(i));
                    thread.start();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Number of Passengers: " + numberOfPassengers + " Capacity of Cars: " + capacityOfCars + " Number of Cars: " + numberOfCars);
    }
}
