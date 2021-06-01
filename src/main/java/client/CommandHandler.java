package client;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.BuildingStats;
import server.NetworkProtocol;
import server.Resources;

/**
 * commands written by other classes are sent to the server
 */
public class CommandHandler {

    String[] sendArray = null;
    static Logger logger = LogManager.getLogger(CommandHandler.class);

    /**
     * handles commands sent entered in the console or sent by other classes
     * handles them properly by sending commands to the server or using other classes
     * @param msg message sent by other classes or entered in the console
     */
    public void handleCommand(String msg) {
        if (!msg.equals("")) {
            String[] request = msg.split(" ");
            String command = request[0];

            switch (command) {
                case "build": //"build <building>" to build a new building of the entered type
                    handleBuild(request, 1);
                    break;
                case "tear": //"tear <building>" to tear down the entered building
                    handleBuild(request, -1);
                    break;
                case "send": //"send <message>" to send a broadcast message to every player
                    handleSend(request);
                    break;
                case "whisper": //"whisper <message> to <addressee>" to send a whisperchat message to the entered addressee
                    handleWhisper(request);
                    break;
                case "group": //"group message <message>" to send a message only to the group your in
                    handleGroup(request);
                    break;
                case "lobby": //"lobby message <message>" to send a message ony to the lobby
                    handleLobby(request);
                    break;
                case "sell": //"sell <number> <product> <price>" to sell (positive number) or buy (negative number) a product
                    handleSell(request);
                    break;
                case "spy": //"spy -username-" to spy out the entered player
                    String playerName = getPlayerName(request);
                    sendArray = new String[2];
                    sendArray[0] = NetworkProtocol.SPYTO.toString();
                    sendArray[1] = playerName;
                    break;
                case "sabot": // "sabot -building- -username- to sabotage a other players building if you have a spy with sabot function
                    sendArray = new String[3];
                    sendArray[0] = NetworkProtocol.SABOT.toString();
                    sendArray[1] = getName(request, 2);
                    sendArray[2] = request[1].toUpperCase();
                    break;
                case "login": //"login" to log in with the entered username (!not for console because provUsername has to be set before)
                    sendArray = new String[2];
                    sendArray[0] = NetworkProtocol.LOGIN.toString();
                    sendArray[1] = Main.provUsername;
                    break;
                case "new": //"new group <groupname>" to make a new group with the entered groupname
                    if (checkArguments(3, request)) {
                        sendArray = new String[3];
                        sendArray[0] = NetworkProtocol.GROUP.toString();
                        sendArray[1] = "NEW";
                        sendArray[2] = getName(request, 2);
                    }
                    break;
                case "add": //"add <player> " to add a player to a existing group
                    if (checkArguments(2, request)) {
                        String plName = getPlayerName(request);
                        sendArray = new String[3];
                        sendArray[0] = NetworkProtocol.GROUP.toString();
                        sendArray[1] = "ADD";
                        sendArray[2] = plName;
                    }
                    break;
                case "join": //"join <goupname>" to join a existing group
                    if (checkArguments(2, request)) {
                        sendArray = new String[3];
                        sendArray[0] = NetworkProtocol.GROUP.toString();
                        sendArray[1] = "JOIN";
                        sendArray[2] = getName(request, 1);
                    }
                    break;
                case "leave": //"leave" to leave your current group
                    sendArray = new String[3];
                    sendArray[0] = NetworkProtocol.GROUP.toString();
                    sendArray[1] = "LEAVE";
                    break;
                case "start": //"start game -highscore- -duration in month- -monthlength-" to start the game with highscore limit and month limit. If no highscore or no duration are chosen, enter a extremely high number intead, if no monthlengh is chosen, enter 300 if neither highscore nor duration and monthlength are chosen, send only "start game" so the standard settings are chosen.
                    if (checkArguments(5, request) || checkArguments(2, request)) {
                        if (checkArguments(5, request)) {
                            if (testIfNumber(request[2], false) && testIfNumber(request[3], false) && testIfNumber(request[4], false)) {
                                sendArray = new String[4];
                                sendArray[1] = request[2];
                                sendArray[2] = request[3];
                                sendArray[3] = request[4];
                            }
                        } else {
                            sendArray = new String[1];
                        }
                        sendArray[0] = NetworkProtocol.GAMGO.toString();
                    } else {
                        System.out.println("(start) incomplete command");
                        logger.warn("incomplete written command");
                    }
                    break;
                case "playerlistAll": //"playerlistAll" to send a command to the server sending a list of every player which will be printed out
                    sendArray = new String[1];
                    sendArray[0] = NetworkProtocol.GETPL.toString();
                    break;
                case "grouplist": //"grouplist" to send a command to the server sending a list of every group which will be printed out
                    sendArray = new String[1];
                    sendArray[0] = NetworkProtocol.GETGL.toString();
                    break;
                case "topScores": //"topHighscores" to get the top three scores
                    sendArray = new String[1];
                    sendArray[0] = NetworkProtocol.GETHS.toString();
                    break;
                case "gamgo": //"gamgo" to start a game without arguments
                    sendArray = new String[1];
                    sendArray[0] = NetworkProtocol.GAMGO.toString();
                    break;
                case "boost": //"boost -object-" to buy a booster
                    sendArray = new String[2];
                    sendArray[0] = "BOOST";
                    sendArray[1] = request[1].toUpperCase();
                    break;
                default:
                    logger.warn("invalid command: "+ command);
            }
            if (sendArray != null) {
                send(sendArray);
                sendArray = null;
            }
        }
    }

