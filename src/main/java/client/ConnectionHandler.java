package client;

import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.NetworkProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * main class creates a object of the connectionHandler
 * connectionHandler creates a socket which stays open until the logout
 * it handles incoming requests and hands them off to other classes
 * the socket is used by other classes to send messages to the server
 */
public class ConnectionHandler extends Thread {

    boolean answered = true;    //documents if a PING is answered
    int noAnswerCounter = 0;    //counts how long no PING has been answered (in 5" steps)
    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    final String SEP = "%"; //separator between arguments
    final String END = "\n";    //end of a command (bsp. LOGIN\nBob\n\n)
    boolean runningSocket = false;     //true while the socket is open and stopped the stopConnection methode
    boolean gameIsRunning = false;
    boolean lobbyIsOpen = false;
    boolean hasProductionBuilding;
    boolean inGroup = false;
    int warningCounter;
    String[] sellCheck;
    String[] lobbyPlayerlist = {null}; //list of every player in your group during game and list of every player in the lobby while you're in the lobby
    String[] groupPlayerlist = {"noGroup"}; //list of every player without a group
    String[] playerAndScoreList; //list of every player in your group with their scores
    String[] playerlistAll = {null}; //list of every player
    String[] groupList; //list of every group
    String[] topScores;
    String[] spyData;
    //checking if the player has is selling and buying the same ressource in one month (sugar, milk, cacao)
    //false if already one action was made
    boolean[] productCheckIn = {true, true, true}; //checks if the product was already bought
    boolean[] productCheckOut = {true, true, true}; //checks if the product was already sold
    //ecoDate to plot a ecosim function (maxSellPrice, minSellPrice, durchschnitt, maxBuyPrice, minBuyPrice, Durchschnitt, month) for the last 12 month
    String[][] ecoDataSugar = new String[13][6];
    String[][] ecoDataCacao = new String[13][6];
    String[][] ecoDataMilk = new String[13][6];
    String[][] ecoDataChocolate = new String[13][6];
    String[][][] ecoData = {ecoDataSugar, ecoDataCacao, ecoDataMilk, ecoDataChocolate};
    //saves the limits for the buying and sellingprice to adapt the slider in the sell and buywindows
    //String[] event = new String[3];
    double[] newSellPrice = new double[4];
    double[] newBuyPrice = new double[4];
    double[] buyLimit = new double[4];
    String[] monthArray = {"-","-","-","-","-","-","-","-","-","-","-","-","-"};
    //level of every building/field
    int FACTORY = 0;
    int SUGARFARM = 0;
    int STABLE = 0;
    int CACAOFARM = 0;
    int LAB = 0;
    int WAREHOUSE = 0;
    //stock of every product (sugar, cacao, milk, chocolate)
    int[] stock = new int[4];
    //Array with amount of product selled this month
    int[] sold = new int[4];
    //Array with price the player sold the products this month
    int[] price = new int[4];
    int capital;
    double income = 0;
    String month = "";
    boolean startedLoop = false;
    static Logger logger = LogManager.getLogger(ConnectionHandler.class);

    /**
     * constructor
     */
    ConnectionHandler() {
        super("ConnectionHandlerThread");
        start();
    }

    /**
     * opens the socket, the in- and outputstream and loop reading in the inputstream and handing it off to the readRequest methode
     */
    @Override
    public void run() throws IllegalStateException {
        openSocket();
        while(runningSocket) {
            try {
                String msg = in.readLine();
                if (msg != null && !msg.equals("")) {
                    readRequest(msg);
                }
                warningCounter = 0;
            } catch(IOException | URISyntaxException e) {
                if (warningCounter < 8 && runningSocket) {
                    logger.fatal("Client couldn't read inputStream!");
                    warningCounter++;
                }
            }
        }
    }

    /**
     * sends a string to the server
     * @param string in form Command\nparameter1-n\n\n which has to be sent to the server
     */
    public synchronized void sendMessage(String string) {
        try {
            out.println(string);
        } catch (Exception e) {
            if (warningCounter < 8 && runningSocket) {
                logger.warn("Client couldn't send the message");
                warningCounter++;
            }
        }
    }

