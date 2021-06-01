package server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handling class for one game
 * one instance per game
 */
public class GameInstance {

    public BiMap<ConnectionHandler, Player> playerList;
    private final String groupName;
    public String gameState;
    private int monthLength = 3;
    private int gameLength = 5;
    public int winCapital = 100000;
    private EcoSim ecoSim;
    private int playerCount;
    public String actualMonth;
    CopyOnWriteArrayList<ConnectionHandler> threadList;
    ScheduledExecutorService scheduler;
    private static final Logger logger = LogManager.getLogger(GameInstance.class);

    /**
     * The Gammeinstance handles the group members, start and end of game and highscores
     * @param groupName name of the group from lobby
     * @param threadList list of the threads
     */
    public GameInstance(String groupName, CopyOnWriteArrayList<ConnectionHandler> threadList) {
        this.groupName = groupName;
        this.threadList = threadList;
        gameState = "WAITING";
        playerList = HashBiMap.create();
    }


    /**
     * add new player to group.
     * -Check if name is already taken, if yes, set number from 1 to n until name is free
     * -add name and corresponding connectionHandler Object to BiMap playerList
     * @param connectionHandler connectionhandler object for playerName
     * @param playerName corresponding playerNAme
     */
    public void addPlayer (ConnectionHandler connectionHandler, String playerName){
        playerList.put(connectionHandler, new Player(connectionHandler.playerName));
        sendArrayToPlayers(getUpdateStringArray());
    }

    /**
     * remove player from group, e.g. remove from hasmap playerlist
     * @param connectionHandler Connectionhandler object to identify player
     */
    public void removePlayer (ConnectionHandler connectionHandler) {
        if (playerList.size() == 1) {
            playerList.remove(connectionHandler);
            connectionHandler.gameList.remove(groupName);
            connectionHandler.sendGameList();
        }
        playerList.remove(connectionHandler);
        sendArrayToPlayers(getUpdateStringArray());
    }

    /**
     * replace connectionhandlers (used for reconnecting)
     * @param oldCH old connectionhandler
     * @param newCH new Connectionhandler
     * @param playerName name of the player
     */
    public void replaceConnectionHandler(ConnectionHandler oldCH, ConnectionHandler newCH, String playerName) {
        Player player = getPlayer(playerName);
        playerList.inverse().replace(player, oldCH, newCH);
    }

