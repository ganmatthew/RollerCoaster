package com.csopesy.group1;

import java.sql.Time;
import java.util.Date;

class Car implements Runnable {
    int index;
    int capacity;
    private Monitor monitor;

    public Car(int index, int capacity, Monitor monitor) {
        this.index = index;
        this.capacity = capacity;
        this.monitor = monitor;
    }

    private void load(){
        System.out.println(new Time(new Date().getTime()) + ": Car " + index + " is loading passengers");
    }

    private void runCar(){
        System.out.println(new Time(new Date().getTime()) + ": car " + index + " is running.");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        unload();
    }

    private void unload(){
        System.out.println(new Time(new Date().getTime()) + ": car " + index + " is unloading passengers.");
        monitor.passengerUnboard(index);
    }

    @Override
    public void run() {
        while(!monitor.checkRemainingRides(index)){
            if(monitor.checkCarOrder(index)){
                load();
                if(!monitor.passengerBoard(index)){
                    runCar();
                }
            }
        }
    }
}
