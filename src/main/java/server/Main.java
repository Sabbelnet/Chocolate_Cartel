package server;


import java.io.IOException;

public class Main {

    private static final int DEFAULT_PORT = 8090;
    private static int port;

    public Main (String port) {
        Main.port = Integer.parseInt(port);
    }

    /**
     * Main Method to start
     * @param args start arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else port = DEFAULT_PORT;
        /**
         * create loginhandler object
         */

        LoginHandler loginHandler = new LoginHandler();

        /**
         * start 1 connector thread
         *
         */
        new Connector(port, loginHandler);
    }
}
