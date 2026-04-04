package org.example.businessLogic;
import org.example.gui.SimulationFrame;
import org.example.model.Server;
import org.example.model.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable{
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int maxServiceTime;
    private int minServiceTime;
    private int numberOfServers;
    private int numberOfClients;
    private int nrOfClientsInLine;
    public SelectionPolicy selectionPolicy;
    private boolean running;
    private int time;
    private double averageWaitingTime;
    private double averageServiceTime;
    private int peakTime;
    private int peakTimeClients;
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;

    public SimulationManager() {
        Task.setCounter(0);
        Server.setCounter(0);
        this.nrOfClientsInLine=0;
        this.averageServiceTime=-1;
        this.averageWaitingTime=-1;
        this.peakTimeClients=-1;
        this.peakTime=-1;
        this.frame=new SimulationFrame();
        this.frame.addStartActionListener(new StartActionListener());
        this.time=0;
        this.running=false;
        this.generatedTasks=Collections.synchronizedList(new ArrayList<Task>());
        }

    public SimulationFrame getFrame() {return frame;}
    public void setFrame(SimulationFrame frame) {this.frame = frame;}
    public int getPeakTimeClients() {return peakTimeClients;}
    public void setPeakTimeClients(int peakTimeClients) {this.peakTimeClients = peakTimeClients;}
    public int getPeakTime() {return peakTime;}
    public void setPeakTime(int peakTime) {this.peakTime = peakTime;}
    public double getAverageServiceTime() {return averageServiceTime;}
    public void setAverageServiceTime(double averageServiceTime) {this.averageServiceTime = averageServiceTime;}
    public double getAverageWaitingTime() {return averageWaitingTime;}
    public void setAverageWaitingTime(double averageWaitingTime) {this.averageWaitingTime = averageWaitingTime;}
    public int getTime() {return time;}
    public void setTime(int time) {this.time = time;}
    public int getTimeLimit() {return timeLimit;}
    public void setTimeLimit(int timeLimit) {this.timeLimit = timeLimit;}
    public int getMaxProcessingTime() {return maxProcessingTime;}
    public void setMaxProcessingTime(int maxProcessingTime) {this.maxProcessingTime = maxProcessingTime;}
    public int getMaxServiceTime() {return maxServiceTime;}
    public void setMaxServiceTime(int maxServiceTime) {this.maxServiceTime = maxServiceTime;}
    public int getMinServiceTime() {return minServiceTime;}
    public void setMinServiceTime(int minServiceTime) {this.minServiceTime = minServiceTime;}
    public int getMinProcessingTime() {return minProcessingTime;}
    public void setMinProcessingTime(int minProcessingTime) {this.minProcessingTime = minProcessingTime;}
    public int getNumberOfServers() {return numberOfServers;}
    public void setNumberOfServers(int numberOfServers) {this.numberOfServers = numberOfServers;}
    public int getNumberOfClients() {return numberOfClients;}
    public void setNumberOfClients(int numberOfClients) {this.numberOfClients = numberOfClients;}
    public SelectionPolicy getSelectionPolicy() {return selectionPolicy;}
    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {this.selectionPolicy = selectionPolicy;}
    public boolean isRunning() {return running;}
    public void setRunning(boolean running) {this.running = running;}
    public Scheduler getScheduler() {return scheduler;}
    public void setScheduler(Scheduler scheduler) {this.scheduler = scheduler;}
    public List<Task> getGeneratedTasks() {return generatedTasks;}
    public void setGeneratedTasks(List<Task> generatedTasks) {this.generatedTasks = generatedTasks;}

    private void generateNRandomTasks() {
        for(int i=0;i<this.numberOfClients;i++)
        {
            int arrivalTime=(int)(Math.random()*(this.maxProcessingTime-this.minProcessingTime+1)+this.minProcessingTime);
            int serviceTime=(int)(Math.random()*(this.maxServiceTime-this.minServiceTime+1)+this.minServiceTime);
            Task t=new Task(arrivalTime,serviceTime);
            this.generatedTasks.add(t);
        }
        Collections.sort(this.generatedTasks, new Comparator<Task>(){
            @Override
            public int compare(Task o1, Task o2) {
                if(o1.getArrivalTime()<o2.getArrivalTime()) return -1;
                else if(o1.getArrivalTime()>o2.getArrivalTime()) return 1;
                else {
                    if(o1.getId()<o2.getId()) return -1;
                    else return 1; //nu pot avea acelasi id pt ca le generez eu
                }}});}

    @Override
    public void run() {
        java.io.PrintWriter w=null;
        try{  w = new java.io.PrintWriter("LIDL.txt");
              while(this.running && this.time<=this.timeLimit && this.stillServing())
              {
                    Iterator<Task> it=this.generatedTasks.iterator();
                    while(it.hasNext())
                    {
                       Task aux=it.next();
                       if(aux.getArrivalTime()<=this.time)
                       {try{
                               this.scheduler.dispatchTask(aux);
                               this.nrOfClientsInLine++;
                               int index=this.scheduler.findTaskServer(aux.getId());
                               if(index!=-1) this.frame.addClient(aux.getId(),index);
                               it.remove();
                           }catch(Exception e)
                           {System.out.println(e.getMessage());}
                       }else break;
                    }
                    this.analyse();
                    Thread.currentThread().sleep(20L);
                    this.writeInFile(w);
                    w.flush();

                    for(Server s: this.scheduler.getServers())
                    {
                        this.frame.eraseClient(s.getId()-1,s.getT().size());
                    }

                    this.time++;
                    try{Thread.sleep(1000L);}
                    catch(Exception e)
                    {Thread.currentThread().interrupt();break;}}
                  this.closeThreads();
                  int wt=0,st=0,nr=0;
                  for(Server s: this.scheduler.getServers())
                  {
                      wt+=s.getTotalWaitingTime();
                      st+=s.getTotalServiceTime();
                      nr+=s.getNrClientsServed();
                  }
                  if(nr!=0)
                  {
                      this.averageWaitingTime=(double)wt/this.nrOfClientsInLine;
                      this.averageServiceTime=(double)st/nr;
                  }

                  this.printConclusions(w);
              } catch(Exception e) {System.out.println(e.getMessage());}
              finally {w.close();}
          }

    public void analyse()
    {
        int nr=0;
        for(Server s: this.scheduler.getServers())
        {
            nr+=s.getT().size();
        }
        if(nr>this.peakTimeClients){ this.peakTimeClients=nr; this.peakTime=this.time;}
    }

    public void writeInFile(java.io.PrintWriter w)
    {
            w.println("Time: "+this.time);
            w.println("Waiting Clients:");
            for(Task t: this.generatedTasks)
            {
                w.print(t);
            }
            w.print('\n');
            for(Server s: this.scheduler.getServers())
            {
                w.print(s);
            }
            w.println();
            w.println();
    }

    public void printConclusions(java.io.PrintWriter w)
    {
            if(this.averageServiceTime==-1)
            {
                w.println("Nu s-a servit niciun client! Nu se pot calcula datele cerute!");
            }
            w.println("Average waiting time: "+this.averageWaitingTime);
            w.println("Average service time: "+this.averageServiceTime);
            w.println("Peak time: "+this.peakTime);

    }

    public boolean stillServing()
    {
        if(this.generatedTasks.isEmpty())
        {
        for(Server s: this.scheduler.getServers())
        {
            if(!s.getT().isEmpty()) {return true;}
        }
        return false;}
        return true;
    }

    public void closeThreads()
    {
        for(Server s: this.scheduler.getServers())
        {
            s.setRunning(false);
            s.getT().clear();
        }
    }

    private class StartActionListener implements ActionListener{
        public StartActionListener(){}

        @Override
        public void actionPerformed(ActionEvent e) {

               timeLimit=frame.getTimeLimit();
               numberOfClients=frame.getNrC();
               numberOfServers=frame.getNrQ();
               minProcessingTime=frame.getMinTime();
               maxProcessingTime=frame.getMaxTime();
               minServiceTime=frame.getMinS();
               maxServiceTime=frame.getMaxS();
               selectionPolicy=frame.getSelectedStrategy();
               if(minProcessingTime>maxProcessingTime || minServiceTime>maxServiceTime) {
                   frame.displayError("Argumente invalide!");
                   return;
               }
               scheduler=new Scheduler(numberOfServers,-1,selectionPolicy);
               generateNRandomTasks();
               frame.startAnimation();
               running=true;
               Thread t=new Thread(SimulationManager.this);
               t.start();
        }
    }
}

