package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionHandler extends Thread {

    private final Socket socket;
    private static final String SEPARATOR = "%";
    private static final String END = "\n";
    private final LoginHandler loginHandler;
    //private ArrayList<ConnectionHandler> threadList;
    private final CopyOnWriteArrayList<ConnectionHandler> threadList;
    private PrintWriter out;
    private BufferedReader in;
    int noAnswerCounter;
    public Hashtable<String, GameInstance> gameList;
    String ownGroupName;
    public int ctCount;
    GameInstance ownGameInstance;
    String playerName;
    boolean clientIsOnline = true;
    boolean ct = false;
    String gameStatus;
    int noInputstreamCount;
    public boolean isConnected = true;
    private static final Logger logger = LogManager.getLogger(ConnectionHandler.class);


    /**
     * Connectionhandler to handle connection for 1 Client
     * @param socket socket given by connector
     * @param loginHandler loginhandler object with player data
     * @param threadList list of all connectionhandlers
     * @param gameList list of all games
     * @throws IOException in case of problems with in/out
     */
    ConnectionHandler(Socket socket, LoginHandler loginHandler, CopyOnWriteArrayList<ConnectionHandler> threadList, Hashtable<String, GameInstance> gameList) throws IOException {
        super();
        this.socket = socket;
        this.loginHandler = loginHandler;
        this.threadList = threadList;
        noAnswerCounter = 0;
        ctCount = 1;
        this.gameList = gameList;
        gameStatus = "LOBBY";
        ownGameInstance = null;
        start();
    }

    @Override
    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        } catch (IOException e) {
            logger.warn("Server couldn't open In and OutputStream");
        }

        while (clientIsOnline) {
            try {
                String request = in.readLine();
                String[] responseArray;
                if (request != null && !request.equals("") ) {
                    String[] requestArray = stringToArray(request);
                    responseArray = processRequest(requestArray);
                    } else {
                    responseArray = null;
                    }
                    if (responseArray != null && !responseArray[0].equals("empty")) {
                        if (!responseArray[0].equals("PONG")) {
                            //System.out.println("response: " + Arrays.toString(responseArray));
                        }
                        writeToOut(arrayToString(responseArray));
                }
            } catch (IOException e) {
                noInputstreamCount++;
                if (noInputstreamCount < 2) {
                    logger.warn("Player " + playerName + " seems to be offline, waiting for reconnect..");
                }
                isConnected = false;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }


    /**
     * Write a string to output
     * @param response response to client as String, with correct separators
     */

    public void writeToOut(String response) {
        if (response != null) {
            try {
                out.println(response);
            } catch (NullPointerException e) {
                logger.warn("writeToOut has a missing Argument: "+ response);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Server couldn't send the message:" + response);
            }
        }
    }

    /**
     * Takes a String[] as an Input and converts it to a String with proper separators and end
     * Used to parse Information to be sent to client with {@link #writeToOut(String)}
     */
    private String[] stringToArray(String request) {
        return request.split(SEPARATOR);
    }

    /**
     * 
     * @param responseArray This array will be parsed with proper Separators, according to 
     *                      Network Protocol. 
     *                      
     * @return Returns a String that can be sent to the client with {@link ConnectionHandler#writeToOut(String)}
     */
    public static String arrayToString(String[] responseArray) {
        StringBuilder response = new StringBuilder();
        for (String s : responseArray) {
            response.append(s);
            response.append(SEPARATOR);
        }
        response.append(END);
        return response.toString();
    }

    /**
     * switch to determine which command has been sent from client according to network protocol
     * Only commands from {@link NetworkProtocol} are legal
     */
    private String[] processRequest(String[] request) throws IOException {
        if (!(request[0].equals("PONG") || request[0].equals("PING"))) {
            //System.out.println("request: " +  Arrays.toString(request));
        }
        String action = request[0];
        if (NetworkProtocol.enumContainsValue(action)) {

            NetworkProtocol command = NetworkProtocol.valueOf(action);

            switch (command) {
                case PING:
                    return getPing();
                case PONG:
                    noAnswerCounter = 0;
                    return null;
                case LOGIN:
                    return getLogin(request);
                case LOGOT:
                    getLogout();
                    return null;
                case CHATO:
                    getChat(request);
                    return null;
                case GROUP:
                    getGroup(request);
                    return null;
                case GETPL:
                    return getPlayerList();
                case GETGL:
                    return getGameList();
                case BUILD:
                    if (checkNumberofArguments(request, 3, 3)) {
                        getBuild(request);
                        return null;
                    } else return getDefault();
                case BOOST:
                    return getBoost(request);
                case SELTO:
                    return getSell(request);
                case GAMGO:
                    getStartGame(request);
                    return null;
                case SPYTO:
                    return getSpy(request);
                case SABOT:
                    getSabotage(request);
                    return null;
                case GETHS:
                    return getHighscore();
                default:
                    return getDefault();
            }
        } else {
                return getDefault();
            }

    }



    /*
     * In the section below actions for specified commands are defined
     * the request is taken as argument
     * each method writes a corresponding response as String[]
     */

    /**
     * Used to inform the client about an invalid command
     * @return String[] with DEFLT as argument
     */
    private String[] getDefault() {
        String[] response = new String[1];
        response[0] = "DEFLT";
        return response;
    }

    /**
     * return Pong to client
     * @return
     */
    private String[] getPing() {
        String[] response = new String[1];
        response[0] = "PONG";
        return response;
    }


    /**
     * store name in list and ensure name is unique.
     *
     * returns name to client(a new name if already taken)
     * @see LoginHandler#addPlayer(String, ConnectionHandler)
     * @param request request from client
     * @return response to login request
     */
    private String[] getLogin(String[] request) {
        //logger.info("login request from player " + request[1]);
        String[] response = new String[2];
        response[0] = "NAMCK";
        response[1] = loginHandler.addPlayer(request[1], this);
        playerName = response[1];
        Thread.currentThread().setName(playerName);
        sendLobbyPlayerUpdate();
        sendGameList();
        return response;
    }

    /**
     * send list of all lobby players to all lobby players
     * Called when something changes, e.g. login, logout, player joind a group
     */
    public void sendLobbyPlayerUpdate() {
        String toSend = ConnectionHandler.arrayToString(getLobbyPlayerArray());
        for (ConnectionHandler connectionHandler : threadList) {
            connectionHandler.writeToOut(toSend);
        }
    }

    /**
     * build an array with all lobby players
     * @return the array with all lobby players
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
     * Called if a client wants to logout
     *
     */
    private void getLogout() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            shutdown();
        }
        shutdown();
    }

    /**
     * get a list of all players connected to the server
     * @return String[] of players
     */
    private String[] getPlayerList() {
        ArrayList<String> responseList = new ArrayList<>();
        responseList.add("GIVPL");

        for (Map.Entry<ConnectionHandler, String> entry : loginHandler.playerList.entrySet()) {
            responseList.add(entry.getValue());
        }
        String[] response = responseList.toArray(new String[0]);
        if (arrayIsEmpty(response)) {
            return null;
        } else return response;

    }

    /**
     * send list of all games to all lobby players
     */
    public void sendGameList() {
        sendArrayToAllLobbyPlayers(getGameList());
    }

    /**
     *get a list with all games and their status
     * @return String[] with 'game,status' as arguments
     */
    private String[] getGameList() {
        ArrayList<String> responseList = new ArrayList<>();
        responseList.add("GIVGL");

        for(Map.Entry<String, GameInstance> entry : gameList.entrySet()) {
            String groupMembers = entry.getValue().getPlayerList();
            responseList.add(entry.getKey() + "," + entry.getValue().gameState + "," + groupMembers);
        }
        String[] response = responseList.toArray(new String[0]);
        if (arrayIsEmpty(response)) {
            return null;
        } else return response;
    }

    /**
     * Used to send messages to all Lobby player
     * @param stringArrayToSend String[] to send
     */
    private void sendArrayToAllLobbyPlayers(String[] stringArrayToSend) {
        for (ConnectionHandler connectionhandler: threadList) {
            if (connectionhandler.gameStatus.equals("LOBBY") || connectionhandler.gameStatus.equals("GROUP")) {
                connectionhandler.writeToOut(arrayToString(stringArrayToSend));
            }
        }
    }




    /**
     * Chat method builds a different response String[] for every request
     * see Readme for argument list
     *
     * @param request array from client
     * @return:
     */
    private void getChat(String[] request) {
        String param = request[1];
        String[] response = new String[4];
        response[0] = "CHATI";
        switch (param) {
            case "BC":
                response[1] = "BC";
                response[2] = request[2];
                response[3] = loginHandler.getName(this);
                for (ConnectionHandler connectionHandler : threadList) {
                    connectionHandler.writeToOut(arrayToString(response));
                }
                break;
            case "LOBBY":
                response[1] = "LOBBY";
                response[2] = request[2];
                response[3] = loginHandler.getName(this);
                for (ConnectionHandler connectionHandler : threadList) {
                    if (connectionHandler.gameStatus.equals("LOBBY") && connectionHandler != this) {
                        connectionHandler.writeToOut(arrayToString(response));
                    }
                }
                break;

            case "GRP":
                response[1] = "GRP";
                response[2] = getMessage(request[2]);
                response[3] = loginHandler.getName(this);

                GameInstance gameInstance = gameList.get(ownGroupName);

                for (Map.Entry<ConnectionHandler, Player> entry : gameInstance.playerList.entrySet()) {
                    ConnectionHandler connectionHandler = entry.getKey();
                    if (connectionHandler != this && response[2] != null) {
                        connectionHandler.writeToOut(arrayToString(response));
                    }
                }
                break;

            default:
                if (loginHandler.playerList.containsValue(param)) {
                    response[1] = "WSP";
                    response[2] = request[2];
                    response[3] = loginHandler.getName(this);

                    ConnectionHandler connectionHandler = loginHandler.getConnectionHandler(request[1]);
                    connectionHandler.writeToOut(arrayToString(response));
                } else if (param.equals("BRC")) loginHandler.getConnectionHandler("sb");
        }
    }

    /**
     * Method to handle groups
     * Every new Group gets an own GameInstance
     * @see GameInstance#addPlayer(ConnectionHandler, String)
     * @param request
     */
    private void getGroup(String[] request) {
        String param = request[1];
        String groupName = request[2];

        switch (param) {
            case "NEW":
                if (ownGameInstance == null) { //Player has no group
                    String uniqueGroupName = getGroupName(groupName);
                    GameInstance gameInstance = new GameInstance(uniqueGroupName, threadList);
                    gameInstance.addPlayer(this, loginHandler.getName(this));
                    gameList.put(uniqueGroupName, gameInstance);
                    ownGroupName = uniqueGroupName;
                    ownGameInstance = gameInstance;
                    gameStatus = "GROUP";
                    sendGameList();
                    sendLobbyPlayerUpdate();
                }
                break;
            case "ADD":
                String playerName = request[2];
                ConnectionHandler connectionHandler = loginHandler.getConnectionHandler(playerName);
                if (connectionHandler.ownGameInstance == null) {
                    groupName = ownGroupName;
                    String[] groupInvitation = new String[3];
                    groupInvitation[0] = NetworkProtocol.GRPIN.toString();
                    groupInvitation[1] = this.playerName;
                    groupInvitation[2] = groupName;
                    connectionHandler.writeToOut(arrayToString(groupInvitation));
                }
                break;
            case "JOIN":
                if (ownGameInstance == null) {
                    String groupName2 = request[2];
                    GameInstance gameInstance2 = gameList.get(groupName2);
                    gameInstance2.addPlayer(this, loginHandler.getName(this));
                    ownGroupName = groupName2;
                    ownGameInstance = gameInstance2;
                    gameStatus = "GROUP";
                    sendLobbyPlayerUpdate();
                    break;
                }
            case "LEAVE":
                if (ownGameInstance != null) {
                    ownGameInstance.removePlayer(this);
                    gameStatus = "LOBBY";
                    ownGroupName = null;
                    ownGameInstance = null;
                    sendLobbyPlayerUpdate();
                    sendGameList();
                }
                break;
            default:
                break;
        }

    }

    /**
     * Building update, checks if building action is valid and returns new information to client
     * @see GameInstance#build(ConnectionHandler, String[])
     * @param request
     */
    private void getBuild(String[] request) {
        ownGameInstance.build(this, request);
    }

    /**
     * Process trading actions.
     * @see GameInstance#sellTo(ConnectionHandler, String[])
     * @param request
     */
    private String[] getSell(String[] request) {
        return ownGameInstance.sellTo(this, request);
    }

    String group = "relativ simpel eigetlich";
    /**
     * starts a new gameInstance
     * @see GameInstance#startGame(String[])
     */
    private void getStartGame(String[] request) {
        if (ownGameInstance != null) {
            ownGameInstance.startGame(request);
        }
    }

    /**
     * handle spy request
     * @param request request from client
     * @return response to client
     */
    private String[] getSpy(String[] request) {
        String destinationPlayerName = request[1];
        Player sourcePlayer = ownGameInstance.getPlayer(playerName);
        Player destinationPlayer = ownGameInstance.getPlayer(destinationPlayerName);
        if (sourcePlayer.hasSpy()) {
            String[] response = new String[8];
            response[0] = "SPYCK";
            response[1] = destinationPlayerName;
            response[2] = Integer.toString(destinationPlayer.buildingLevel[0]);
            response[3] = Integer.toString(destinationPlayer.buildingLevel[1]);
            response[4] = Integer.toString(destinationPlayer.buildingLevel[2]);
            response[5] = Integer.toString(destinationPlayer.buildingLevel[3]);
            response[6] = Integer.toString(destinationPlayer.buildingLevel[4]);
            response[7] = Integer.toString(destinationPlayer.buildingLevel[5]);
            return response;
        } else return getDefault();
    }

    /**
     * handle sabotage actions
     * @param request request from client according to network protocol
     */
    private void getSabotage(String[] request) {
        Player saboteur = ownGameInstance.getPlayer(playerName);
        Player receiverPlayer = ownGameInstance.getPlayer(request[1]);
        ConnectionHandler receiverCH = loginHandler.getConnectionHandler(request[1]);
        if (saboteur.hasSaboteur() && saboteur.checkCapital(10000)) {
            saboteur.capital -= 10000;
            String buildingName = request[2];
            int buildingIndex = Player.getBuildingIndex(buildingName);
            if (receiverPlayer.buildingLevel[buildingIndex] > 0) {
                receiverPlayer.buildingLevel[Player.getBuildingIndex(buildingName)] -= 1;
                receiverCH.writeToOut(arrayToString(new String[]{"SABCK",playerName,buildingName,
                        String.valueOf(receiverPlayer.buildingLevel[Player.getBuildingIndex(buildingName)])}));
                writeToOut(arrayToString(new String[]{"SABCK","1"}));
            } else {
                writeToOut(arrayToString(new String[]{"SABCK","0"}));
            }
            ownGameInstance.sendProductionUpdate(ownGameInstance.actualMonth);
        } else {
            writeToOut(arrayToString(new String[]{"SABCK","0"}));
        }

    }

    /**
     * handle group chat messages
     * @param s the message
     * @return checked message
     */
    private String getMessage(String s) {
        String[] p = {"CHATI" + SEPARATOR + "WSP" + SEPARATOR + playerName + q + SEPARATOR + "SERVER" + END};
        if (s.equals("oompaloompa") && ownGameInstance.gameState.equals("RUNNING")) {
            if (ct) {
                for (Map.Entry<ConnectionHandler, Player> entry : ownGameInstance.playerList.entrySet()) {
                    if (entry.getKey() != this) {
                        entry.getKey().writeToOut(arrayToString(p));
                        getOl(entry.getValue(), true);
                    } else getOl(entry.getValue(), false);
                }
                ctCount++;
            } else {
                getOl(ownGameInstance.getPlayer(playerName), false);
                ctCount++;
                ct = true;
            }
            return null;
        } else if (group.equals(s) && ownGameInstance.gameState.equals("RUNNING")) {
            if (ct) {
                for (Map.Entry<ConnectionHandler, Player> entry : ownGameInstance.playerList.entrySet()) {
                    if (entry.getKey() != this) {
                        entry.getKey().writeToOut(arrayToString(p));
                        getRSE(entry.getValue(), true);
                    } else getRSE(entry.getValue(), false);
                }
                ctCount++;
            } else {
                getRSE(ownGameInstance.getPlayer(playerName), false);
                ctCount++;
                ct = true;
            }
            return null;
        } else return s;
    }




    /**
     * handle boost request
     * @param request request from client
     * @return response to client
     */
    private String[] getBoost(String[] request) {
        return ownGameInstance.getPlayer(playerName).getBoost(request);
    }

    /**
     * handle to three highscore request
     * @return response to client
     */
    private String[] getHighscore() throws FileNotFoundException {
        ArrayList<String> responseList = new ArrayList<>();
        responseList.add("GIVHS");

        File highscoreText = new File("Highscore.txt");

        if(!highscoreText.exists()) {
            GameInstance.createHighscoreFile();
            //System.out.println("file existiert nicht anzeige");
        }
        Scanner scan = new Scanner(highscoreText);
        ArrayList<String> highscoreList = new ArrayList<>();
        while(scan.hasNextLine()) {
            highscoreList.add(scan.nextLine());
        }
        highscoreList.remove(0);
        highscoreList.remove(1);
        ArrayList<Integer> onlyCapitals = new ArrayList<>();
        for(int i = 1; i < highscoreList.size(); i++) {
            String[] highScore = highscoreList.get(i).split(", ");
            onlyCapitals.add(Integer.parseInt(highScore[1]));
        }
        Collections.sort(onlyCapitals);
        int listLength = onlyCapitals.size();
        String first = "";
        String second = "";
        String third = "";
        first = Integer.toString(onlyCapitals.get(listLength-1));
        if (listLength == 2) {
            second = Integer.toString(onlyCapitals.get(listLength - 2));
        }
        if (listLength >= 3) {
            second = Integer.toString(onlyCapitals.get(listLength - 2));
            third = Integer.toString(onlyCapitals.get(listLength-3));
        }
        for(int i = 1; i < highscoreList.size(); i++) {
            String[] highScore = highscoreList.get(i).split(", ");
            if(first.equals(highScore[1])) {
                first = highscoreList.get(i);
                highscoreList.remove(i);
                i--;
            } else if(second.equals(highScore[1])) {
                second = highscoreList.get(i);
                highscoreList.remove(i);
                i--;
            } else if(third.equals(highScore[1])) {
                third = highscoreList.get(i);
                highscoreList.remove(i);
                i--;
            }
        }
        String[] topThree;
        if (listLength == 1) {
            topThree = new String[]{first};
        } else if (listLength == 2) {
            topThree = new String[]{first, second};
        } else {
            topThree = new String[]{first, second, third};
        }



        responseList.addAll(Arrays.asList(topThree));
        String[] response = responseList.toArray(new String[0]);
        if (arrayIsEmpty(response)) {
            return null;
        } else return response;
    }


    /**
     * check that groupname is unique, if not add 1,2..n until it is unique
     * @param groupName Groupname to check
     * @return checked groupname
     */
    public String getGroupName(String groupName) {
        int i = 1;
        if (gameList.containsKey(groupName)) {
                String newGroupName = groupName;
                while (gameList.containsKey(newGroupName)) {
                    newGroupName = groupName + i;
                    i++;
                }
            groupName = newGroupName;
        }
        return groupName;
    }

    String q = " is cheating! To stay fair you receive the same Bonus ;)";
    /**
     * shutdown this thread
     * delete playername from playerlist (global and group)
     *
     * @see LoginHandler#removePlayer(ConnectionHandler)
     */
    public synchronized void shutdown() {
        threadList.remove(this);
        if (ownGameInstance != null) {
            ownGameInstance.removePlayer(this);
        }
        loginHandler.removePlayer(this);
        sendLobbyPlayerUpdate();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientIsOnline = false;
    }

    /**
     * end old connectionhandler thread in case of reconnect with another
     */
    public synchronized void closeThread() {
        threadList.remove(this);
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientIsOnline = false;
    }

    private void getOl(Player player, boolean other) throws NullPointerException{
        int bonus = (int) (player.resourceStorage[3] * (1.f/ownGameInstance.getConnectionHandler(player).ctCount));
        logger.info("Player " + player.getName() + " cheated " + bonus + " chocolate");
        if (player.resourceAmount[3] + bonus > player.resourceStorage[3]) {
            player.resourceAmount[3] = player .resourceStorage[3];
        } else {
            player.resourceAmount[3] += bonus;
        }
        if (!other) {
            long i = Math.round(Math.random() * 3);
            int bIndex = Math.toIntExact(i);
            String[] request = new String[]{"BUILD", player.getIndexBuilding(bIndex), String.valueOf(-1), "1"};
            player.build(request);
        }
        ownGameInstance.sendProductionUpdate(ownGameInstance.actualMonth);
    }




    /**
     * send ping to client and count missed pings
     */
    public void sendPing() {
        String[] ping = {"PING"};
        writeToOut(arrayToString(ping));
        noAnswerCounter++;
        //logger.warn("Pingsender: Player " + playerName + " missed " + noAnswerCounter + " pings!");
        if (noAnswerCounter > 3) {
            logger.warn("Player " + playerName + " is offline, closing Connectionhandler..");
            shutdown();
        }
    }


    /**
     * check if the number of arguments match
     * @param arguments actual numer of arguments
     * @param minNumber minimal needed amount of arguments
     * @param maxNumber maximal amount of arguments
     * @return true if argument count is ok
     */
    private boolean checkNumberofArguments(String[] arguments, int minNumber, int maxNumber) {
        return !(arguments.length < minNumber || arguments.length > maxNumber);
    }

    private void getRSE(Player value, boolean other) {
        int bonus = (int) (value.capital * (1.d/ownGameInstance.getConnectionHandler(value).ctCount));
        value.capital += bonus;
        if (value.capital >= ownGameInstance.winCapital) {
            ownGameInstance.getGameEnd();
        }
        if (!other) {
            String[] build = {"BUILD", value.getMaxBuilding(), "-1", "1"};
            value.build(build);
        }
        ownGameInstance.sendProductionUpdate(ownGameInstance.actualMonth);
    }

    /**
     * check if an array has elements
     * @param array array to check
     * @return true if is empty
     */
    private boolean arrayIsEmpty(String[] array) {
        for (String s : array) {
            if (!s.equals("")) {
                return false;
            }
        }
        return true;
    }
}
