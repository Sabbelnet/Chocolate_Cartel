package server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collection;


public class LoginHandler {

    BiMap<ConnectionHandler, String> playerList;

    /**
     * The Loginhandler stores data of all players and generates lists if needed
     */
    public LoginHandler() {
        playerList = HashBiMap.create();
    }


    /**
     * adds a new player to HashTable playerTable
     * if name is already in the list, numbers from 1 to n are added
     * until name is free.
     *
     * @param playerName playername to check/add
     * @param connectionHandler connectionHandler object for Playername
     * @return choosen playername
     */
    public String addPlayer(String playerName, ConnectionHandler connectionHandler) {
        //System.out.println("Player " + playerName + " wants to log in");
        int i = 1;
        if (playerList.containsValue(playerName)) {
            ConnectionHandler connectionHandler2 = getConnectionHandler(playerName); // connectionhandler that has the playername
            if (!connectionHandler2.isConnected) { //case "reconnecting player"
                System.out.println("*** Reconnecting player " + playerName);
                connectionHandler.ownGroupName = connectionHandler2.ownGroupName;
                connectionHandler.ownGameInstance = connectionHandler2.ownGameInstance;
                connectionHandler.gameStatus = connectionHandler2.gameStatus;
                if (connectionHandler.ownGameInstance != null) {
                    connectionHandler.ownGameInstance.replaceConnectionHandler(
                            connectionHandler2, connectionHandler, playerName);
                }
                playerList.remove(connectionHandler2);
                connectionHandler2.closeThread();
            } else {//case "new player"
                String newPlayerName = playerName;
                while (playerList.containsValue(newPlayerName)) {
                    newPlayerName = playerName + i;
                    i++;
                }
                playerName = newPlayerName;
            }
        }
        playerList.put(connectionHandler, playerName);
        System.out.println("Player " + playerName + " joined the Game");
        return playerName;
    }

    /**
     * remove a player from the game
     * @param connectionHandler adf
     */
    public void removePlayer(ConnectionHandler connectionHandler) {
        playerList.remove(connectionHandler);
    }


    /**
     * get List of all players
     * @return String[] with all players connected to the server
     */
    public String[] getPlayerList() {
        Collection<String> collection = playerList.values();
        return collection.toArray(new String[0]);
    }

    /**
     * getName is used to get a Playername for a connectionhandler
     * @param connectionHandler this players's client's object
     * @return The players name
     */
    public String getName(ConnectionHandler connectionHandler) {
        return playerList.get(connectionHandler);
    }


    /**
     * get Connectionhandler from name by using inverse() from Guava
     * @param playerName player that asks for connectionhandler
     * @return ConnectionHandler object for corresponding playerName
     */
    public ConnectionHandler getConnectionHandler(String playerName) {
        ConnectionHandler connectionHandler;

        connectionHandler = playerList.inverse().get(playerName);
        return connectionHandler;
    }
}
