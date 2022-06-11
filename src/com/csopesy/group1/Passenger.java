package com.csopesy.group1;

import java.sql.Time;
import java.util.Date;

class Passenger implements Runnable {
    private int index;
    private Monitor monitor;

    Passenger(int index, Monitor monitor) {
        this.index = index;
        this.monitor = monitor;
    }

    private void board(){
        System.out.println(new Time(new Date().getTime()) + ": Passenger " + index + " got in line for boarding.");
        synchronized (monitor){
            monitor.increment(index);
        }
    }

    private void unboard(){

    }

    @Override
    public void run() {
        try{
            Thread.sleep((((int) (Math.random() * 20) + 1) * 1000));
            board();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }

    }
}
