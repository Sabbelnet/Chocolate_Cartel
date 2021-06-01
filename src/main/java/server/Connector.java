package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Connector extends Thread {


    private int port;
    ServerSocket server;
    LoginHandler loginHandler;
    CopyOnWriteArrayList<ConnectionHandler> threadList;
    public static CopyOnWriteArrayList<String> highscoreList;
    Hashtable<String, GameInstance> gameList1;
    ArrayList<GameInstance> gameList;
    int playerCount;
    private boolean isOnline;


    /**
     * private static final Object LOCK = new Object();
     * @param loginHandler loginHandler Object, player list is stored here
     * @param port listening port of the server
     */
    public Connector(int port, LoginHandler loginHandler)  {

        start();
        this.port = port;
        this.loginHandler = loginHandler;
        threadList = new CopyOnWriteArrayList<>();
        gameList1 = new Hashtable<>();
        gameList = new ArrayList<>();
        highscoreList = new CopyOnWriteArrayList<>();
        isOnline = true;
    }

    @Override
    public void run() {

        new PingSender(threadList);

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Started server on port %s. Can be  changed by passing the port as program argument%n", port);
        System.out.println("listening...");

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        PingSender pingSender = new PingSender(threadList);
        scheduler.scheduleAtFixedRate(pingSender, 10, 5, TimeUnit.SECONDS);

        /**
         * infinity loop to accept clients
         * -create new connectionHandler, accept connection and give loginHandler object
         * -add to threadlist
         * -execute new thread
         */
            while (isOnline) {
                if (playerCount > 200) {
                    try {
                        Thread.sleep(2000);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ConnectionHandler connectionHandler = null;
                try {
                    connectionHandler = new ConnectionHandler(server.accept(), loginHandler, threadList, gameList1);
                    Thread.sleep(1);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                threadList.add(connectionHandler);
            }
    }
}
