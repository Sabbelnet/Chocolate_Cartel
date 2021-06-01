package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

public class GUISabotageController implements Initializable {

    @FXML ListView<String> buildingList;
    @FXML Button sabotageButton, showBuildingsButton;
    @FXML Label warning, sabotageAnswer;
    @FXML ComboBox<String> playerList;

    String player;
    String building;

    /**
     * Initializes the Controller
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        getGroupList();
        sabotageButton.setVisible(false);
    }

    /**
     * fills playerlist with all players in the group
     */
    @FXML
    void getBuildingList() {
        player = playerList.getSelectionModel().getSelectedItem();
        sabotageButton.setVisible(true);
        if (player != null) {
            Main.commandHandler.handleCommand("spy " + player);
            Updater.spyForSabotage = true;
        } else {
            showBuildingsButton.setText("choose a player!");
        }
    }

    /**
     * writes the buildinglist of the chosen player into the buildinglist listview
     */
    public void setBuildingList() {
        Updater.spyForSabotage = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] buildings = new String[6];
                boolean hasABuilding = false;
                System.out.println(Arrays.toString(Main.connectionHandler.spyData));
                for (int i = 0; i < Main.connectionHandler.spyData.length - 2; i++) {
                    if (Integer.parseInt(Main.connectionHandler.spyData[i + 2]) > 0) {
                        buildings[i] = GUISpyController.getInstance().description[i];
                        hasABuilding = true;
                    } else {
                        buildings[i] = "";
                    }
                }
                if(hasABuilding) {
                    ArrayList<String> buildingListArrayList = new ArrayList<>(Arrays.asList(buildings));
                    ObservableList<String> itemsBuilding = FXCollections.observableArrayList(buildingListArrayList);
                    buildingList.setItems(itemsBuilding);
                } else {
                    ArrayList<String> buildingListArrayList = new ArrayList<>(Collections.singletonList("player has no buildings yet"));
                    ObservableList<String> itemsBuilding = FXCollections.observableArrayList(buildingListArrayList);
                    buildingList.setItems(itemsBuilding);
                }
            }
        });
    }

    /**
     * sends spycommand to server with the chosen player
     */
    @FXML
    void handleSabotageButton() {
        if (buildingList.getSelectionModel().getSelectedItem() == null || buildingList.getSelectionModel().getSelectedItem().equals("")) {
            warning.setVisible(true);
        } else {
            building = buildingList.getSelectionModel().getSelectedItem();
            Main.commandHandler.handleCommand("sabot " + building + " " + player);
            sabotageButton.setVisible(false);
        }
    }

    /**
     * prints out info about how the sabotage went
     * @param sabData info to print out
     */
    public void setSabData(String sabData) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (sabData.equals("0")) {
                    sabotageAnswer.setText("you don't have enough capital to sabotage");
                } else {
                    sabotageAnswer.setText("your sabotage was successful");
                }

            }
        });
    }

    /**
     * shows player list
     */
    void getGroupList() {
        ArrayList<String> groupListArrayList = new ArrayList<>(Arrays.asList(Main.connectionHandler.groupPlayerlist));
        groupListArrayList.remove(Main.username);
        groupListArrayList.remove("UPDAT");
        ObservableList<String> itemsGroup = FXCollections.observableArrayList(groupListArrayList);
        playerList.setItems(itemsGroup);
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUISabotageController instance;

    /**
     * gets instance
     */
    public GUISabotageController() {
        instance = this;
    }

    /**
     * @return the instance
     */
    public static GUISabotageController getInstance() {
        return instance;
    }
}