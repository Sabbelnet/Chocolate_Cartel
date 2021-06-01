package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * is called by client/Main
 * starts GUI
 */
public class GUIStart extends Application implements Runnable {

    public static Stage mainStage;

    /**
     * starts the stage
     * @param stage Loginstage
     * @throws Exception if there is no fxml, no server...
     */
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILogin.fxml"));
        Scene scene = new Scene(root);
        mainStage.setTitle("Chocolate Cartel Login");
        mainStage.getIcons().add(new Image("logo_freigestellt.png"));
        mainStage.setScene(scene);
        GUIStart.mainStage.setOnCloseRequest(e -> {
            try {
                GUIStart.getInstance().handleLogout();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        mainStage.show();
    }

    /**
     * if player wants to close window in GUI, player will be logged out
     * @throws Exception fxml
     */
    @FXML
    public void handleLogout() throws Exception{
        Main.connectionHandler.stopConnection(true);
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUIStart instance;

    /**
     * gets instance
     */
    public GUIStart() {
        instance = this;
    }

    /**
     * @return instance
     */
    public static GUIStart getInstance() {
        return instance;
    }

    /**
     * starts GUI for player
     */
    @Override
    public void run() {
        Application.launch(GUIStart.class);
    }
}