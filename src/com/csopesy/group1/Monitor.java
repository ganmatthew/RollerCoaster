package com.csopesy.group1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Monitor {
    private int counter;                    // current number of passengers in queue
    private final int capacity;             // maximum capacity of each car
    private int carCounter = 0;             // index of current car in front of the line
    private int unboardedCounter = 0;       // current number of already unboarded passengers
    private int availableCars;              // current number of already unboarded passengers
    private final int numberOfCars;         // total number of cars
    private int resetCounter = 0;           // how many times all cars have finished one rotation in the track
    private final int numberOfPassengers;   // total number of passengers
    private boolean isDone = false;         // true when all possible rides are done
    private final ArrayList<Integer> queue = new ArrayList<>();                 // list of current passengers in queue
    private final ArrayList<Integer> boardedPassengers = new ArrayList<>();     // list of currently boarded passengers

    public Monitor(int counter, int capacity, int numberOfCars, int numberOfPassengers){
        this.counter = counter;
        this.capacity = capacity;
        this.numberOfCars = numberOfCars;
        this.availableCars = numberOfCars;
        this.numberOfPassengers = numberOfPassengers;
    }

    // increment: Is called everytime a passenger thread invoked board.
    public synchronized void increment(int index){
        // Increments the current number of passenger in queue.
        counter++;
        queue.add(index);
        // Checks if the current number of passenger is enough to fill all seats in a car.
        if (counter >= capacity){
            // Checks if there is an available car for passengers to board.
            if (availableCars > 0){
                notify();
            }
        }
    }

    // checkCarOrder: Is used to check whether it is the turn of a car to load passengers.
    public synchronized boolean checkCarOrder(int index){
        return index == carCounter; // returns true if the current car thread is the one to go. Returns false otherwise.
    }

    // passengerBoard: Is called everytime a car thread checks if the current number of passenger is enough to fill all seats.
    public synchronized boolean passengerBoard(){
        // Loop until the current car is full.
        while (!isCarFull() && !isDone){
            try {
                wait(); // Forces the thread to wait until a notify() is called.
                if(counter >= capacity){
                    for (int i = 0; i < capacity; i++){
                        System.out.println(new Time(new Date().getTime()) + "\tPassenger " + queue.get(0) + " has boarded Car " + carCounter);
                        boardedPassengers.add(queue.get(0));
                        queue.remove(0);
                    }
                    counter -= capacity;
                    availableCars--;
                }
                else{
                    return isDone;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return isDone; // If there is not enough passenger left to fill a car, then all rides is done. Stops all car threads.
    }

    // passengerUnboard: Is called everytime a car thread finishes one rotation in the track.
    public synchronized void passengerUnboard (int index){
        for (int i = 0; i < capacity; i++){
            System.out.println(new Time(new Date().getTime()) + "\tPassenger " + boardedPassengers.get(unboardedCounter) + " has unboarded from Car " + index);
            unboardedCounter++;
        }
        if (numberOfPassengers == unboardedCounter || (numberOfPassengers == unboardedCounter + queue.size() && queue.size() < capacity)){
            System.out.println("All rides completed");
            isDone = true;
            notifyAll();
        } else {
            availableCars++;
            checkAvailable();
        }

    }

    // checkRemainingRides: Is used to check if there is still enough passenger to fill a ride.
    public synchronized boolean checkRemainingRides(){
        return (numberOfPassengers == unboardedCounter) || ((numberOfPassengers == (unboardedCounter + queue.size())) && (queue.size() < capacity));
    }

    // isCarFull: Is used to check if a car is full of passenger and ready to go.
    private synchronized boolean isCarFull(){
        if (boardedPassengers.size() >= (capacity * (carCounter + 1 + (resetCounter * numberOfCars)))){
            carCounter++;
            if (carCounter == numberOfCars){
                resetCounter++;
                carCounter = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    // checkAvailable: Is used to check if there is an available car for a passenger to board.
    private synchronized void checkAvailable(){
        if (availableCars > 0 && counter >= capacity){
            notify();
        }
    }
}
