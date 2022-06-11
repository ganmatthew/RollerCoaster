package com.csopesy.group1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Monitor {
    private int counter;
    private ArrayList<Integer> queue = new ArrayList<>();;
    private ArrayList<Integer> boardedPassengers = new ArrayList<>();
    private int capacity;
    private int carCounter = 0;
    private int unboardedCounter = 0;
    private int availableCars;
    private int numberOfCars;
    private int resetCounter = 0;
    private int numberOfPassengers;
    private boolean isDone = false;

    public Monitor(int counter, int capacity, int numberOfCars, int numberOfPassengers){
        this.counter = counter;
        this.capacity = capacity;
        this.numberOfCars = numberOfCars;
        availableCars = numberOfCars;
        this.numberOfPassengers = numberOfPassengers;
    }

    public synchronized void increment(int index){
        counter++;
        queue.add(index);
        if(counter >= capacity){
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

    public synchronized boolean checkCarOrder(int index){
        if(index == carCounter){
            return true;
        }
        else{
            return false;
        }
    }

    public synchronized boolean passengerBoard(int index){
        while(!isCarFull() && !isDone){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(isDone){
            return true;
        }
        else{
            return false;
        }
    }

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

    public synchronized boolean checkRemainingRides(int index){
        if(numberOfPassengers == unboardedCounter || (numberOfPassengers == unboardedCounter + queue.size() && queue.size() < capacity)){
            return true;
        }
        else{
            return false;
        }
    }

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
