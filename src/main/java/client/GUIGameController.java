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
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUIGameController implements Initializable {
    public String receiver, message, chatLine;
    Updater updater;
    ArrayList<String> chatArrayList = new ArrayList<>();
    @FXML Button build, booster, buy, sell, ranking, help, logout, sendMessage, spy, showLevelButton, hideLevelButton,
            soundButton, sugarBuilding, cacaoBuilding, milkBuilding, chocolateBuilding, labBuilding, warehouseBuilding;
    Button[] buildingButtons = new Button[6];
    @FXML
    TextField gameMessageField;
    @FXML
    ComboBox<String> messageReceiver;
    @FXML ListView<String> chatMessages;
    @FXML ImageView sugarView1, sugarView2, sugarView3, cacaoView1, cacaoView2, cacaoView3, milkView1, milkView2, milkView3, chocolateView1, chocolateView2, chocolateView3, labView1, labView2, labView3,  warehouseView1, warehouseView2, warehouseView3, backgroundView, cupPicture, winBackground,
            spyView, spyWithChairView, sittingSpyView, spySabView, spyWithChairSabView, sittingSpySabView, millView;
    ImageView[] sugarView = new ImageView[3];
    ImageView[] cacaoView = new ImageView[3];
    ImageView[] milkView = new ImageView[3];
    ImageView[] chocolateView = new ImageView[3];
    ImageView[] labView = new ImageView[3];
    ImageView[] warehouseView = new ImageView[3];
    ImageView[][] buildingViews = new ImageView[6][3];
    @FXML
    Label capital, sugar, milk, cacao, chocolate, sellLabel, buildLabel, buyLabel, spyLabel, rankingLabel, dataLabel, boosterLabel, helpLabel, logoutLabel,
            sugarOfferLabel, cacaoOfferLabel, milkOfferLabel, chocolateOfferLabel;
    @FXML Text usernameGame, sugarLabel, cacaoLabel, milkLabel, chocolateLabel, warehouseLabel, labLabel, dayLabel, monthLabel;
    @FXML Pane backgroundPane;
    @FXML BorderPane borderPane;
    @FXML AnchorPane winPane;
    int dayOfTheMonth = 1;
    DayCounter dayCounter;
    MediaPlayer mediaPlayer;
    boolean levelShown = false;
    double paneHeight = 0;
    boolean saboteurIsActive = false;
    boolean spyIsActive = false;
    double[] width = {0.37, 0.38, 0.4, 0.58, 0.45, 0.46};
    double[] x = {0.45, 0.77, 0.2, 0.03, 0.994, 0.8};
    double[] y = {0.34, 0.08, 0.1, 0.44, 0.33, 0.6};

    /**
     * sets all images of the buildings and the background
     * opens UpdaterGui
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media background = null;
        try {
            background = new Media(GUIGameController.class.getResource("/sounds/bensound-jazzcomedy.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer(background);
        mediaPlayer.setVolume(0);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        backgroundView.setVisible(true);
        usernameGame.setText(Main.username);
        updateResources();

        sugarView[0] = sugarView1; sugarView[1] = sugarView2; sugarView[2] = sugarView3;
        cacaoView[0] = cacaoView1; cacaoView[1] = cacaoView2; cacaoView[2] = cacaoView3;
        milkView[0] = milkView1; milkView[1] = milkView2; milkView[2] = milkView3;
        chocolateView[0] = chocolateView1; chocolateView[1] = chocolateView2; chocolateView[2] = chocolateView3;
        labView[0] = labView1; labView[1] = labView2; labView[2] = labView3;
        warehouseView[0] = warehouseView1; warehouseView[1] = warehouseView2; warehouseView[2] = warehouseView3;
        buildingViews[0] = sugarView; buildingViews[1] = cacaoView; buildingViews[2] = milkView;
        buildingViews[3] = chocolateView; buildingViews[4] = labView; buildingViews[5] = warehouseView;
        buildingButtons[0] = sugarBuilding; buildingButtons[1] = cacaoBuilding; buildingButtons[2] = milkBuilding;
        buildingButtons[3] = chocolateBuilding; buildingButtons[4] = labBuilding; buildingButtons[5] = warehouseBuilding;

        //start updaterGui
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        updater = new Updater(this, scheduler);
        scheduler.scheduleAtFixedRate(updater, 1, 600, TimeUnit.MILLISECONDS);
        getGroupList();

        dayLabel.setVisible(true);
        monthLabel.setVisible(true);
        startDayCounter();

        Parent game1 = null;
        try {
            game1 = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILoadingScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene lobbyScene1 = new Scene(game1);
        GUIStart.mainStage.setScene(lobbyScene1);
        GUIStart.mainStage.show();
    }

    /**
     * handles buildButton opening buildwindow
     * @throws IOException
     */
    @FXML
    private void handleBuild() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIBuild.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Chocolate Cartel build");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(build.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles helpButton opening helpwindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleHelp(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIRules.fxml"));
        stage.setTitle("Chocolate Cartel help");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(help.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles boosterButton opening boosterwindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBooster(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIBooster.fxml"));
        stage.setTitle("Chocolate Cartel booster");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(booster.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles spyButton opening spywindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSpy(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUISpy.fxml"));
        stage.setTitle("Chocolate Cartel spy");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(spy.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles sellButton opening sellwindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSell(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUISell.fxml"));
        stage.setTitle("Chocolate Cartel sell");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(sell.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles highscoreButton opening highscorewindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleRanking(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIRanking.fxml"));
        stage.setTitle("Chocolate Cartel ranking");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ranking.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * handles
     * @param event logoutButton opening logoutwindow
     * @throws IOException
     */
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILogout.fxml"));
        stage.setTitle("Chocolate Cartel logout");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(logout.getScene().getWindow());
        stage.show();
    }

    /**
     * handles buyButton opening buywindow
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBuy(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIBuy.fxml"));
        stage.setTitle("Chocolate Cartel buy");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(buy.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleMonthlyUpdate(ActionEvent event) throws IOException{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIMonthlyUpdate.fxml"));
        stage.setTitle("Chocolate Cartel update");
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(build.getScene().getWindow());
        stage.showAndWait();
    }

    /**
     * shows info about building on the buildingpictures
     */
    @FXML
    public void showLevels() {
        if (Main.connectionHandler.SUGARFARM > 0) {
            sugarLabel.setText("sugarfarm\nLevel " + Main.connectionHandler.SUGARFARM);
            fitBuildingLevelSize(sugarLabel, x[0], y[0]);
        }
        if (Main.connectionHandler.CACAOFARM > 0) {
            cacaoLabel.setText("cacaofarm\nLevel " + Main.connectionHandler.CACAOFARM);
            fitBuildingLevelSize(cacaoLabel, x[1], y[1]);
        }
        if (Main.connectionHandler.STABLE > 0) {
            milkLabel.setText("stable\nLevel " + Main.connectionHandler.STABLE);
            fitBuildingLevelSize(milkLabel, x[2], y[2]);
        }
        if (Main.connectionHandler.FACTORY > 0) {
            chocolateLabel.setText("factory\nLevel " + Main.connectionHandler.FACTORY);
            fitBuildingLevelSize(chocolateLabel, x[3], y[3]);
        }
        if (Main.connectionHandler.LAB > 0) {
            labLabel.setText("laboratory\nLevel " + Main.connectionHandler.LAB);
            fitBuildingLevelSize(labLabel, x[4], y[4]);
        }
        if (Main.connectionHandler.WAREHOUSE > 0) {
            warehouseLabel.setText("warehouse\nLevel " + Main.connectionHandler.WAREHOUSE);
            fitBuildingLevelSize(warehouseLabel, x[5], y[5]);
        }
        showLevelButton.setVisible(false);
        hideLevelButton.setVisible(true);
        levelShown = true;
    }

    /**
     * hides info about buildings on the buildingpictures
     */
    @FXML
    private void hideLevels() {
        sugarLabel.setText("");
        cacaoLabel.setText("");
        milkLabel.setText("");
        chocolateLabel.setText("");
        labLabel.setText("");
        warehouseLabel.setText("");
        showLevelButton.setVisible(true);
        hideLevelButton.setVisible(false);
        levelShown = false;
    }

    /**
     * sets the buildingpictures on the gamescreen according to their level
     * @param building building name
     * @param newLevel level
     */
    @FXML
    public void setPicture(String building, int newLevel) {
        switch (building) {
            case "SUGARFARM":
                Main.connectionHandler.SUGARFARM = newLevel;
                setOtherBuildingLevelsInvisible(sugarView);
                if (newLevel > 0) {
                    sugarView[Main.connectionHandler.SUGARFARM - 1].setVisible(true);
                }
                break;
            case "CACAOFARM":
                Main.connectionHandler.CACAOFARM = newLevel;
                setOtherBuildingLevelsInvisible(cacaoView);
                if (newLevel > 0) {
                    cacaoView[Main.connectionHandler.CACAOFARM - 1].setVisible(true);
                }
                break;
            case "STABLE":
                Main.connectionHandler.STABLE = newLevel;
                setOtherBuildingLevelsInvisible(milkView);
                if (newLevel > 0) {
                    milkView[Main.connectionHandler.STABLE - 1].setVisible(true);
                }
                break;
            case "FACTORY":
                Main.connectionHandler.FACTORY = newLevel;
                setOtherBuildingLevelsInvisible(chocolateView);
                if (newLevel > 0) {
                    chocolateView[Main.connectionHandler.FACTORY - 1].setVisible(true);
                }
                break;
            case "LAB":
                Main.connectionHandler.LAB = newLevel;
                setOtherBuildingLevelsInvisible(labView);
                if (newLevel > 0) {
                    labView[Main.connectionHandler.LAB - 1].setVisible(true);
                }
                break;
            case "WAREHOUSE":
                Main.connectionHandler.WAREHOUSE = newLevel;
                setOtherBuildingLevelsInvisible(warehouseView);
                if (newLevel > 0) {
                    warehouseView[Main.connectionHandler.WAREHOUSE - 1].setVisible(true);
                }
                break;
        }
        if (levelShown) {
            showLevels();
        }
    }

    /**
     * is called by client/ConnectionHandler if there is an incoming message and printed out in chat
     * @param chat string array with message and sender info
     */
    void getIncomingChatGame(String[] chat) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatLine = "message by " + chat[3] + ": " + chat[2];
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
            }
        });
    }

    /**
     * shows player list for chat purpose
     */
    void getGroupList() {
        ArrayList<String> groupListArrayList = new ArrayList<>(Arrays.asList(Main.connectionHandler.groupPlayerlist));
        groupListArrayList.remove(Main.username);
        groupListArrayList.remove("UPDAT");
        groupListArrayList.add("GROUP");
        ObservableList<String> itemsGroup = FXCollections.observableArrayList(groupListArrayList);
        messageReceiver.setItems(itemsGroup);
    }

    /**
     * sends entered messages with command GROUP to server
     */
    @FXML
    void handleOutgoingMessage () {
        receiver = messageReceiver.getSelectionModel().getSelectedItem();
        message = gameMessageField.getText();
        if (receiver != null) {
            if (message.equals("")) {
                gameMessageField.setPromptText("choose receiver");
            } else if (receiver.equals("GROUP")) {
                Main.commandHandler.handleCommand("group message " + message);
                chatLine = "your message to GROUP: " + message;
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                gameMessageField.clear();
            } else {
                Main.commandHandler.handleCommand("whisper " + message + " to " + receiver);
                gameMessageField.setText("");
                chatLine = "your message to " + receiver + ": " + message;
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                gameMessageField.clear();
            }
        } else {
            gameMessageField.setText("");
            gameMessageField.setPromptText("you have to choose an address");
            gameMessageField.setFocusTraversable(false);
        }
    }

    /**
     * updates all the Labels on the GameGui with the Ressources and the Date
     */
    @FXML
    public void updateResources() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                capital.setText(String.valueOf(Main.connectionHandler.capital));
                sugar.setText(String.valueOf(Main.connectionHandler.stock[0]));
                cacao.setText(String.valueOf(Main.connectionHandler.stock[1]));
                milk.setText(String.valueOf(Main.connectionHandler.stock[2]));
                chocolate.setText(String.valueOf(Main.connectionHandler.stock[3]));
            }
        });
    }

    /**
     * shows what product the player offered for which price
     * @param product chocolate, cacao, sugar or milk
     * @param amount offered amount
     * @param price price he wants for the product
     */
    @FXML
    public void updateOffer(String product, String amount, String price) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getLabel(product).setText(amount + " for a price of " + price);
            }
        });
    }

    /**
     * opens big pane in the gamewindow telling one won and showing the ranking
     * @throws IOException IOException
     */
    @FXML
    public void handleWin() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent gameEnd = null;
                try {
                    gameEnd = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIGameEnd.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene lobbyScene = new Scene(gameEnd);
                GUIStart.mainStage.setTitle("Chocolate Cartel win");
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
     * opens a warnwindow if the client lost connection to the server
     */
    @FXML
    public void handleLostConnection() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent lostConnection = null;
                try {
                    lostConnection = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUILostConnection.fxml"));
                } catch (IOException e) {
                    System.out.println("couldn't open lostConnectionScreen");
                }
                Scene lobbyScene = new Scene(lostConnection);
                GUIStart.mainStage.setTitle("Chocolate Cartel lost connection");
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
     * changes volume of the gamesound to 0.2 or 0 after pressing the sound on/off button
     */
    @FXML
    void handleSoundOff() {
        double volume = mediaPlayer.getVolume();
        if(volume == 0) {
            soundButton.setText("sound off");
            mediaPlayer.setVolume(0.2);
        }
        else if(volume == 0.2){
            soundButton.setText("sound on");
            mediaPlayer.setVolume(0);
        }
    }

    /**
     * removes all the labels from the buttons if the mouse touches a new button
     */
    @FXML
    void removeLabel() {
        boosterLabel.setText("");
        spyLabel.setText("");
        buildLabel.setText("");
        sellLabel.setText("");
        buyLabel.setText("");
        helpLabel.setText("");
        logoutLabel.setText("");
        rankingLabel.setText("");
        dataLabel.setText("");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setBoosterLabel() {
        boosterLabel.setText("booster");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setBuildLabel() {
        buildLabel.setText("build");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setBuyLabel() {
        buyLabel.setText("buy");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setDataLabel() {
        dataLabel.setText("data");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setHelpLabel() {
        helpLabel.setText("help");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setRankingLabel() {
        rankingLabel.setText("ranking");
    }

    /**
     * sets buttondescription if button is touched by the mouse
     */
    @FXML
    void setLogoutLabel() {
        logoutLabel.setText("logout");
    }

    @FXML
    void setSellLabel() {
        sellLabel.setText("sell");
    }

    @FXML
    void setSpyLabel() {
        spyLabel.setText("spy");
    }

    @FXML
    void buildCacao(ActionEvent event) {
        build("cacao");
    }

    @FXML
    void buildChocolate(ActionEvent event) {
        build("chocolate");
    }

    @FXML
    void buildLab(ActionEvent event) {
        build("lab");
    }

    @FXML
    void buildMilk(ActionEvent event) {
        build("milk");
    }

    @FXML
    void buildSugar(ActionEvent event) {
        build("sugar");
    }

    @FXML
    void buildWarehouse(ActionEvent event) {
        build("warehouse");
    }

    /**
     * handles the button build and opens the window to choose a building
     * @param building
     */
    private void build(String building) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIBuild.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Chocolate Cartel build " + building);
        stage.getIcons().add(new Image("logo_freigestellt.png"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(build.getScene().getWindow());
        GUIBuildingController guiBuildingController = GUIBuildingController.getInstance();
        switch(building) {
            case "sugar":
                guiBuildingController.buildSugar();
                break;
            case "cacao":
                guiBuildingController.buildCacao();
                break;
            case "milk":
                guiBuildingController.buildMilk();
                break;
            case "chocolate":
                guiBuildingController.buildChocolate();
                break;
            case "lab":
                guiBuildingController.buildLab();
                break;
            case "warehouse":
                guiBuildingController.buildWarehouse();
                break;
        }
        stage.showAndWait();
    }

    /**
     * fits size of buildingLabels to the size of the pane
     * @param buildingLabel label to be resized
     * @param x position
     * @param y position
     */
    private void fitBuildingLevelSize(Text buildingLabel, double x, double y) {
        buildingLabel.setX(paneHeight * x);
        buildingLabel.setY(paneHeight * y);
    }

    /**
     * checks if the screensize has changed and if it has, corrects the size of the background and buildingpictures
     */
    @FXML
    public void fitBuildingSizes() {
        if (paneHeight != backgroundPane.getHeight()) {
            paneHeight = backgroundPane.getHeight();
            backgroundView.setFitHeight(paneHeight);
            backgroundView.setX(0);
            backgroundView.setY(0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 6; j++) {
                    fitSize(buildingViews[j][i], width[j], x[j], y[j]);
                }
            }

            fitSize(millView, 0.17, -0.0005, 0.084);

            for (int i = 0; i < buildingButtons.length; i++) {
                setBuildingButtonSizes(buildingButtons[i], width[i], x[i], y[i]);
            }

            showLevelButton.setLayoutY(paneHeight - showLevelButton.getHeight());
            hideLevelButton.setLayoutY(paneHeight - hideLevelButton.getHeight());
            showLevelButton.setLayoutX(paneHeight * 1.4 - showLevelButton.getWidth());
            hideLevelButton.setLayoutX(paneHeight * 1.4 - hideLevelButton.getWidth());
            updater.spy.setFitWidth(paneHeight * 0.04);
        }
        if (spyIsActive) {
            updater.setSpyPosition(paneHeight);
        }
    }

    /**
     * fits the size of every building to the size of the pane
     * @param building picture to fit
     * @param width width of the building
     * @param x position
     * @param y position
     */
    private void fitSize(ImageView building, double width,  double x, double y) {
        building.setFitWidth(paneHeight * width);
        building.setX(paneHeight * x);
        building.setY(paneHeight * y);
    }

    private void setBuildingButtonSizes(Button button, double width, double x, double y) {
        button.setMinSize(paneHeight * width * 0.9, paneHeight * width * 0.65);
        button.setMaxSize(paneHeight * width * 0.9, paneHeight * width * 0.65);
        button.setLayoutX(paneHeight * x);
        button.setLayoutY(paneHeight * y);
    }

    void setOtherBuildingLevelsInvisible(ImageView[] building) {
        for (int i = 0; i < 3; i++) {
            building[0].setVisible(false);
            building[1].setVisible(false);
            building[2].setVisible(false);
        }
    }

    /**
     * returns the label belonging to the given product
     * @param product product of which one wants back the label
     * @return label of the product
     */
    private Label getLabel(String product) {
        switch(product) {
            case "sugar":
                return sugarOfferLabel;
            case "milk":
                return milkOfferLabel;
            case "cacao":
                return cacaoOfferLabel;
            case "chocolate":
                return chocolateOfferLabel;
            default:
                System.out.println("invalid product for updateOffer");
                return null;
        }
    }

    /**
     * removes all offers if another product was chosen
     */
    @FXML
    public void removeOffers() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cacaoOfferLabel.setText("");
                sugarOfferLabel.setText("");
                milkOfferLabel.setText("");
                chocolateOfferLabel.setText("");
            }
        });
    }

    public void setSabotWarning(String[] request) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setPicture(request[2], Integer.parseInt(request[3]));
                chatArrayList.add("you got sabotaged by " + request[1] + "\nyour " + request[2].toLowerCase() + " is now on level " + request[3]);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(GUIGameController.getInstance().chatArrayList);
                chatMessages.setItems(itemsChat);
            }
        });
    }

    /**
     * creates an object of the dayCounter class
     */
    private void startDayCounter() {
        long dayLength = (GUILobbyController.getInstance().monthlength * 100L / 3);
        ScheduledExecutorService dayScheduler = Executors.newScheduledThreadPool(1);
        dayCounter = new DayCounter(dayScheduler);
        dayScheduler.scheduleAtFixedRate(dayCounter, 0, dayLength, TimeUnit.MILLISECONDS);
    }

    /**
     * subclass counting which day of the month it is and updating the calendarlabel
     */
    public class DayCounter implements Runnable {
        public ScheduledExecutorService scheduler;
        DayCounter(ScheduledExecutorService scheduler){
            this.scheduler = scheduler;
        }
        @Override
        public void run() {
            if (dayOfTheMonth < 30) {
                dayOfTheMonth++;
                dayLabel.setText(String.valueOf(dayOfTheMonth));
            }
        }

    }

    /**
     * sets the right calendarpicture depending on the month sent by the server
     * @param month for the month
     */
    public void setMonthPicture(String month) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                monthLabel.setText(month);
                dayLabel.setText(String.valueOf(dayOfTheMonth));
            }
        });
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUIGameController instance;

    public GUIGameController() {
        instance = this;
    }

    public static GUIGameController getInstance() {
        return instance;
    }

}
