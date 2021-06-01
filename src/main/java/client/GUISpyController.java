package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GUISpyController implements Initializable {

    @FXML ListView<String> playerList;
    @FXML Button spyButton, sabotageButton;
    @FXML Label warning, spyDataLabel, noSpyLabel;
    @FXML VBox spyContent;

    String player;
    String[] description = {"sugarfarm", "cacaofarm", "stable", "factory", "warehouse", "lab"};

    /**
     * Initilize the controller
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        getPlayerList();
        if (GUIGameController.getInstance().spyIsActive) {
            noSpyLabel.setVisible(false);
            spyContent.setVisible(true);
        }
        if (GUIGameController.getInstance().saboteurIsActive) {
            sabotageButton.setVisible(true);
        }
    }

    /**
     * fills playerlist with all players in the group
     */
    void getPlayerList() {
        ArrayList<String> playerListArrayList = new ArrayList<>(Arrays.asList(Main.connectionHandler.groupPlayerlist));
        playerListArrayList.remove(Main.username);
        playerListArrayList.remove(null);
        ObservableList<String> itemsPlayer = FXCollections.observableArrayList(playerListArrayList);
        playerList.setItems(itemsPlayer);
    }

    /**
     * sends spycommand to server with the chosen player
     * @param event
     */
    @FXML
    void handleSpyButton(ActionEvent event) {
        if (playerList.getSelectionModel().getSelectedItem() == null) {
            warning.setVisible(true);
        } else {
            player = playerList.getSelectionModel().getSelectedItem();
            Main.commandHandler.handleCommand("spy " + player + "\n");
        }
    }

    /**
     * handles the Sabotage Button
     * @param actionEvent mouseclick
     * @throws IOException if there is no fxml
     */
    public void handleSabotageButton(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUISabotage.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Chocolate Cartel sabotage");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.initOwner(sabotageButton.getScene().getWindow());
        stage.show();
    }

    /**
     * sends the spydate to the player
     * @param spyData from enemy player
     */
    public void setSpyData(String[] spyData) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String spy = "";
                spy += spyData[1] + "\n";
                for (int i = 2; i < spyData.length; i++) {
                    spy += description[i - 2] + ":   " + spyData[i] + "\n";
                }
                spyDataLabel.setText(spy);
            }
        });
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUISpyController instance;

    /**
     * gets instance
     */
    public GUISpyController() {
        instance = this;
    }

    /**
     * @return instance
     */
    public static GUISpyController getInstance() {
        return instance;
    }
}