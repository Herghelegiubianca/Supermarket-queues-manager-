package org.example.model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime;
    private int initialServiceTime;
    private static int counter;

    public Task() {}

    public Task(int arrivalTime, int serviceTime)
    {
           Task.counter++;
           this.id=Task.counter;
           this.arrivalTime=arrivalTime;
           this.serviceTime=serviceTime;
           this.initialServiceTime=serviceTime;
           this.waitingTime=0;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Task.counter = counter;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

    public void setInitialServiceTime(int initialServiceTime) {
        this.initialServiceTime = initialServiceTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    @Override
    public String toString() {
        return "(" +
                this.id +
                ", " + arrivalTime +
                ", " + serviceTime +
                "),  ";
    }
}