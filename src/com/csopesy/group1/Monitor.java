package com.csopesy.group1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Monitor {
    /*
            counter: used for storing the current number of passengers in queue.
            capacity: used for storing the maximum capacity of each cars.
            carCounter: used for storing the current car in front of the line.
            unboardedCounter: used for storing the current number of already unboarded passengers.
            numberOfCars: used for storing the total number of cars.
            resetCounter: used for storing how many times all cars have finished one rotation in the track.
            numberOfPassengers: used for storing the tatal number of passengers.

            isDone: used for checking whether all possible rides is done.

            queue: used for storing the list of current passengers in queue.
            boardedPassengers: used for storing the list of the current boarded passengers.
     */
    private int counter, capacity, carCounter = 0, unboardedCounter = 0, availableCars, numberOfCars, resetCounter = 0, numberOfPassengers;
    private boolean isDone = false;
    private ArrayList<Integer> queue = new ArrayList<>();;
    private ArrayList<Integer> boardedPassengers = new ArrayList<>();

    public Monitor(int counter, int capacity, int numberOfCars, int numberOfPassengers){
        this.counter = counter;
        this.capacity = capacity;
        this.numberOfCars = numberOfCars;
        availableCars = numberOfCars;
        this.numberOfPassengers = numberOfPassengers;
    }

    // increment: Is called everytime a passenger thead invoked board.
    public synchronized void increment(int index){
        // Increments the current number of passenger in queue.
        counter++;
        queue.add(index);
        // Checks if the current number of passenger is enough to fill all seats in a car.
        if(counter >= capacity){
            // Checks if there is an available car for passengers to board.
            if(availableCars > 0){
                for(int i = 0; i < capacity; i++){
                    System.out.println(new Time(new Date().getTime()) + ": Passenger " + queue.get(0) + " have boarded car " + carCounter);
                    boardedPassengers.add(queue.get(0));
                    queue.remove(0);
                }
                counter -= capacity;
                availableCars--;
                notify();
            }
        }
    }


    // checkCarOrder: Is used to check whether it is the turn of a car to load passengers.
    public synchronized boolean checkCarOrder(int index){
        if(index == carCounter){
            return true; // returns true if the current car thread is the one to go. Returns false otherwise.
        }
        else{
            return false;
        }
    }

    // passengerBoard: Is called everytime a car thread checks if the current number of passenger is enough to fill all seats.
    public synchronized boolean passengerBoard(int index){
        // Loop until the current car is full.
        while(!isCarFull() && !isDone){
            try {
                wait(); // Forces the thread to wait until a notify() is called.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(isDone){
            return true; // If there is not enough passenger left to fill a car, then all rides is done. Stops all car threads.
        }
        else{
            return false;
        }
    }

    // passengerUnboard: Is called everytime a car thread finishes one rotation in the track.
    public synchronized void passengerUnboard (int index){
        for(int i = 0; i < capacity; i++){
            System.out.println(new Time(new Date().getTime()) + ": Passenger " + boardedPassengers.get(unboardedCounter) + " have unboarded car " + index);
            unboardedCounter++;
        }
        if(numberOfPassengers == unboardedCounter || (numberOfPassengers == unboardedCounter + queue.size() && queue.size() < capacity)){
            System.out.println("All rides completed");
            isDone = true;
            notifyAll();
        }
        else{
            availableCars++;
            checkAvailable();
        }

    }

    // checkRemainingRides: Is used to check if there is still enough passenger to fill a ride.
    public synchronized boolean checkRemainingRides(int index){
        if(numberOfPassengers == unboardedCounter || (numberOfPassengers == unboardedCounter + queue.size() && queue.size() < capacity)){
            return true;
        }
        else{
            return false;
        }
    }

    // isCarFull: Is used to check if a car is full of passenger and ready to go.
    private synchronized boolean isCarFull(){
        if(boardedPassengers.size() >= (capacity * (carCounter + 1 + (resetCounter * numberOfCars)))){
            carCounter++;
            if(carCounter == numberOfCars){
                resetCounter++;
                carCounter = 0;
            }
            return true;
        }
        else{
            return false;
        }
    }

    // checkAvailable: Is used to check if there is an available car for a passenger to board.
    private synchronized void checkAvailable(){
        if(availableCars > 0 && counter >= capacity){
            for(int i = 0; i < capacity; i++){
                System.out.println(new Time(new Date().getTime()) + ": Passenger " + queue.get(0) + " have boarded car " + carCounter);
                boardedPassengers.add(queue.get(0));
                queue.remove(0);
            }
            counter -= capacity;
            availableCars--;
            notify();
        }
    }
}
