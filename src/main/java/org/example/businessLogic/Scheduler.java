package org.example.businessLogic;
import org.example.model.FullServerException;
import org.example.model.SelectionPolicy;
import org.example.model.Server;
import org.example.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private ExecutorService executor;

    public Scheduler(int maxNoServers, int maxTasksPerServer, SelectionPolicy p) {
        this.servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;

        if (p == SelectionPolicy.SHORTEST_QUEUE) {
            this.strategy = new ShortestQueueStrategy();
        }
        if (p == SelectionPolicy.SHORTEST_TIME) {
            this.strategy = new ShortestTimeStrategy();
        }

        this.executor= Executors.newVirtualThreadPerTaskExecutor();
        for(int i=0;i<maxNoServers;i++) {
                 Server s=new Server(maxTasksPerServer);
                 this.servers.add(s);
                 executor.execute(s);
             }
    }

    public List<Server> getServers() {return servers;}
    public void setServers(List<Server> servers) {this.servers = servers;}
    public int getMaxNoServers() {return maxNoServers;}
    public void setMaxNoServers(int maxNoServers) {this.maxNoServers = maxNoServers;}
    public int getMaxTasksPerServer() {return maxTasksPerServer;}
    public void setMaxTasksPerServer(int maxTasksPerServer) {this.maxTasksPerServer = maxTasksPerServer;}
    public Strategy getStrategy() {return strategy;}
    public void setStrategy(Strategy strategy) {this.strategy = strategy;}

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ShortestTimeStrategy();
        }
    }

    public void dispatchTask(Task t) throws FullServerException {
            try {
                this.strategy.addTask(this.servers, t);
            }catch(FullServerException e)
            {
                throw e;
            }
    }

    public int findTaskServer(int taskId) {
            for(int i=0;i<this.servers.size();i++)
            {
                Task [] tasks=this.servers.get(i).getTasks();
                for(Task t: tasks)
                {
                    if(t.getId()==taskId) return i;
                }
            }
            return -1;
    }

    public void closeThreads() {
        if(this.executor!=null) {
            this.executor.shutdownNow();
        }
        for(Server s: this.servers)
        {
            s.setRunning(false);
            s.getT().clear();
        }
    }

}
