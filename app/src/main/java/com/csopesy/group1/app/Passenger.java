package com.csopesy.group1.app;

import javafx.scene.Scene;

import java.sql.Time;
import java.util.Date;

class Passenger implements Runnable {
    private final int index;
    private final int minRandTimeSec;
    private final int maxRandTimeSec;
    private final Monitor monitor;

    Scene scene;
    Controller controller;

    private int carCounter = 0;

    Passenger(int index, int minRandTimeSec, int maxRandTimeSec, Monitor monitor, Scene scene, Controller controller) {
        this.index = index;
        this.minRandTimeSec = minRandTimeSec;
        this.maxRandTimeSec = maxRandTimeSec;
        this.monitor = monitor;
        this.scene = scene;
        this.controller = controller;
    }

    private void board(){
        System.out.println(new Time(new Date().getTime()) + "\tPassenger " + index + " is in line for boarding");
        carCounter = monitor.increment(index);
        controller.updatePassQueue(scene, index, "add");
    }

    private void unboard(){
        if(monitor.unboard(index)){
            System.out.println(new Time(new Date().getTime()) + "\tPassenger " + index + " has unboarded Car " + carCounter);
            monitor.unboardSuccessful();
            controller.updatePassUnboard(scene,index);
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep((((int) (Math.random() * maxRandTimeSec) + minRandTimeSec) * 1000));
            board();
            unboard();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
