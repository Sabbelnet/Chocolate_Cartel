package client;

import javafx.animation.PauseTransition;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * handles GUILobby.fxml
 * proved username is shown
 * shows list of lobby and group members
 * chat function: lobby and whisper
 * player can open new group, invite other player or accept invite
 * game can be startet with optional parameters
 */
public class GUILobbyController implements Initializable {

    String message, whispered, chatLine;
    ArrayList<String> chatArrayList = new ArrayList<>();
    TreeItem<String> treeRoot, treeGroups, treeMembers;
    static boolean inGroup = false;
    @FXML TextField outgoingMessage, newGameName;
    @FXML Text provedUsername, joiningGame;
    @FXML Button logout, sendMessage, enterNewGroup, joinGroup, startGame, addPlayer, leaveGroup, help;
    @FXML ListView<String> lobbyPlayerList, chatMessages, groupMemberList, groupInvitesList, topHighscoresListView;
    @FXML TreeView<String> groupsMembersTree;
    @FXML ComboBox<String> playerListOption;
    @FXML Slider highscoreSlider, monthlengthSlider, gamelengthSlider;
    @FXML VBox groupElementsVB = new VBox();
    @FXML VBox invitationVB = new VBox();
    @FXML VBox newGroupVB = new VBox();
    @FXML TextField monthLengthLabel, highscoreLabel, durationLabel;
    Updater updater;
    int highscore = 0;
    int monthlength = 0;
    int gamelength = 0;
    boolean gameStartByMe = false;
    public static boolean gameWasPlayed = false;
    boolean highscoreSet;
    boolean gamelengthSet;
    boolean monthlengtSet;
    ArrayList<String> groupListArrayList = new ArrayList<>(Arrays.asList());

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUILobbyController instance;

    /**
     * decides the instance
     */
    public GUILobbyController() {
        instance = this;
    }

    /**
     * @return the instance
     */
    public static GUILobbyController getInstance() {
        return instance;
    }

    /**
     * Initializes the Controller
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.connectionHandler.lobbyIsOpen = true;
        setLobbyPlayerlist();
        provedUsername.setText(Main.username);
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        updater = new Updater(this, scheduler);
        scheduler.scheduleAtFixedRate(updater, 100, 1000, TimeUnit.MILLISECONDS);
        try {
            Main.commandHandler.handleCommand("grouplist");
            Updater.getGrouplist = true;
        } catch(Exception e) {
            System.out.println("couldn't get Grouplist");
        }
        try {
            getHighscores();
        } catch(Exception e) {
            System.out.println("couldn't get Highscores");
        }
        newGroupVB.setVisible(true);
        invitationVB.setVisible(true);
        if (gameWasPlayed) {
            groupElementsVB.setVisible(true);
            addPlayer.setVisible(true);
            getYourGroupPlayerlist();
        } else {
            groupElementsVB.setVisible(false);
            addPlayer.setVisible(false);
        }

    }

    /**
     * gets list of top three scores
     */
    void getHighscores() {
        Main.commandHandler.handleCommand("topScores");
    }

    /**
     * sets the Highscores
     */
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

