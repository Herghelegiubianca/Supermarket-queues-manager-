package org.example.businessLogic;

import org.example.model.FullServerException;
import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public final class ShortestQueueStrategy implements Strategy {
    //nu am atribute

    public void addTask(List<Server> servers, Task t) throws FullServerException {
        Server aux = servers.get(0);
        int size = servers.get(0).getT().size();
        for (Server s : servers) {
            if (size > s.getT().size()) {
                aux = s;
                size = s.getT().size();
            }
        }
        try {
            aux.addTask(t);
        } catch (FullServerException e) {
            throw e;
        }
    }

}