    /**
     * return player Object according to playerName
     * @param username Name
     * @return Player object
     */
    public Player getPlayer(String username) {
        Player returnPlayer = null;
        for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            Player player = entry.getValue();
            if (player.getName().equals(username)) {
                returnPlayer = player;
                break;
            }
        }
        return returnPlayer;
    }

    /**
     * get the connectionhandler to a player
     * @param player player input
     * @return according connectionhandler output
     */
    public ConnectionHandler getConnectionHandler(Player player) {
        Player returnPlayer = null;
        for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            if (player == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param connectionHandler connectionHandler object of the requesting client
     * @param request string[] with request from client
     */
    public void build (ConnectionHandler connectionHandler, String[] request){
        Player player = playerList.get(connectionHandler);
        String[] response = player.build(request);
        if (response != null) {
            connectionHandler.writeToOut(ConnectionHandler.arrayToString(response));
        }
    }

    /**
     * SELTO handling, redirects to sellTo method in ecoSim
     * @param connectionHandler connectionhandler
     * @param request request
     * @return response String[]
     */
    public String[] sellTo (ConnectionHandler connectionHandler, String[] request) {
        Player player = playerList.get(connectionHandler);
        return ecoSim.sellTo(request, player);
    }

    /**
     * start new game, set status of every player in this group to INGAME
     * @param request request
     */
    public void startGame (String[] request) {
        gameState = "RUNNING";
        if (request.length > 1) {
            winCapital = Integer.parseInt(request[1]);
            gameLength = Integer.parseInt(request[2]);
            monthLength = Integer.parseInt(request[3]);
        }
        System.out.println("-- Starting new game with wincondition " + winCapital + " $, gameLength "
                + gameLength + ", monthlength " + monthLength + " --");


        String[] response = new String[4];
        response[0] = "GAMGO";
        response[1] = String.valueOf(winCapital);
        response[2] = String.valueOf(gameLength);
        response[3] = String.valueOf(monthLength);
        for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            ConnectionHandler connectionHandler = entry.getKey();
            connectionHandler.gameStatus = "INGAME";
            connectionHandler.writeToOut(ConnectionHandler.arrayToString(response));
        }

        sendLobbyPlayerUpdate();

        scheduler = Executors.newScheduledThreadPool(1);

        GameTimer gameTimer = new GameTimer(gameLength, scheduler, this);

        scheduler.scheduleAtFixedRate(gameTimer, 2, monthLength, TimeUnit.SECONDS);

        playerCount = playerList.size();

        ecoSim = new EcoSim(playerList, playerCount, winCapital);
    }

    /**
     * send list with all group players to all group players
     * e.g. send updates in group member list
     */
    public void sendGroupPlayerUpdate() {
        sendArrayToPlayers(getUpdateStringArray());
    }

    /**
     * Method to execute all actions needed at the end of a game
     * -set gamestate to waiting
     * -set all players state to GROUP so they receive looby updates
     */
    public void getGameEnd() {
        scheduler.shutdown();
        String[] gameEndArray = new String[2];
        gameEndArray[0] = "GAMND";
        playerList.forEach((k,v) -> {
            gameEndArray[1] = String.valueOf(v.capital);
            k.writeToOut(ConnectionHandler.arrayToString(gameEndArray));
        });
        sendArrayToPlayers(getUpdateStringArray()); //send UPDAT for highscore
        String winner = "";
        try {
            winner = saveHighScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Game finished. And the winner is.. " + winner);
        //set new states
        gameState = "WAITING";
        boolean b = true;
        for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            ConnectionHandler connectionHandler = entry.getKey();
            connectionHandler.gameStatus = "GROUP";
            if (b) {
                connectionHandler.sendGameList();
                connectionHandler.sendLobbyPlayerUpdate();
                b = false;
            }
        }
        resetPlayers();
    }

    /**
     * generates UPDAT String, according to network protocol
     * @return response
     */
    public String[] getUpdateStringArray() {
        ArrayList<String> playersInGroupArrayList = new ArrayList<>();
        playersInGroupArrayList.add("UPDAT");
        playersInGroupArrayList.add("GROUP");

        Integer[] pointsArray = getSortedPointsArray();
        for (Integer integer : pointsArray) {
            if (playersInGroupArrayList.size() == playerList.size() + 2) {
                break;
            }
            for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
                ConnectionHandler connectionHandler = entry.getKey();
                Player player = entry.getValue();
                if (player.capital == integer) {
                    playersInGroupArrayList.add(connectionHandler.playerName + "," + playerList.get(connectionHandler).capital);
                }
                if (playersInGroupArrayList.size() == playerList.size() + 2) {
                    break;
                }
            }
        }
        return playersInGroupArrayList.toArray(new String[0]);
    }

    /**
     * get a sorted array with the points of all players
     * @return Integer[] with points sorted
     */
    private Integer[] getSortedPointsArray() {
        ArrayList<Integer> pointsList = new ArrayList<>();
        playerList.forEach((k,v) -> {
            if (!pointsList.contains(v.capital)) {
                pointsList.add(v.capital);
            }
        });
        Integer[] pointsArray = pointsList.toArray(new Integer[0]);
        Arrays.sort(pointsArray, Collections.reverseOrder());
        return pointsArray;
    }

    /**
     * used to send a String[] to all group players
     * @param array Array to be sent
     */
    public void sendArrayToPlayers(String[] array) {
        for (BiMap.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            ConnectionHandler connectionHandler = entry.getKey();
            connectionHandler.writeToOut(ConnectionHandler.arrayToString(array));
        }
    }

    /**
     * monthly update, update players resources
     * redirects to player's method updateResources
     */
    public void updateResources() {
        playerList.forEach((k, v) -> v.updateResources());
    }

    /**
     * monthly update, update Limits
     */
    public void updateLimits() { ecoSim.updateLimits(); }

    /**
     * monthly update, reset last months sells
     */
    public void resetSell() {
        playerList.forEach((k, v) -> v.resetSell());
    }


    /**
     * call monthlyupdate in ecosim
     * generate PRODUP and ECOUP and send to client
     * @param newMonth new month
     */
    public void calculateMonthlySales(String newMonth) {

        actualMonth = newMonth;

        ecoSim.setEvent(newMonth.equals(Months.December.toString()) || newMonth.equals(Months.March.toString()));

        ecoSim.sellToMonthlyUpdate(); //calculate sells for the month



    }

    /**
     * send PRODUP to all group players
     * @param newMonth the new Month
     */
    public void sendProductionUpdate(String newMonth) {
        playerList.forEach((connectionHandler, player) ->
                connectionHandler.writeToOut(ConnectionHandler.arrayToString(player.getProductionUpdate(newMonth))));
    }

    /**
     * send ECOUP to all grou oplayers
     */
    public void sendEcoUpdate() {
        String[] ecoUpdateString = ecoSim.getEcoUpdate();
        playerList.forEach((k,v) ->
                k.writeToOut(ConnectionHandler.arrayToString(ecoUpdateString)));
    }

    /**
     * check if win capital has been reached
     */
    public void checkWinCapital() {
        for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            Player player = entry.getValue();
            if (player.capital >= winCapital) {
                getGameEnd();
                break;
            }
        }
    }

    /**
     * get List of all group players
     * @return String with all players, delimited by ","
     */
    public String getPlayerList() {
        StringJoiner playerListStringBuilder = new StringJoiner(",");
        playerList.forEach((k,v) -> playerListStringBuilder.add(v.getName()));
        return playerListStringBuilder.toString();

    }


    /**
     * send list of all players to  all lobby players
     */
    private void sendLobbyPlayerUpdate() {
        String toSend = ConnectionHandler.arrayToString(getLobbyPlayerArray());
        for (ConnectionHandler connectionHandler : threadList) {
            connectionHandler.writeToOut(toSend);
        }
    }

    /**
     * build an array with all lobby players
     * @return String[]with al llobby players
     */
    private String[] getLobbyPlayerArray() {

        ArrayList<String> LobbyPlayerArrayList = new ArrayList<>();
        LobbyPlayerArrayList.add("UPDAT");
        LobbyPlayerArrayList.add("LOBBY");

        for (ConnectionHandler connectionHandler : threadList) {
            if (connectionHandler.gameStatus.equals("LOBBY")) {
                LobbyPlayerArrayList.add(connectionHandler.playerName);
            }
        }
        return LobbyPlayerArrayList.toArray(new String[0]);
    }

    /**
     * call constructor of player class to reset for a new game
     */
    public void resetPlayers() {
        playerList.forEach((k,v) -> v.setDefault());
    }


    /**
     * save highscores from last game
     * @return bestr player
     * @throws IOException if there is no scores/list
     */
    public String saveHighScore() throws IOException {
        ArrayList<Integer> pointsList = new ArrayList<Integer>();
        playerList.forEach((k, v) -> pointsList.add(v.capital));
        Integer[] points = pointsList.toArray(new Integer[0]);
        Arrays.sort(points);
        Player winPlayer = null;
        for (BiMap.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
            if(entry.getValue().capital == points[points.length - 1]) {
                winPlayer = entry.getValue();
            }
        }
        StringJoiner toWriteJoiner = new StringJoiner( ", ","", "\n");
        assert winPlayer != null;
        toWriteJoiner.add(winPlayer.getName()).add(String.valueOf(winPlayer.capital));
        toWriteJoiner.add(String.valueOf(LocalDate.now()));
        writeToFile(toWriteJoiner.toString());
        return winPlayer.getName();
    }

    /**
     * write the highscore to the text file
     * @param toWrite String with the player, points and date to write
     */
    public void writeToFile(String toWrite) {
        File file = new File("Highscore.txt");
        if (!file.exists()) {
            createHighscoreFile();
        }

        try {
            FileWriter fileWriter = new FileWriter("Highscore.txt", true);
            BufferedWriter buffer = new BufferedWriter(fileWriter);
            buffer.write(toWrite);
            buffer.close();
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
            logger.info("Highscore was successfully updated");

        } catch (IOException e) {
            System.out.println("An error occurred while writing Highscore.txt");
            logger.error("An problem occurred while writing the Highscore");
            e.printStackTrace();
        }

    }

    /**
     * create new highscore file if none exists
     */
    public static void createHighscoreFile() {
        try {
            File highscore = new File("Highscore.txt");
            if (highscore.createNewFile()) {
                System.out.println("File created: " + highscore.getName());
            } else {
                System.out.println("File already exists.");
                logger.info("Highscore.txt already existed");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            logger.error("An error occurred while creating the Highscore.txt file");
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter("Highscore.txt");
            fileWriter.write(" *** Highscores *** " + "\n\n\n" + "Willy Wonka, 10000, 1971-01-01" + "\n");
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
            logger.info("Highscore was successfully updated");
        } catch (IOException e) {
            System.out.println("An error occurred while writing Highscore.txt");
            logger.error("An error occurred while writing Highscore.txt");
            e.printStackTrace();
        }
    }
}
