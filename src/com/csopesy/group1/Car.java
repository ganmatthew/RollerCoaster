package com.csopesy.group1;

import java.sql.Time;
import java.util.Date;

class Car implements Runnable {
    int index;
    int capacity;
    int runTimeInSec;
    private final Monitor monitor;

    public Car(int index, int capacity, int runTimeInSec, Monitor monitor) {
        this.index = index;
        this.capacity = capacity;
        this.runTimeInSec = runTimeInSec;
        this.monitor = monitor;
    }

    private void load(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is loading passengers");
    }

    private void runCar(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is running");
        try {
            Thread.sleep(runTimeInSec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        unload();
    }

    private void unload(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is unloading passengers");
        monitor.passengerUnboard(index);
    }

    @Override
    public void run() {
        while (!monitor.checkRemainingRides()){
            if (monitor.checkCarOrder(index)){
                load();
                if (!monitor.passengerBoard()){
                    runCar();
                }
            }
        }
    }
}
