package com.csopesy.group1;

import java.sql.Time;
import java.util.Date;

class Passenger implements Runnable {
    private final int index;
    private final int minRandTimeSec;
    private final int maxRandTimeSec;
    private final Monitor monitor;
    private int carCounter = 0;

    Passenger(int index, int minRandTimeSec, int maxRandTimeSec, Monitor monitor) {
        this.index = index;
        this.minRandTimeSec = minRandTimeSec;
        this.maxRandTimeSec = maxRandTimeSec;
        this.monitor = monitor;
    }

    private void board(){
        System.out.println(new Time(new Date().getTime()) + "\tPassenger " + index + " is in line for boarding");
        carCounter = monitor.increment(index);
    }

    private void unboard(){
        if(monitor.unboard(index)){
            System.out.println(new Time(new Date().getTime()) + "\tPassenger " + index + " has unboarded Car " + carCounter);
            monitor.unboardSuccessful();
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
