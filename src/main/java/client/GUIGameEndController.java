package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * window shows when game is finished
 * player can choose to go back to lobby or log out
 */
public class GUIGameEndController implements Initializable {

    @FXML ImageView cupPicture;
    @FXML Label endText;
    @FXML Button logout, showHighscoreButton;
    MediaPlayer mediaPlayer;

    /**
     * initializes the GameEndScreen
     * @param location location
     * @param resources ressources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GUIGameController.getInstance().mediaPlayer.setVolume(0);
        findYourScore();
        String info = "";
        if (Main.username.equals(Main.connectionHandler.groupPlayerlist[0])) {
            info += "YOU WIN\n\nyour score: " + Main.connectionHandler.capital + "\n\n";
            cupPicture.setVisible(true);
            Media win = null;
            try {
                win = new Media(GUIGameController.class.getResource("/sounds/win.wav").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mediaPlayer = new MediaPlayer(win);
            mediaPlayer.setVolume(0.2);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
        } else {
            info += "GAME OVER\nyour score:" + Main.connectionHandler.capital + "\n\n";
            Media loose = null;
            try {
                loose = new Media(GUIGameController.class.getResource("/sounds/LoosingSound.mp3").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mediaPlayer = new MediaPlayer(loose);
            mediaPlayer.setVolume(0.2);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
        }
        info += "result\n";
        for (int i = 0; i < Main.connectionHandler.playerAndScoreList.length; i++) {
            info += Main.connectionHandler.playerAndScoreList[i].split(",")[0] + ":    " + Main.connectionHandler.playerAndScoreList[i].split(",")[1] + "\n";
        }
        endText.setText(info);
    }

    /**
     * leads player back to lobby
     * @throws IOException load fxml
     */
    @FXML
    void handleBackToLobby() throws IOException {
        Main.connectionHandler.gameIsRunning = false;
        GUILobbyController.gameWasPlayed = true;
        Parent lobby = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILobby.fxml"));
        Scene lobbyScene = new Scene(lobby);
        GUIStart.mainStage.setTitle("Chocolate Cartel Lobby");
        GUIStart.mainStage.getIcons().add(new Image("logo_freigestellt.png"));
        GUIStart.mainStage.setScene(lobbyScene);
        Main.commandHandler.handleCommand("grouplist");
        Updater.getGrouplist = true;
        GUILobbyController.inGroup = true;
        GUIStart.mainStage.show();
    }

    /**
     * logs player out, closes window
     */
    @FXML
    void handleLogout() {
        Main.commandHandler.handleCommand("logout");
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
    }

    /**
     * handles showHighscoreButton opening highscorewindow
     * @param event is an date event
     * @throws IOException if there are no file
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

    /**
     * finds and shows your score
     */
    private void findYourScore() {
        for (int i = 0; i < Main.connectionHandler.playerAndScoreList.length; i++) {
            String[] player = Main.connectionHandler.playerAndScoreList[i].split(",");
            if (player[0].equals(Main.username)) {
                Main.connectionHandler.capital = Integer.parseInt(player[1]);
            }
        }
    }
}
