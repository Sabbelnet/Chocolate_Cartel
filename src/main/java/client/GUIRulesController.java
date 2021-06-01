package client;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.util.ResourceBundle;


public class GUIRulesController implements Initializable {

    @FXML
    TextFlow text;

    /**
     * This Controller writes our GameGuide into a PopUp 
     * @param location is the location
     * @param resources is the ressources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * as we use textflow, so we can edit part of our text, with bold. underline...
         * we had to split it up to make it more readable and be able to fix it easier
         */
        Text text1 = new Text("Gamerules \n\n");
        text1.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        text1.setUnderline(true);

        Text text2 = new Text("Goal \n\n");
        text2.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        text2.setUnderline(true);

        Text text3 = new Text("The target of the Game is to make as much Capital as you can. There are two factors " +
                "which will decide your playstyle.\n\n");
        text3.setFont(Font.font("Helvetica", 12));

        Text text35 = new Text("Timed Mode : ");
        text35.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text4 = new Text("Here your main limit is decided by time, when the game reaches a defined " +
                "amount of months, the game will end. \n");
        text4.setFont(Font.font("Helvetica", 12));

        Text text5 = new Text("One way to gain an advantage in the last months is, that you dont use your saboteur " +
                "anymore (costs money) and tear down all your buildings shortly before the game ends.\n");
        text5.setFont(Font.font("Helvetica", 12));

        Text text6 = new Text("Tearing down your buildings gives you some amount of money back which you spend to " +
                "build it. \n");
        text6.setFont(Font.font("Helvetica", 12));

        Text text7 = new Text("You can also sell all of the rest of your extra ressources to generate as much " +
                "capital before the game ends.\n\n");
        text7.setFont(Font.font("Helvetica", 12));

        Text text75 = new Text("Capital Mode : ");
        text75.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text8 = new Text("In this mode you win by generating a defined amount of capital. \n" +
                "Here you will try to build up your factory as fast as you can and sabotage your enemys/ the best " +
                "players to hinder them getting ahead of you.\n");
        text8.setFont(Font.font("Helvetica", 12));

        Text text9 = new Text("You can also sell all of the rest of your extra ressources to generate as much " +
                "capital before the game ends.\n\n");
        text9.setFont(Font.font("Helvetica", 12));


        Text text10 = new Text("Buttons\n\n");
        text10.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
        text10.setUnderline(true);

        Text text105 = new Text("Build : ");
        text105.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text11 = new Text("Use it to upgrade your buildings, to be able to produce sugar, milk, " +
                "cacao and chocolate.\n");
        text11.setFont(Font.font("Helvetica", 12));

        Text text12 = new Text("Your buildings will start to produce ressources, if you have a choclate factory, " +
                "your ressources will be used to produce chocolate, you will need sugar, cacao and milk to produce " +
                "it. \n");
        text12.setFont(Font.font("Helvetica", 12));

        Text text13 = new Text("To be able to upgrade your buildings past level 1 you need to upgrade your laboratory " +
                "first. On laboratory level 2, you unlock the ability to buy a spy/saboteur.  \n");
        text13.setFont(Font.font("Helvetica", 12));

        Text text14 = new Text("The ressources you produce will be send to your warehouse, watch out that you dont " +
                "overfill it!\n\n");
        text14.setFont(Font.font("Helvetica", 12));

        Text text145 = new Text("SELL/BUY : ");
        text145.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text15 = new Text("You can sell or buy ressources with these buttons, the more a ressource " +
                "gets sold the lower the price will get and if noone sells a ressource there will be none to buy.\n\n");
        text15.setFont(Font.font("Helvetica", 12));

        Text text155 = new Text("Highscore : ");
        text155.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text16 = new Text("With the highscore Button you can view the Highscore of your Game.\n\n");
        text16.setFont(Font.font("Helvetica", 12));

        Text text165 = new Text("Booster : ");
        text165.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text17 = new Text("As soon as you unlock the spy/saboteur, you can buy them here, they cost " +
                "a lot and their actions too, so dont go overboard!\n\n");
        text17.setFont(Font.font("Helvetica", 12));

        Text text175 = new Text("Logout : ");
        text175.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text18 = new Text("Logout : If you want to leave the game prematurely. If you stay offline to long, " +
                "you wont be able to reconnect !\n\n");
        text18.setFont(Font.font("Helvetica", 12));

        Text text185 = new Text("Data : ");
        text185.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text19 = new Text("Data : Shows your monthly update, on production and economy.\n");
        text19.setFont(Font.font("Helvetica", 12));
        /**
        * Here we are adding all the textpart together
        */
        text.getChildren().add(text1);
        text.getChildren().add(text2);
        text.getChildren().add(text3);
        text.getChildren().add(text35);
        text.getChildren().add(text4);
        text.getChildren().add(text5);
        text.getChildren().add(text6);
        text.getChildren().add(text7);
        text.getChildren().add(text75);
        text.getChildren().add(text8);
        text.getChildren().add(text9);
        text.getChildren().add(text10);
        text.getChildren().add(text105);
        text.getChildren().add(text11);
        text.getChildren().add(text12);
        text.getChildren().add(text13);
        text.getChildren().add(text14);
        text.getChildren().add(text145);
        text.getChildren().add(text15);
        text.getChildren().add(text155);
        text.getChildren().add(text16);
        text.getChildren().add(text165);
        text.getChildren().add(text17);
        text.getChildren().add(text175);
        text.getChildren().add(text18);
        text.getChildren().add(text185);
        text.getChildren().add(text19);
    }
}
