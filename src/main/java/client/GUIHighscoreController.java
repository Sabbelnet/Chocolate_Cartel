package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * handles GUIHighscore.fxml
 * shows highscore list (top three) in game if window requested
 */
public class GUIHighscoreController implements Initializable {

    @FXML ListView topHighscoresListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getHighscores();
        } catch(Exception e) {
            System.out.println("couldn't get Highscores");
        }
        setHighscores();
    }

    /**
     * gets list of top three scores
     */
    void getHighscores() {
        Main.commandHandler.handleCommand("topScores");
    }

    public void setHighscores() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> topScores = new ArrayList<>(Arrays.asList(Main.connectionHandler.topScores));
                topScores.remove("GIVHS");
                ObservableList<String> itemsTopScores = FXCollections.observableArrayList(topScores);
                topHighscoresListView.setItems(itemsTopScores);
            }
        });
    }

}
