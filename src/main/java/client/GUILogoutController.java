package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * if player hits logout-button, this window shows
 * player has option to logout or go back to the scene
 */
public class GUILogoutController {

    @FXML private Button logoutNo;
    @FXML private Button logoutYes;

    /**
     * player choses to logout, client shuts down
     * @param event button
     */
    @FXML
    private void logoutYesClick(ActionEvent event) {
        Stage stage = (Stage) logoutYes.getScene().getWindow();
        stage.close();
        Main.connectionHandler.stopConnection(true);
    }

    /**
     * leads player back to scene
     * @param event button
     */
    @FXML
    public void logoutNoClick(ActionEvent event) {
        Stage stage = (Stage) logoutNo.getScene().getWindow();
        stage.close();
    }
}