    /**
     * turns a String into a array of the command and the argument and hands it off to the processRequest methode
     * @param msg String sent by the server
     * @throws IOException in.read() could throw exception
     */
    private void readRequest(String msg) throws IOException, IllegalStateException, URISyntaxException {
        String[] request_arr = msg.split(SEP);
        if (request_arr.length > 0) {
            processRequest(request_arr);
        }
    }

    /**
     * stopps connection by closing socket and in and outputstream
     * @param withLogot who has the logout
     */
    public void stopConnection(boolean withLogot) {
        runningSocket = false;
        if (withLogot) {
            sendMessage("LOGOT\n");
            PingSender.scheduler.shutdown();
        }
        try {
            in.close();
            out.close();
            GUIStart.mainStage.close();
            if (gameIsRunning) {
                GUIGameController.getInstance().dayCounter.scheduler.shutdown();
            }
            socket.close();
        } catch(IOException e) {

            logger.fatal("couldn't stop connection");
        }
    }

    /**
     *checks the command in a switch case condition and works them off or hands them off to a other class
     * @param request requests the requests from the server
     * @throws IOException if it cant find it
     * @throws IllegalStateException if the state is illegal
     * @throws URISyntaxException if the urisyntax is wrong
     */
    public void processRequest(String[] request) throws IllegalStateException, IOException, URISyntaxException {
        String stringCommand = request[0];
        if (NetworkProtocol.enumContainsValue(stringCommand)) {

            NetworkProtocol command = NetworkProtocol.valueOf(stringCommand);
            switch (command) {
                case UPDAT:
                    handleUPDAT(request);
                    break;
                case PRDUP:
                    handlePRDUP(request);
                    GUIGameController.getInstance().setMonthPicture(request[1]);
                    break;
                case ECOUP:
                    try {
                        handleECOUP(request);
                    } catch(Exception e) {
                        logger.info("invalid ecoup");
                    }
                    break;
                case BLDCK:
                    handleBLDCK(request);
                    break;
                case BSTCK:
                    handleBSTCK(request);
                    break;
                case CHATI:
                    if (request.length == 4) {
                        System.out.println("new message by " + request[3] + ": " + request[2]);
                        if(gameIsRunning) {
                            GUIGameController.getInstance().getIncomingChatGame(request);
                        } else {
                            GUILobbyController.getInstance().getIncomingChatLobby(request);
                        }
                    } else {
                        System.out.println(request[1] + ": " + request[2]);
                    }
                    break;
                case SPYCK:
                    spyData = request;
                    if (Updater.spyForSabotage) {
                        GUISabotageController.getInstance().setBuildingList();
                    } else {
                        GUISpyController.getInstance().setSpyData(request);
                    }
                    break;
                case SABCK:
                    if (request.length == 2) {
                        GUISabotageController.getInstance().setSabData(request[1]);
                    } else {
                        GUIGameController.getInstance().setSabotWarning(request);
                    }
                    break;
                case SELCK:
                    handleSELCK(request);
                    break;
                case NAMCK:
                    Main.username = request[1];
                    System.out.println("your username is: " + request[1]);
                    GUILoginController.getInstance().handleNamck();
                    break;
                case GRPIN:
                    GUILobbyController.getInstance().handleGroupInvitation(request);
                    break;
                case GAMGO:
                    GUILobbyController.getInstance().highscore = Integer.parseInt(request[1]);
                    GUILobbyController.getInstance().gamelength = Integer.parseInt(request[2]);
                    GUILobbyController.getInstance().monthlength = Integer.parseInt(request[3]);
                    GUILobbyController.getInstance().startGame();
                    System.out.println("game started with\nhighscore: " + GUILobbyController.getInstance().highscore + "\ngamelength: " +
                            GUILobbyController.getInstance().gamelength + "\nmonthLength: " + GUILobbyController.getInstance().monthlength);
                    gameIsRunning = true;
                    break;
                case GAMND:
                    handleGAMND(request);
                    break;
                case GIVPL:
                    playerlistAll = new String[request.length - 1];
                    System.arraycopy(request, 1, playerlistAll, 0, request.length - 1);
                    if (Updater.getPlayerlist) {
                        try {
                            GUILobbyController.getInstance().setPlayerlistAll();
                        } catch (Exception e) {
                            logger.info("couldn't update playerlistall");
                        }
                    }
                    break;
                case GIVGL:
                    groupList = request;
                    if (Updater.getGrouplist) {
                        GUILobbyController.getInstance().setGrouplist();
                    }
                    break;
                case DEFLT:
                    //gets ignored
                    break;
                case GIVHS:
                    topScores = request;
                    GUILobbyController.getInstance().setHighscores();
                case PING:
                    sendMessage("PONG" + SEP + "test" + END);
                    break;
                case PONG:
                    answered = true;
                    noAnswerCounter = 0;
                    break;
                default:
                    logger.warn("Server sent an invalid command:" + Arrays.toString(request));
            }
        } else {
            logger.warn("sent command \"" + request[0] + "\" does not exist in the enum");
        }
    }

