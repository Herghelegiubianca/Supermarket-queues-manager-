package org.example.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicBoolean running;
    private int id;
    private static int counter;
    private static int maxTasks;
    private int totalServiceTime;
    private int nrClientsServed;
    private AtomicInteger totalWaitingTime;

        public Server(int maxTasks) {
            // initialize queue and waiting period
            this.tasks = new LinkedBlockingQueue<>();
            this.waitingPeriod = new AtomicInteger(0);
            this.running=new AtomicBoolean(true);
            Server.counter++;
            this.id=Server.counter;
            Server.maxTasks=maxTasks;
            this.totalServiceTime=0;
            this.nrClientsServed=0;
            this.totalWaitingTime=new AtomicInteger(0);;
        }

    public BlockingQueue<Task> getT() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Server.counter = counter;
    }

    public static int getMaxTasks() {
        return maxTasks;
    }

    public static void setMaxTasks(int maxTasks) {
        Server.maxTasks = maxTasks;
    }

    public int getTotalServiceTime() {
        return totalServiceTime;
    }

    public void setTotalServiceTime(int totalServiceTime) {
        this.totalServiceTime = totalServiceTime;
    }

    public int getNrClientsServed() {
        return nrClientsServed;
    }

    public void setNrClientsServed(int nrClientsServed) {
        this.nrClientsServed = nrClientsServed;
    }

    public void setRunning(AtomicBoolean running) {
        this.running = running;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime.get();
    }

    public void setTotalWaitingTime(int totalWaitingTime) {
        this.totalWaitingTime.set(totalWaitingTime);
    }

    public void addTask(Task newTask) throws FullServerException{

            if(Server.maxTasks!=-1 && this.tasks.size()>=Server.maxTasks) //eu pun maxTasks=-1 cand nu vreau sa am limita
            {
                throw new FullServerException("Coada "+this.id+" nu mai poate accepta clienti!\n");
            }

            newTask.setWaitingTime(this.waitingPeriod.get());
            this.tasks.add(newTask);
            this.waitingPeriod.addAndGet(newTask.getServiceTime());
            this.totalWaitingTime.addAndGet(newTask.getWaitingTime());
        }

        public void run(){
        while (this.running.get()) {
            // take next task from queue
            // stop the thread for a time equal with the task’s processing time
            // decrement the waitingPeriod

            try {
                Task client = this.tasks.peek();
                if (client != null) {
                    for (int i = 0; i < client.getInitialServiceTime(); i++) {

                        Thread.sleep(1000L);
                        client.setServiceTime(client.getServiceTime() - 1);
                        if(this.waitingPeriod.get()>0) this.waitingPeriod.decrementAndGet();

                    }

                    this.nrClientsServed++;
                    this.totalServiceTime += client.getInitialServiceTime();

                    this.tasks.poll();
                }else {Thread.currentThread().sleep(50L);}
            }catch(Exception e)
            {
                Thread.currentThread().interrupt();
                break;
            }
          }
        }

        public Task[] getTasks() {
             Task [] t=new Task[this.tasks.size()];
             this.tasks.toArray(t);
             return t;
        }

    @Override
    public String toString() {
        String rez="Queue " + this.id+": ";
        if(this.tasks.size()==0)
        {
            rez=rez+"closed";
        }else
        {
            for(Task t: this.tasks)
            {
                rez=rez+t.toString();
            }
        }
        rez=rez+'\n';
        return rez;
    }
}
