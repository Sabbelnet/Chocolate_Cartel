package server;

public class Start {

    public static int port = 8090;

    /**
     * entry point for jar (called by Main)
     * @param portArgument port to listen to
     */
    public static void start(int portArgument) {
        port = portArgument;

        /**
         * @apiNote start loginhandler Thread
         */

        LoginHandler loginHandler = new LoginHandler();

        /**
         * start 1 connector thread
         */
        new Connector(port, loginHandler);




    }
}