    /**
     * handles outgoing broadcast messages
     * @param request entered request with message
     */
    private void handleSend(String[] request) {
        sendArray = new String[3];
        sendArray[0] = NetworkProtocol.CHATO.toString();
        sendArray[1] = "BC";
        StringBuilder bcMessageBuilder = new StringBuilder();
        for(int i = 1; i < request.length; i++) {
            bcMessageBuilder.append(request[i]);
            if (i < request.length - 1) {
                bcMessageBuilder.append(" ");
            }
        }
        sendArray[2] = bcMessageBuilder.toString();
    }

    /**
     * handles outgoing whisper messages
     * @param request entered request with message and receiver
     */
    private void handleWhisper(String[] request) {
        if (checkArguments(3, request)) {
            sendArray = new String[3];
            sendArray[0] = NetworkProtocol.CHATO.toString();
            StringBuilder messageBuilder = new StringBuilder();
            int counter = 1;
            for (int i = 1; !request[i].equals("to"); i++) {
                counter++;
                messageBuilder.append(request[i]).append(" ");
            }
            sendArray[2] = messageBuilder.toString();
            sendArray[1] = getAddressee(counter, request);
        }
    }

    /**
     * handles outgoing group messages
     * @param request entered request with message
     */
    private void handleGroup(String[] request) {
        if (checkArguments(3, request)) {
            sendArray = new String[3];
            sendArray[0] = NetworkProtocol.CHATO.toString();
            StringBuilder grMessageBuilder = new StringBuilder();
            for(int i = 2; i < request.length; i++) {
                grMessageBuilder.append(request[i]);
                if (i < request.length - 1) {
                    grMessageBuilder.append(" ");
                }
            }
            sendArray[1] = "GRP";
            sendArray[2] = grMessageBuilder.toString();
        }
    }

    /**
     * handles outgoing lobby messages
     * @param request entered request with message
     */
    private void handleLobby(String[] request) {
        if (checkArguments(3, request)) {
            sendArray = new String[3];
            sendArray[0] = NetworkProtocol.CHATO.toString();
            sendArray[1] = "LOBBY";
            StringBuilder loMessageBuilder = new StringBuilder();
            for(int i = 2; i < request.length; i++) {
                loMessageBuilder.append(request[i]);
                if (i < request.length - 1) {
                    loMessageBuilder.append(" ");
                }
            }
            sendArray[2] = loMessageBuilder.toString();
        }
    }

