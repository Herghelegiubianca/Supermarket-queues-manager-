package org.example.businessLogic;

import org.example.model.FullServerException;
import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public final class ShortestTimeStrategy implements Strategy{
    //nu am atribute

    public void addTask(List<Server> servers, Task t) throws FullServerException
    {
        Server aux=servers.get(0);
        int waitingTime=servers.get(0).getWaitingPeriod();
        for(Server s: servers)
        {
            if(waitingTime>s.getWaitingPeriod())
            {
                aux=s;
                waitingTime=s.getWaitingPeriod();
            }
        }

        try {
            aux.addTask(t);
        } catch (FullServerException e) {
            throw e;
        }
    }
}
