package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;

import javafx.scene.control.Button;


public class GUISellController {
    String product = "";
    String price = "";
    String amount = "";

    @FXML private Button sellButton, sugarChartButton, cacaoChartButton, milkChartButton, chocolateChartButton, setMaxButton;

    @FXML private Slider amountSlider, priceSlider;

    @FXML private Label noProductWarning, noAmountWarning, noPriceWarning, amountInfo, priceInfo, chosenAmount, chosenPrice, chosenProduct, amountLabel, priceLabel, offerLabel, nothingToSellLabel;

    @FXML private ImageView sugarSymbol, milkSymbol, cacaoSymbol, chocolateSymbol;
    double priceMax;
    double amountMax;

    /**
     * handles chosen Product
     * setting infos and global product variable
     * @param event
     */
    @FXML
    void handleSugar(ActionEvent event) {
        product = "sugar";
        chosenProduct.setText(product);
        handleProductSet(0);
        setInvisible();
        sugarSymbol.setVisible(true);
    }

    /**
     * handles chosen Product
     * setting infos and global product variable
     * @param event
     */
    @FXML
    void handleCacao(ActionEvent event) {
        product = "cacao";
        chosenProduct.setText(product);
        handleProductSet(1);
        setInvisible();
        cacaoSymbol.setVisible(true);
    }

    /**
     * handles chosen Product
     * setting infos and global product variable
     * @param event
     */
    @FXML
    void handleMilk(ActionEvent event) {
        product = "milk";
        chosenProduct.setText(product);
        handleProductSet(2);
        setInvisible();
        milkSymbol.setVisible(true);
    }

    /**
     * handles chosen Product
     * setting infos and global product variable
     * @param event
     */
    @FXML
    void handleChocolate(ActionEvent event) {
        product = "chocolate";
        chosenProduct.setText(product);
        handleProductSet(3);
        setInvisible();
        chocolateSymbol.setVisible(true);
    }

    /**
     * sets buttons and sliders visible after product is chosen
     * sets pricemin and max
     * @param productIndex (sugar, cacao, milk, chocolate)
     */
    void handleProductSet(int productIndex) {
        if (Main.connectionHandler.stock[productIndex] > 0) {
            setMaxButton.setVisible(true);
            nothingToSellLabel.setVisible(false);
            amount = "";
            price = "";
            chosenAmount.setText(amount);
            chosenPrice.setText(price);
            sellButton.setVisible(true);
            priceSlider.setMin(Main.connectionHandler.newSellPrice[productIndex]);
            priceSlider.setMax(Main.connectionHandler.newSellPrice[productIndex] * 1.25);
            priceMax = Main.connectionHandler.newSellPrice[productIndex] * 1.25;
            priceSlider.setVisible(true);
            amountSlider.setMin(1);
            amountSlider.setMax(Main.connectionHandler.stock[productIndex]);
            amountMax = Main.connectionHandler.stock[productIndex];
            amountSlider.setVisible(true);
            amountInfo.setVisible(true);
            priceInfo.setVisible(true);
        } else {
            setMaxButton.setVisible(false);
            chosenAmount.setText("");
            chosenPrice.setText("");
            sellButton.setVisible(false);
            priceSlider.setVisible(false);
            amountSlider.setVisible(false);
            amountInfo.setVisible(false);
            priceInfo.setVisible(false);
            nothingToSellLabel.setVisible(true);
        }
    }

    /**
     * removes warnings if productmenu is touched
     * @param event buttonevent
     */
    @FXML
    void removeWarning(MouseEvent event) {
       noProductWarning.setVisible(false);
       noAmountWarning.setVisible(false);
       noPriceWarning.setVisible(false);
       offerLabel.setVisible(false);
    }

    /**
     * sends sellcommand to server and shows info about offer
     * @param event
     */
    @FXML
    void handleSellButton(ActionEvent event) {
        if (product.equals("")) {
            noProductWarning.setVisible(true);
        } else if (amount.equals("")) {
            noAmountWarning.setVisible(true);
        } else if (price.equals("")) {
            priceInfo.setVisible(false);
        } else {
            Main.commandHandler.handleCommand("sell " + amount + " " + product + " " + price);
            offerLabel.setText("you made an offer for:\n" + amount + " " +  product + "\n price: " + price);
            offerLabel.setVisible(true);
            GUIGameController.getInstance().updateOffer(product, amount, price);
        }
    }

    /**
     * sets global amount after using the amountslider
     */
    @FXML
    private void setAmount() {
        noAmountWarning.setVisible(false);
        amount = String.valueOf((int) amountSlider.getValue());
        amountLabel.setVisible(true);
        chosenAmount.setText(amount);
    }

    /**
     * sets global price after using the priceslider
     */
    @FXML
    private void setPrice() {
        noPriceWarning.setVisible(false);
        price = String.valueOf((int) priceSlider.getValue());
        priceLabel.setVisible(true);
        chosenPrice.setText(price);
    }

    /**
     * sets all productsymbols invisible before chosen productsymbol is set visible
     */
    private void setInvisible(){
        sugarSymbol.setVisible(false);
        cacaoSymbol.setVisible(false);
        milkSymbol.setVisible(false);
        chocolateSymbol.setVisible(false);
    }

    public void setMax() {
        noPriceWarning.setVisible(false);
        price = String.valueOf((int) priceMax);
        priceLabel.setVisible(true);
        chosenPrice.setText(price);
        noAmountWarning.setVisible(false);
        amount = String.valueOf((int) amountMax);
        amountLabel.setVisible(true);
        chosenAmount.setText(amount);
        amountSlider.setValue(amountMax);
        priceSlider.setValue(priceMax);
    }

    /**
     * handles pressing the sugarChart button
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSugarChart(ActionEvent event) throws IOException {
        Stage stage= new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUISugarChart.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(sugarChartButton.getScene().getWindow());
            stage.showAndWait();
    }

    /**
     * handles pressing the sugarChart button
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleCacaoChart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUICacaoChart.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(cacaoChartButton.getScene().getWindow());
            stage.showAndWait();
    }

    /**
     * handles pressing the sugarChart button
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleMilkChart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIMilkChart.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(milkChartButton.getScene().getWindow());
            stage.showAndWait();
    }

    /**
     * handles pressing the sugarChart button
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleChocolateChart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GUIChocolateChart.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(chocolateChartButton.getScene().getWindow());
            stage.showAndWait();
    }
}
