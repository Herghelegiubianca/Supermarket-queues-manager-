package org.example.businessLogic;

import org.example.model.FullServerException;
import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, SelectionPolicy p) {
        // for maxNoServers
        // - create server object
        // - create thread with the object
        this.servers=new ArrayList<>();
        this.maxNoServers=maxNoServers;
        this.maxTasksPerServer=maxTasksPerServer;

        if (p==SelectionPolicy.SHORTEST_QUEUE) {
            this.strategy = new ShortestQueueStrategy();
        }
        if (p==SelectionPolicy.SHORTEST_TIME) {
            this.strategy = new ShortestTimeStrategy();
        }

        for (int i = 0; i < maxNoServers; i++)
            {
                Server s=new Server(maxTasksPerServer);
                this.servers.add(s);
                Thread t= new Thread(s);
                t.start();
            }
        }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public void setMaxNoServers(int maxNoServers) {
        this.maxNoServers = maxNoServers;
    }

    public int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void changeStrategy(SelectionPolicy policy) {
        // apply strategy pattern in order to instantiate the strategy with the concrete strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
            }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ShortestTimeStrategy();
            }
        }

        public void dispatchTask(Task t) throws FullServerException {
        // call the strategy addTask method
            try
            {
                this.strategy.addTask(this.servers, t);
            }catch(FullServerException e)
            {
                throw e;
            }
        }

        public int findTaskServer(int taskId)
        {
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

}
