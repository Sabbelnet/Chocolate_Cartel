package client;

import java.util.concurrent.ScheduledExecutorService;

public class PingSender implements Runnable {

    String ip;
    static ScheduledExecutorService scheduler;

    PingSender(String ip, ScheduledExecutorService scheduler) {
        this.ip = ip;
        PingSender.scheduler = scheduler;
    }

    /**
     * loop sending a ping every 2"
     * tests if Ping was answered by the server before sending the next Ping
     */
    @Override
    public void run() {

                if (Main.connectionHandler.answered) {
                    Main.connectionHandler.answered = false;
                    //switches to false so the if condition for the next round is only true if the PING was answered
                    String msg = "PING" + Main.connectionHandler.SEP + "test" + Main.connectionHandler.END;
                    Main.connectionHandler.sendMessage(msg);
                } else {
                    if (Main.connectionHandler.noAnswerCounter > 2) {
                        GUIGameController.getInstance().handleLostConnection();
                        scheduler.shutdown();
                    } else {
                        Main.connectionHandler.noAnswerCounter++;    //one more PING wasn't answered
                        Main.connectionHandler.answered = true;      //switches to true so another PING can be sent to check if the next one will be answered
                    }
                }
    }
}
