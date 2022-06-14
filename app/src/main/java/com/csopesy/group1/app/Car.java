package com.csopesy.group1.app;

import javafx.application.Platform;
import javafx.scene.Scene;

import java.sql.Time;
import java.util.Date;

class Car implements Runnable {
    int index;
    int capacity;
    int runTimeInSec;
    Controller  controller;
    Scene scene;
    private final Monitor monitor;

    public Car(int index, int capacity, int runTimeInSec, Monitor monitor, Controller controller, Scene scene) {
        this.index = index;
        this.capacity = capacity;
        this.runTimeInSec = runTimeInSec;
        this.monitor = monitor;
        this.controller = controller;
        this.scene= scene;
    }

    private void load(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is loading passengers");
    }

    private void runCar(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is running");
        try {
            Platform.runLater(()->controller.AddCarAndRun(scene));

            Thread.sleep(runTimeInSec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        unload();
    }

    private void unload(){
        System.out.println(new Time(new Date().getTime()) + "\tCar " + index + " is unloading passengers");
        monitor.passengerUnboard(index, this.scene);
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