    /**
     * writes the different playerlists and sets inGroup true if the player is in a group
     * @param request requestArray sent by the server
     */
    private void handleUPDAT(String[] request) {
        if (request[1].equals("GROUP")) {
            groupPlayerlist = null;
            playerAndScoreList = null;
            inGroup = true;
            playerAndScoreList = new String[request.length - 2];
            System.arraycopy(request, 2, playerAndScoreList, 0, playerAndScoreList.length);

            groupPlayerlist = new String[playerAndScoreList.length];
            for (int i = 0; i < request.length - 2; i++) {
                groupPlayerlist[i] = "";
                for (int j = 0; j < request[i + 2].length(); j++) {
                    if (request[i + 2].charAt(j) != ',') {
                        groupPlayerlist[i] += request[i + 2].charAt(j);
                    } else {
                        break;
                    }
                }
            }

        } else {
            lobbyPlayerlist = null;
            lobbyPlayerlist = new String[request.length - 2];
            System.arraycopy(request, 2, lobbyPlayerlist, 0, lobbyPlayerlist.length);
        }
        if (!gameIsRunning && lobbyIsOpen) {
            GUILobbyController.getInstance().setLobbyPlayerlist();
            GUILobbyController.getInstance().getYourGroupPlayerlist();
        }
    }

    /**
     * updates the month, capital, income, every products stock, selled and price
     * setts productCheck to true so one can sell or buy the same product again next month
     * @param request requestArray sent by the server
     */
    private void handlePRDUP(String[] request) {
        if (!month.equals(request[1])) {
            GUIGameController.getInstance().dayOfTheMonth = 1;
            month = request[1];
            GUIGameController.getInstance().removeOffers();
            Arrays.fill(productCheckIn, true);
            Arrays.fill(productCheckOut, true);
        }
        String[] cash = request[2].split(",");
        capital = Integer.parseInt(cash[0]);
        income = Double.parseDouble(cash[1]);
        String[] sugarArray = request[3].split(",");
        String[] cacaoArray = request[4].split(",");
        String[] milkArray = request[5].split(",");
        String[] chocolateArray = request[6].split(",");
        stock[0] = Integer.parseInt(sugarArray[0]);
        stock[1] = Integer.parseInt(cacaoArray[0]);
        stock[2] = Integer.parseInt(milkArray[0]);
        stock[3] = Integer.parseInt(chocolateArray[0]);
        sold[0] = Integer.parseInt(sugarArray[1]);
        sold[1] = Integer.parseInt(cacaoArray[1]);
        sold[2] = Integer.parseInt(milkArray[1]);
        sold[3] = Integer.parseInt(chocolateArray[1]);
        price[0] = Integer.parseInt(sugarArray[2]);
        price[1] = Integer.parseInt(cacaoArray[2]);
        price[2] = Integer.parseInt(milkArray[2]);
        price[3] = Integer.parseInt(chocolateArray[2]);
        GUIGameController.getInstance().updateResources();
    }

