package com.csopesy.group1;

import java.util.ArrayList;

public class Helper {
    private int counter;
    private ArrayList<Integer> queue = new ArrayList<>();;

    public Helper(int counter){
        this.counter = counter;
    }

    public void increment(int index){
        counter++;
        queue.add(index);
//        System.out.println("Passenger: " + queue.get(counter - 1) + " have boarded");
    }
}
