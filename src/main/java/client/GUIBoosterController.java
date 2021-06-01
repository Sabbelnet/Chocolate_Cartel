package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIBoosterController implements Initializable {
    String booster;
    @FXML Button buyButton;
    @FXML Label boosterInfo, noBuildingWarning;
    @FXML VBox contentVBox;

    /**
     * initializes the GUIBoosterController
     * @param location location from player
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Main.connectionHandler.hasProductionBuilding) {
            noBuildingWarning.setVisible(false);
            contentVBox.setVisible(true);
        }
    }

    /**
     * sends the command to commandhandler
     */
    public void buyBooster() {
        Main.commandHandler.handleCommand("boost " + booster);
    }

    /**
     * checks if you bought a spy if not lets you buy one
     * @param actionEvent is a mouseclick
     */
    public void setSpy(ActionEvent actionEvent) {
        if (GUIGameController.getInstance().spyIsActive) {
            boosterInfo.setText("you have already bought the spy");
        }
        booster = "spy";
        buyButton.setText("buy spy");
        buyButton.setVisible(true);
        boosterInfo.setText("spy\n\nprice: 5000\nbenefit: you can spy your opponents to find out which buildings they have built");
    }

    /**
     * method to buy a saboteur
     * @param actionEvent mousclick
     */
    public void setSaboteur(ActionEvent actionEvent) {
        if (GUIGameController.getInstance().spyIsActive) {
            booster = "sabotage";
            buyButton.setText("buy saboteur");
            buyButton.setVisible(true);
            boosterInfo.setText("saboteur\n\nprice: 10000\nbenefit: you can sabotage your opponents by tearing down one of their buildings\nevery sabotage costs an additional 10000");
        } else {
            boosterInfo.setText("you need to have already bought a spy to buy this booster");
        }
    }

    /**
     * gives you the info from enemys
     * @param info informations from other players
     */
    public void setBoosterInfo(String info) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boosterInfo.setText(info);
            }
        });
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUIBoosterController instance;

    /**
     * makes an instance
     */
    public GUIBoosterController() {
        instance = this;
    }

    /**
     * @return instance
     */
    public static GUIBoosterController getInstance() {
        return instance;
    }
}
