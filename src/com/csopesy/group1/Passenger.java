package com.csopesy.group1;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

class Passenger implements Runnable {
    private int index;
    private Helper helper;

    Passenger(int index, Helper helper) {
        this.index = index;
        this.helper = helper;
    }

    private void board(){
        System.out.println(new Time(new Date().getTime()) + ": Passenger " + index + " boarded");
        synchronized (helper){
            helper.increment(index);
        }
    }

    private void unboard(){

    }

    @Override
    public void run() {
        try{
            Thread.sleep((((int) (Math.random() * 30) + 1) * 1000));
            board();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }

    }
}
