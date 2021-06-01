package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Timer that calls all monthly events in a game
 */
public class GameTimer implements Runnable {

    private int i;
    private final int endGameCondition;
    private final int gameLength;
    private final ScheduledExecutorService scheduler;
    private final GameInstance gameInstance;
    private static final Logger logger = LogManager.getLogger(GameInstance.class);

    /**
     *
     * @param gameLength1 length of a game in months
     * @param scheduler own scheduler to shutdown
     * @param gameInstance gameinsrtnace
     */
    public GameTimer(int gameLength1, ScheduledExecutorService scheduler, GameInstance gameInstance) {

        this.gameLength = gameLength1;
        i = Months.getRandomInt();
        endGameCondition = i + gameLength;
        this.scheduler = scheduler;
        this.gameInstance = gameInstance;
    }


    @Override
    public void run() {
        if (gameInstance.playerList.size() == 0) {
            scheduler.shutdown();
        }

        if (i == endGameCondition) { //check if gamelength has been reached
            gameInstance.getGameEnd();
        }

        String newMonth = String.valueOf(Months.getMonth(i % 12));


        gameInstance.calculateMonthlySales(newMonth); //calculate sells

        gameInstance.resetSell(); //reset last months sells

        gameInstance.updateLimits(); //decrease lower limit, increase upper limit

        gameInstance.updateResources(); //calculate new stored amount of resources

        gameInstance.sendProductionUpdate(newMonth);

        gameInstance.sendEcoUpdate();

        gameInstance.sendGroupPlayerUpdate();

        gameInstance.checkWinCapital(); //check if wincapital has been reached



        i++;

    }
}