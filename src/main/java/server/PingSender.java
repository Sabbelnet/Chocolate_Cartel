package server;

import java.util.concurrent.CopyOnWriteArrayList;

public class PingSender implements Runnable {

    CopyOnWriteArrayList<ConnectionHandler> threadList;


    /**
     * Constructor, takes the list of all threads as argument
     * @param threadList list of all connectionhandler threads
     */
    PingSender(CopyOnWriteArrayList<ConnectionHandler> threadList) {

        this.threadList = threadList;
    }

    @Override
    public void run() {
        if (!threadList.isEmpty()) {
            for (ConnectionHandler connectionHandler : threadList) {
                connectionHandler.sendPing();
            }
        }
    }
}
