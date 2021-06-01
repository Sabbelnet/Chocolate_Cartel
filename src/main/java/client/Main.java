package client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * takes the ip and the username as arguments
 * starts the connectionHandler which opens a socket to communicate with the server
 * starts the gui, the pingsender and the commandhandler
 */
public class Main {

    static String ip;
    static String username;
    static String provUsername = "-";
    static int port;
    static ConnectionHandler connectionHandler;
    static CommandHandler commandHandler;
    static Thread guiThread;
    static ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        runMain(args);
    }

    public static void runMain(String[] args) {
        ip = args[0];
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("couldn't parse: " + args[1]);
        }

        if(args.length > 2) {
            provUsername = args[2];
        }

        /*
         * start connectionHandler thread
         */
        connectionHandler = new ConnectionHandler();

        guiThread = new Thread(new GUIStart());
        guiThread.start();


        /*
         * start pingSender thread
         */
        scheduler = Executors.newScheduledThreadPool(1);
        //start PingSender
        PingSender pingSender = new PingSender(args[0], scheduler);
        scheduler.scheduleAtFixedRate(pingSender, 3, 2, TimeUnit.SECONDS);

        /*
         * start commandHandler thread
         */
        commandHandler = new CommandHandler();
    }
}

