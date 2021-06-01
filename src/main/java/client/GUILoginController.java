package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * handles GUILogin.fxml
 * player can choose username
 * leads to lobby scene
 */
public class GUILoginController implements Initializable {

    @FXML Button go, keepCommandComputerUsername;
    @FXML TextField typedUsername;
    @FXML Text commandUsername;
    @FXML Label noServerWarning;

    /**
     * Initialization
     * @param location location
     * @param resources ressources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText();
    }

    /**
     * warning message if there is no Server
     */
    public void setNoServerWarning() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                noServerWarning.setText("no server!\n will log in as soon as there is a server");
                noServerWarning.setVisible(true);
            }
        });
    }

    /**
     * takes command username as an option
     */
    void setText() {
        typedUsername.setPromptText("enter your username");
        typedUsername.setFocusTraversable(false);
        commandUsername.setText(Main.provUsername);
    }

    /**
     * user chooses random typed in username
     * leads user to lobby
     */
    public void handleGoClick() {
        if (!typedUsername.getText().equals("")) {
            Main.provUsername = typedUsername.getText();
            Main.commandHandler.handleCommand("login");
        }
    }

    /**
     * user chooses typed command username, if no typed username, program takes computer name
     * leads user to lobby
     * @param actionEvent leads to new window
     * @throws IOException FXMLLoader.load() could throw exception
     */
    public void handleCommandComputerClick(ActionEvent actionEvent) throws IOException {
        Main.commandHandler.handleCommand("login");
    }

    /**
     * if server sends checked name, lobby sccene shows
     */
    public void handleNamck() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent lobby = null;
                try {
                    lobby = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILobby.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene lobbyScene = new Scene(lobby);
                GUIStart.mainStage.setTitle("Chocolate Cartel Lobby");
                GUIStart.mainStage.getIcons().add(new Image("logo_freigestellt.png"));
                GUIStart.mainStage.setScene(lobbyScene);
                GUIStart.mainStage.setOnCloseRequest(e -> {
                    try {
                        GUIStart.getInstance().handleLogout();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
                GUIStart.mainStage.show();
            }
        });
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUILoginController instance;

    /**
     * decides instance
     */
    public GUILoginController() {
        instance = this;
    }

    /**
     *
     * @return the instance
     */
    public static GUILoginController getInstance() {
        return instance;
    }
}
