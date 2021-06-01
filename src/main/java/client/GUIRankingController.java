package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIRankingController implements Initializable {

    @FXML Label rankingList, gamgoDataLabel;
    String text = "";
    @FXML Button showHighscoreButton;

    /**
     * sets highscorelist to the list sent by the server
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamgoDataLabel.setText("\nHighscore: " + GUILobbyController.getInstance().highscore + "\n\ngamelength: " +
                GUILobbyController.getInstance().gamelength + "\n\nmonthlength: " + GUILobbyController.getInstance().monthlength);
        for (int i = 0; i < Main.connectionHandler.playerAndScoreList.length; i++) {
            text += Main.connectionHandler.playerAndScoreList[i].split(",")[0] + ":   " +
                    Main.connectionHandler.playerAndScoreList[i].split(",")[1] + '\n';
        }
        rankingList.setText(text);
    }

    /**
     * handles showHighscoreButton opening highscorewindow
     * @param event is a date event
     * @throws IOException if it cant finde the file
     */
    @FXML
    public void showHighscore(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIHighscore.fxml"));
        stage.setTitle("Chocolate Cartel highscore");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(showHighscoreButton.getScene().getWindow());
        stage.showAndWait();
    }
}