    /**
     * gets list of total groups with its members
     */
    @FXML
    public void getGrouplist() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.commandHandler.handleCommand("grouplist");
                Updater.getGrouplist = true;
            }
        });
    }

    /**
     * gets list of total player online
     * puts it in whisper chat option
     */
    void getPlayerlistAll() {
        Main.commandHandler.handleCommand("playerlistAll");
        Updater.getPlayerlist = true;
    }

    /**
     * sets the names of the players in the right position/array
     */
    public void setPlayerlistAll() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Updater.getPlayerlist = false;
                ArrayList<String> totalPlayerlist = new ArrayList<>(Arrays.asList(Main.connectionHandler.playerlistAll));
                totalPlayerlist.remove(Main.username);
                totalPlayerlist.remove("null");
                totalPlayerlist.add("players in lobby");
                if (inGroup) {
                    totalPlayerlist.add("players in my group");
                }
                ObservableList<String> itemsTotalPlayerlist = FXCollections.observableArrayList(totalPlayerlist);
                try {
                    playerListOption.setItems(itemsTotalPlayerlist);
                } catch (Exception e) {
                    System.out.println("couldn't update PlayerlistAll");
                }
            }
        });
    }

    /**
     * gets list of players in lobby that arent in a group
     * puts it in list where players can be added
     */
    public void setLobbyPlayerlist() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyPlayerList.setItems(null);
                ArrayList<String> lobbyPlayerlist = new ArrayList<>(Arrays.asList(Main.connectionHandler.lobbyPlayerlist));
                lobbyPlayerlist.remove("null");
                lobbyPlayerlist.remove(Main.username);
                ObservableList<String> itemsLobbyPlayerlist = FXCollections.observableArrayList(lobbyPlayerlist);
                try {
                    lobbyPlayerList.setItems(itemsLobbyPlayerlist);
                } catch(Exception e) {
                    System.out.println("couldn't update lobbylist");
                }
            }
        });
    }

    /**
     * gets list of players in your group
     * shows list in gui list view
     */
    void getYourGroupPlayerlist() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!Main.connectionHandler.groupPlayerlist[0].equals("noGroup")) {
                    ArrayList<String> groupPlayerlist = new ArrayList<>(Arrays.asList(Main.connectionHandler.groupPlayerlist));
                    groupPlayerlist.remove("null");
                    ObservableList<String> itemsGroupPlayerlist = FXCollections.observableArrayList(groupPlayerlist);
                    try {
                        groupMemberList.setItems(itemsGroupPlayerlist);
                    } catch (Exception e) {
                        System.out.println("couldn't update ownGrouplist");
                    }
                }
            }
        });
    }

    /**
     * fills the grouplist
     */
    @FXML
    public void setGrouplist() {
        Updater.getGrouplist = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                treeRoot = new TreeItem<>("groups, state");
                if (Main.connectionHandler.groupList.length > 1) {
                    for (int i = 1; i < Main.connectionHandler.groupList.length; i++) {
                        String[] groupsNMembers = Main.connectionHandler.groupList[i].split(",");
                        treeGroups = new TreeItem(groupsNMembers[0] + ", " + groupsNMembers[1]);
                        for (int k = 2; k < groupsNMembers.length; k++) {
                            treeMembers = new TreeItem<>(groupsNMembers[k]);
                            treeGroups.getChildren().add(treeMembers);
                        }
                        treeRoot.getChildren().add(treeGroups);
                    }
                }
                groupsMembersTree.setRoot(treeRoot);
            }
        });
    }


    /**
     * player can open a new group with chosen name
     * @param actionEvent event
     */
    public void handleNewGroupName(ActionEvent actionEvent) {
        if (!newGameName.getText().equals("")) {
            Main.commandHandler.handleCommand("new group " + newGameName.getText());
            inGroup = true;
            groupElementsVB.setVisible(true);
            addPlayer.setVisible(true);
            newGroupVB.setVisible(false);
            invitationVB.setVisible(false);
            Main.commandHandler.handleCommand("grouplist");
            Updater.getGrouplist = true;
        } else {
            newGameName.setText("");
            newGameName.setPromptText("please enter a name");
        }
    }

    /**
     * chosen player will be added to the own group
     */
    @FXML
    void handleAddPlayer() {
        String addPlayer = lobbyPlayerList.getSelectionModel().getSelectedItem();
        Main.commandHandler.handleCommand("add " + addPlayer);
    }

    /**
     * player was added by other player
     * @param invitation
     */
    void handleGroupInvitation(String[] invitation) throws URISyntaxException {
        groupListArrayList.add(invitation[2]);
        ObservableList<String> itemsInvites = FXCollections.observableArrayList(groupListArrayList);
        groupInvitesList.setItems(itemsInvites);
        joinGroup.setVisible(true);
        AudioClip invite =  new AudioClip(GUILobbyController.class.getResource("/sounds/invitationSound.mp3").toURI().toString());
        invite.play();
    }

    /**
     * adds player to chosen game
     * leads user to game
     */
    @FXML
    void handleJoinGroup() {
        if (groupInvitesList.getSelectionModel().getSelectedItem() != null && !groupInvitesList.getSelectionModel().getSelectedItem().equals("")) {
            Main.commandHandler.handleCommand("join " + groupInvitesList.getSelectionModel().getSelectedItem());
            groupListArrayList.remove(groupInvitesList.getSelectionModel().getSelectedItem());
            ObservableList<String> itemsInvites = FXCollections.observableArrayList(groupListArrayList);
            groupInvitesList.setItems(itemsInvites);
            inGroup = true;
            groupElementsVB.setVisible(true);
            addPlayer.setVisible(true);
            newGroupVB.setVisible(false);
            invitationVB.setVisible(false);
            Main.commandHandler.handleCommand("grouplist");
            Updater.getGrouplist = true;
        }
    }

    /**
     * leads palyer to game scene, takes optional parameters for game length simulation
     * @param actionEvent button
     * @throws IOException fxml
     */
    @FXML
    void handleStartGame(ActionEvent actionEvent) throws IOException {
        try {
            if (checkIfValidValue(highscoreLabel.getText(), highscoreLabel)) {
                if (Integer.parseInt(highscoreLabel.getText()) >= 20000) {
                    highscore = Integer.parseInt(highscoreLabel.getText());
                    highscoreSet = true;
                } else {
                    highscoreLabel.setFont(Font.font(9));
                    highscoreLabel.setText("");
                    highscoreLabel.setPromptText("enter higher number!");
                }
            }
            if (checkIfValidValue(durationLabel.getText(), durationLabel)) {
                gamelength = Integer.parseInt(durationLabel.getText());
                gamelengthSet = true;
            }
            if (checkIfValidValue(monthLengthLabel.getText(), monthLengthLabel)) {
                monthlength = Integer.parseInt(monthLengthLabel.getText());
                monthlengtSet = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (highscoreSet && gamelengthSet && monthlengtSet) {
            if (joiningGame.equals("")) {
                joiningGame.setText("you first have to join a game");
            } else {
                Main.commandHandler.handleCommand("start game " + highscore + " " + gamelength + " " + monthlength);
            }
            Parent game = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIGame.fxml"));
            Scene lobbyScene = new Scene(game);
            GUIStart.mainStage.setOnCloseRequest(e -> {
                try {
                    GUIStart.getInstance().handleLogout();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            GUIStart.mainStage.setTitle("Chocolate Cartel Game");
            GUIStart.mainStage.getIcons().add(new Image("logo_freigestellt.png"));
            /**
             * before game starts a loadingscreen shows up for 4 seconds
             */
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished( event -> GUIStart.mainStage.setScene(lobbyScene));
            delay.play();
        }
        gameStartByMe = true;
    }

    /**
     * method called if other player calls game, leads player to game scene
     * @throws IOException fxml
     */
    public void startGame() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (gameStartByMe == false) {
                    Parent game2 =null;
                    try {
                        game2 = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIGame.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene lobbyScene = new Scene(game2);
                    GUIStart.mainStage.setTitle("Chocolate Cartel Game");
                    GUIStart.mainStage.getIcons().add(new Image("logo_freigestellt.png"));
                    /**
                     * before game starts a loadingscreen shows up for 5 seconds
                     */
                    PauseTransition delay = new PauseTransition(Duration.seconds(5));
                    delay.setOnFinished( event -> GUIStart.mainStage.setScene(lobbyScene));
                    delay.play();
                }
            }
        });
    }

    /**
     * is called by client/ConnectionHandler if there is an incoming message and printed out in chat
     * @param chat string array with message and sender info
     */
    void getIncomingChatLobby(String[] chat) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatLine = chat[1] + " message by " + chat[3] + ": " + chat[2];
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                if(chat[1].equals("WSP")){
                    AudioClip invite = null;
                    try {
                        invite = new AudioClip(GUILobbyController.class.getResource("/sounds/message.mp3").toURI().toString());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    invite.play();
                }
            }
        });
    }

    /**
     * player has option to choose address (lobby or whisper) to send a message
     */
    @FXML
    void handleOutgoingMessage() {
        whispered = playerListOption.getSelectionModel().getSelectedItem();
        message = outgoingMessage.getText();
        if (whispered != null) {
            if (message.equals("")) {
                outgoingMessage.setPromptText("please type in a message");
                outgoingMessage.setFocusTraversable(false);
            } else if (whispered.equals("players in lobby")) {
                Main.commandHandler.handleCommand("lobby message " + message);
                chatLine = "your message to LOBBY: " + message;
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                outgoingMessage.clear();
            } else if (whispered.equals("players in my group")) {
                Main.commandHandler.handleCommand("group message " + message);
                chatLine = "your message to GROUP: " + message;
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                outgoingMessage.clear();
            } else {
                Main.commandHandler.handleCommand("whisper " + message + " to " + whispered);
                outgoingMessage.setText("");
                chatLine = "your message to " + whispered + ": " + message;
                chatArrayList.add(chatLine);
                ObservableList<String> itemsChat = FXCollections.observableArrayList(chatArrayList);
                chatMessages.setItems(itemsChat);
                outgoingMessage.clear();
            }
        } else {
            outgoingMessage.setText("");
            outgoingMessage.setPromptText("you have to choose an address");
            outgoingMessage.setFocusTraversable(false);
        }
    }

    /**
     * logs player out, closes window
     */
    @FXML
    void handleLogout() throws IOException {
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
     * handles the help button
     * @param event mouseclick
     * @throws IOException if there is no
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
     * checks if the values entered in the textfield to enter the monthlength, gamelength and highscore of the game
     * are numeric, positive and not 0
     * @param value entered value
     * @param textField textfield where warning would be printed out
     * @return
     */
    private boolean checkIfValidValue(String value, TextField textField) {
        boolean isValid = true;
        if (value.equals("")) {
            isValid = false;
            textField.setText("");
            textField.setPromptText("enter number!");
        } else if (!Main.commandHandler.testIfNumber(value, false)) {
            isValid = false;
            textField.setText("");
            textField.setPromptText("enter number!");
        } else if (value.charAt(0) == '-') {
            isValid = false;
            textField.setFont(Font.font(9));
            textField.setText("");
            textField.setPromptText("enter positive number!");
        } else if (value.equals("0")) {
            isValid = false;
            textField.setText("");
            textField.setPromptText("cannot be 0!");
        }
        return isValid;
    }

    /**
     * player leaves group with this button
     * @param actionEvent button
     */
    public void leaveGroup(ActionEvent actionEvent) {
        Main.commandHandler.handleCommand("leave");
        newGroupVB.setVisible(true);
        groupInvitesList.setVisible(true);
        groupElementsVB.setVisible(false);
        Main.commandHandler.handleCommand("grouplist");
        Updater.getGrouplist = true;
        invitationVB.setVisible(true);
        inGroup = false;
    }

    /**
     * optional durations to set win conditions
     * @param mouseEvent slider
     */
    public void setMonthlengthLabel(javafx.scene.input.MouseEvent mouseEvent) {
        monthLengthLabel.setText(String.valueOf((int) monthlengthSlider.getValue()));
    }

    /**
     * sets the duration for a game with a slider
     * @param mouseEvent for the slider
     */
    public void setDurationLabel(javafx.scene.input.MouseEvent mouseEvent) {
        durationLabel.setText(String.valueOf((int) gamelengthSlider.getValue()));
    }

    /**
     * sets the max. highscore for a gamestart on a label
     * @param mouseEvent from the slider
     */
    public void setHighscoreLabel(javafx.scene.input.MouseEvent mouseEvent) {
        highscoreLabel.setText(String.valueOf((int) highscoreSlider.getValue()));
    }


}