    /**
     * handles the incoming eco update to plot the given data
     * defines ecoData for sugar, cacao, milk and chocolate
     * @param request requestArray sent by the server
     */
    private void handleECOUP(String[] request) {
        for (int i = 12; i > 0; i--) {
            for (int j = 0; j < 6; j++) {
                ecoDataSugar[i][j] = ecoDataSugar[i-1][j];
                ecoDataCacao[i][j] = ecoDataCacao[i-1][j];
                ecoDataMilk[i][j] = ecoDataMilk[i-1][j];
                ecoDataChocolate[i][j] = ecoDataChocolate[i-1][j];
            }
            monthArray[i] = monthArray[i-1];
        }
        newSellPrice[0] = Double.parseDouble(request[1].split(",")[0]);
        newSellPrice[1] = Double.parseDouble(request[2].split(",")[0]);
        newSellPrice[2] = Double.parseDouble(request[3].split(",")[0]);
        newSellPrice[3] = Double.parseDouble(request[4].split(",")[0]);

        newBuyPrice[0] = Double.parseDouble(request[1].split(",")[1]);
        newBuyPrice[1] = Double.parseDouble(request[2].split(",")[1]);
        newBuyPrice[2] = Double.parseDouble(request[3].split(",")[1]);
        newBuyPrice[3] = Double.parseDouble(request[4].split(",")[1]);

        buyLimit[0] = Double.parseDouble(request[1].split(",")[9]);
        buyLimit[1] = Double.parseDouble(request[2].split(",")[9]);
        buyLimit[2] = Double.parseDouble(request[3].split(",")[9]);
        buyLimit[3] = Double.parseDouble(request[4].split(",")[9]);

        for (int j = 0; j < 6; j++) {
            ecoDataSugar[0][j] = request[1].split(",")[j + 2];
            ecoDataCacao[0][j] = request[2].split(",")[j + 2];
            ecoDataMilk[0][j] = request[3].split(",")[j + 2];
            ecoDataChocolate[0][j] = request[4].split(",")[j + 2];
        }
        monthArray[0] = month;
        ecoData[0] = ecoDataSugar;
        ecoData[1] = ecoDataCacao;
        ecoData[2] = ecoDataMilk;
        ecoData[3] = ecoDataChocolate;
    }

    /**
     * handles BLDCK, return after the player wanted to build a building
     * checks if it really was build, else prints out information
     * @param request requestarray sent by the server
     */
    private void handleBLDCK(String[] request) {
        if (request.length == 4) {
            capital = Integer.parseInt(request[3]);
        }
        String building = request[1];
        int level = Integer.parseInt(request[2]);
        switch(building) {
            case "SUGARFARM":
                handleBuildingCases(level, SUGARFARM, "SUGARFARM", "sugarfarm", request, true, GUIGameController.getInstance().sugarBuilding);
                break;
            case "CACAOFARM":
                handleBuildingCases(level, CACAOFARM, "CACAOFARM", "cacaofarm", request, true, GUIGameController.getInstance().cacaoBuilding);
                break;
            case "STABLE":
                handleBuildingCases(level, STABLE, "STABLE", "stable", request, true, GUIGameController.getInstance().milkBuilding);
                break;
            case "FACTORY":
                handleBuildingCases(level, FACTORY, "FACTORY", "factory", request, false, GUIGameController.getInstance().chocolateBuilding);
                break;
            case "LAB":
                handleBuildingCases(level, LAB, "LAB", "laboratory", request, false, GUIGameController.getInstance().labBuilding);
                break;
            case "WAREHOUSE":
                handleBuildingCases(level, WAREHOUSE, "WAREHOUSE", "warehouse", request, false, GUIGameController.getInstance().warehouseBuilding);
                break;
        }

        GUIGameController guiGameController = GUIGameController.getInstance();
        if (guiGameController.levelShown) {
            guiGameController.showLevels();
        }
    }

    /**
     * handles buildcommand sent by the server for a given building
     * @param newLevel new level of this sent by the server
     * @param oldLevel old level of this building
     * @param picture name of the picture to be set on the gamescreen
     * @param building name of the building to print out a info about it
     * @param request requestarray sent by the server
     * @param productionBuilding true if it is a productionBilding, false if it is the lab or the warehouse
     */
    private void handleBuildingCases(int newLevel, int oldLevel, String picture, String building, String[] request, boolean productionBuilding, Button buildingButton) {
        if (newLevel == oldLevel) {
            setBuildingInfo("you don't have enough money or your labratorys level is too low to build a " + building, request);
        } else if (newLevel > oldLevel){
            buildingButton.setVisible(true);
            if (productionBuilding) {
                hasProductionBuilding = true;
            }
            GUIGameController.getInstance().setPicture(picture, newLevel);
            GUIGameController.getInstance().updateResources();
            setBuildingInfo("building " + building + " was successful\nits new level is " + newLevel, request);
        } else {
            GUIGameController.getInstance().setPicture(picture, newLevel);
            GUIGameController.getInstance().updateResources();
            setBuildingInfo("tearing down " + building + " was successful", request);
        }
    }