    /**
     * handles sell and buy commands
     * doesn't send the buy command if the player has already selled the same product in the same month
     * doesn't send the sell command if the player has already buyed the same product in the same month
     * @param request entered request with the product, the amount and the price
     */
    private void handleSell(String[] request) {
        if (testIfNumber(request[1], false) && checkArguments(4, request)) {
            sendArray = new String[4];
            sendArray[0] = NetworkProtocol.SELTO.toString();
            switch (request[2]) {
                case "chocolate":
                    sendArray[1] = Resources.CHOCO.name();
                    break;
                case "sugar":
                    handleSellCases(Resources.SUGAR.name(), 0, Integer.parseInt(request[1]), request[2]);
                    break;
                case "cacao":
                    handleSellCases(Resources.CACAO.name(), 1, Integer.parseInt(request[1]), request[2]);
                    break;
                case "milk":
                    handleSellCases(Resources.MILK.name(), 2, Integer.parseInt(request[1]), request[2]);
                    break;
                default:
                    System.out.println("invalid product");
                    logger.warn("invalid product was written");
            }
            if (sendArray != null) {
                sendArray[2] = request[1];
                sendArray[3] = request[3];
            }
        } else {
            logger.warn("(sell) incomplete command" + Arrays.toString(request));
        }
    }
    /**
     * handles the cases cacao, sugar and milk in the sell case
     * sets the product argument in the sendArray to the right product and checks if the player has already selled or bought the same product in this month
     * @param product the product one wanted to sell/buy
     * @param index products index of the productCheck array
     * @param amount amount of product one wants to sell/buy to check if it's selling or buying
     * @param productName name of the Product to give a proper outprint
     */
    private void handleSellCases(String product, int index,  int amount, String productName) {
        if (amount < 0) {
            if (Main.connectionHandler.productCheckOut[index]) {
                sendArray[1] = product;
                Main.connectionHandler.productCheckIn[index] = false;
            } else {
                sendArray = null;
                System.out.println("you cannot buy " + productName + " because you already sold it this month");
                logger.warn("product was tried to be bought, which was already sold for this month");
            }
        } else if (amount > 0) {
            if (Main.connectionHandler.productCheckIn[index]) {
                sendArray[1] = product;
                Main.connectionHandler.productCheckOut[index] = false;
            } else {
                sendArray = null;
                System.out.println("you cannot sell " + productName + " because you already bought it this month");
                logger.warn("product was tried to be sold, but this month it got bought already");
            }
        } else {
            Main.connectionHandler.productCheckIn[index] = true;
            Main.connectionHandler.productCheckOut[index] = true;
            sendArray[1] = product;
            logger.warn("You sent a zero sell offer so all your sell and buy offers got canceled");
        }
    }

    /**
     * sends the sendArray String to the server in the right format
     * @param arguments arguments which have to be sent to the server
     */
    private void send(String[] arguments) {
        StringBuilder toSend = new StringBuilder();
        for (int i = 0; i < arguments.length - 1; i++) {
            toSend.append(arguments[i]).append(Main.connectionHandler.SEP);
        }
        toSend.append(arguments[arguments.length- 1]).append(Main.connectionHandler.END);
        Main.connectionHandler.sendMessage(toSend.toString());
    }

    /**
     * tests if a parameter is a number to decide if the command is usable
     * @param number parameter which should be a number
     * @param positive true if is number
     * @return boolean being true if number was a number and false if it wasn't
     */
    public boolean testIfNumber(String number, boolean positive) {
        boolean isNumeric = true;
        for (int i = 0; i < number.length(); i++) {
            if (positive) {
                if (!Character.isDigit(number.charAt(i))) {
                    isNumeric = false;
                    break;
                }
            } else {
                if (!Character.isDigit(number.charAt(i)) && number.charAt(i) != '-') {
                    isNumeric = false;
                    break;
                }
            }
        }
        return isNumeric;
    }