    /**
     * sets all buildings to zero in case a new game will be started and opens the gameEnd screen
     * @param request requestarray sent by the server
     */
    private void handleGAMND(String[] request) {
        capital = Integer.parseInt(request[1]);
        try {
            GUIGameController.getInstance().handleWin();
        } catch (IOException e) {
            logger.info("couldn't load winScreen");
        }
        logger.info("The Game has ended successfully");
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().sugarView);
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().cacaoView);
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().milkView);
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().chocolateView);
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().labView);
        GUIGameController.getInstance().setOtherBuildingLevelsInvisible(GUIGameController.getInstance().warehouseView);
        SUGARFARM = 0;
        CACAOFARM = 0;
        STABLE = 0;
        FACTORY = 0;
        LAB = 0;
        WAREHOUSE = 0;
    }

    /**
     * writes buildingInfo to buildingscreen if the building was built or not
     * @param info info to be printed out
     * @param request requestarray sent by the server
     */
    private void setBuildingInfo(String info, String[] request) {
        if (request.length == 4) {
            GUIBuildingController.getInstance().setBuildingInfo(info);
        }
    }
    /**
     * handles bstck after buying a booster
     * @param request request sent by the server
     */
    public void handleBSTCK(String[] request) {
        capital = Integer.parseInt(request[2]);
        GUIGameController.getInstance().updateResources();
        switch(request[1]) {
            case "SPY":
                GUIGameController.getInstance().updater.setSpy();
                GUIBoosterController.getInstance().setBoosterInfo("you bought a spy");
                break;
            case "SABOTAGE":
                GUIGameController.getInstance().updater.setSaboteur();
                GUIBoosterController.getInstance().setBoosterInfo("you bought a saboteur");
                break;
            case "DEFAULT":
                if (capital < 5000) {
                    GUIBoosterController.getInstance().setBoosterInfo("you don't have enough capital to buy a spy");
                } else if (capital < 10000 && GUIGameController.getInstance().spyIsActive) {
                    GUIBoosterController.getInstance().setBoosterInfo("you don't have enough capital to buy a saboteur");
                } else if (LAB < 1) {
                    GUIBoosterController.getInstance().setBoosterInfo("you need to have the laboratory on level 1 to buy a spy");
                } else if (LAB < 2) {
                    GUIBoosterController.getInstance().setBoosterInfo("you need to have the laboratory on level 2 to buy a saboteur");
                }
                else {
                    GUIBoosterController.getInstance().setBoosterInfo("couldn't buy spy because of unknown reasons");
                }
                break;
        }
    }

    /**
     * handles SELCK command sent by the server
     * gives info to the player if the sale was successful
     * @param request request sent by the server
     */
    private void handleSELCK(String[] request) {
        sellCheck = request;
        if (request[2].equals("0")) {
            logger.info("selling " + request[1] + " wasn't successful");
        }
        switch(request[1]) {
            case "SUGAR":
                if(Integer.parseInt(request[2]) < 0) {
                    productCheckIn[0] = false;
                } else {
                    productCheckOut[0] = false;
                }
                break;
            case "MILK":
                if(Integer.parseInt(request[2]) < 0) {
                    productCheckIn[1] = false;
                } else {
                    productCheckOut[1] = false;
                }
                break;
            case "CACAO":
                if(Integer.parseInt(request[2]) < 0) {
                    productCheckIn[2] = false;
                } else {
                    productCheckOut[2] = false;
                }
                break;
        }
    }

    /**
     * opens socket as a connection to the server
     */
    private void openSocket() {
        try {
            socket = new Socket(Main.ip, Main.port);
            out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            runningSocket = true;
        } catch (ConnectException e) {
            logger.fatal("Client couldn't open socket!");
            try {
                GUILoginController.getInstance().setNoServerWarning();
            } catch (Exception exception){
                logger.info("couldn't set noServerwarning");
            }
            if (!startedLoop) {
                while (!runningSocket) {
                    startedLoop = true;
                    openSocket();
                    Main.commandHandler.handleCommand("login");
                    //start PingSender
                    PingSender pingSender = new PingSender(Main.ip, Main.scheduler);
                    Main.scheduler.scheduleAtFixedRate(pingSender, 3, 2, TimeUnit.SECONDS);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            logger.fatal("problems with opening the socket");
            e.printStackTrace();
        }
    }
}