    /**
     * used in the cases new, add and join
     * reads out the name of the group by combining all of the array elements belonging to the name
     * @param request stringarray of the entered request
     * @param arguments number of arguments before the name begins
     * @return groupname as a string with spaces instead of separated array elements
     */
    private String getName(String[] request, int arguments) {
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = arguments; i < request.length; i++) {
            nameBuilder.append(request[i]);
            if (i != request.length - 1) {
                nameBuilder.append(" ");
            }
        }
        return nameBuilder.toString();
    }

    /**
     * gives back the addressee of the message
     * @param startPoint index of the first addresseeword
     * @param request requestarray
     * @return addressee
     */
    private String getAddressee(int startPoint, String[] request) {
        StringBuilder addresseeBuilder = new StringBuilder();
        for (int i = startPoint + 1; i < request.length; i++) {
            addresseeBuilder.append(request[i]);
            if (i != request.length - 1) {
                addresseeBuilder.append(" ");
            }
        }
        return addresseeBuilder.toString();
    }

    /**
     * used in the case add
     * reads out the name of the player
     * @param request stringarray of the entered request
     * @return stringarray with a[0] being the name and a[1] being the element number of the word "to" which is used in the metode getName
     */
    private String getPlayerName(String[] request) {
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 1; i < request.length; i++) {
            nameBuilder.append(request[i]);
            if (i != request.length - 1) {
                nameBuilder.append(" ");
            }
        }
        return nameBuilder.toString();
    }

    /**
     * checks if the entered request has enough arguments
     * @param numberOfArguments required number of arguments
     * @param request stringarray of the entered request
     */
    private boolean checkArguments(int numberOfArguments, String[] request) {
        return numberOfArguments <= request.length;
    }

    /**
     * sends command for building the building to the server
     * @param request entered requestarray
     * @param arg2 1 for build and -1 for tear
     */
    private void handleBuild(String[] request, int arg2) {
        sendArray = new String[3];
        sendArray[0] = NetworkProtocol.BUILD.toString();
        if (checkArguments(2, request)) {
            switch (request[1]) {
                case "chocolate":
                    sendArray[1] = "FACTORY";
                    break;
                case "sugar":
                    sendArray[1] = "SUGARFARM";
                    break;
                case "milk":
                    sendArray[1] = "STABLE";
                    break;
                case "cacao":
                    sendArray[1] = "CACAOFARM";
                    break;
                case "laboratory":
                    if (Main.connectionHandler.hasProductionBuilding) {
                        sendArray[1] = "LAB";
                    } else {
                        GUIBuildingController.getInstance().setNoProductionBuildingWarning();
                        sendArray = null;
                    }
                    break;
                case "warehouse":
                    sendArray[1] = "WAREHOUSE";
                    break;
                default:
                    System.out.println("invalid building");
                    logger.warn("invalid building");
            }
        } else {
            System.out.println("(build/tear down) incomplete command" + Arrays.toString(request));
            logger.warn("incomplete command was written/used");
        }
        if (sendArray != null) {
            sendArray[2] = "" + arg2;
        }
    }

    /**
     * @param building building one wants to get back the production
     * @param level level of this building (1,2,3)
     * @return production per month for the given building on the given level
     */
    public int getProduction(String building, int level) {
        String buildingLevel = building + level;
        int production = 0;
        switch(buildingLevel) {
            case "FACTORY1":
                production =  BuildingStats.FACTORY1.productionPerMonth;
                break;
            case "FACTORY2":
                production =  BuildingStats.FACTORY2.productionPerMonth;
                break;
            case "FACTORY3":
                production =  BuildingStats.FACTORY3.productionPerMonth;
                break;
            case "SUGARFARM1":
                production =  BuildingStats.SUGARFARM1.productionPerMonth;
                break;
            case "SUGARFARM2":
                production =  BuildingStats.SUGARFARM2.productionPerMonth;
                break;
            case "SUGARFARM3":
                production =  BuildingStats.SUGARFARM3.productionPerMonth;
                break;
            case "STABLE1":
                production =  BuildingStats.STABLE1.productionPerMonth;
                break;
            case "STABLE2":
                production =  BuildingStats.STABLE2.productionPerMonth;
                break;
            case "STABLE3":
                production =  BuildingStats.STABLE3.productionPerMonth;
                break;
            case "CACAOFARM1":
                production =  BuildingStats.CACAOFARM1.productionPerMonth;
                break;
            case "CACAOFARM2":
                production =  BuildingStats.CACAOFARM2.productionPerMonth;
                break;
            case "CACAOFARM3":
                production =  BuildingStats.CACAOFARM3.productionPerMonth;
                break;
        }
        return production;
    }
}