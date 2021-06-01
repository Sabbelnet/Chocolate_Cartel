package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIMonthlyUpdateController implements Initializable {

    @FXML private ImageView sugarSymbol, cacaoSymbol, milkSymbol, chocolateSymbol;

    @FXML private Text sugarSoldField, sugarPriceField, sugarProfitField, cacaoSoldField, cacaoPriceField, cacaoProfitField,
            milkSoldField, milkPriceField, milkProfitField, chocolateSoldField, chocolatePriceField, chocolateProfitField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    /**
     * writes all the data to the fields
     */
    @FXML
    void setData() {
        sugarSoldField.setText(String.valueOf(Main.connectionHandler.sold[0]));
        cacaoSoldField.setText(String.valueOf(Main.connectionHandler.sold[1]));
        milkSoldField.setText(String.valueOf(Main.connectionHandler.sold[2]));
        chocolateSoldField.setText(String.valueOf(Main.connectionHandler.sold[3]));

        sugarPriceField.setText(String.valueOf(Main.connectionHandler.price[0]));
        cacaoPriceField.setText(String.valueOf(Main.connectionHandler.price[1]));
        milkPriceField.setText(String.valueOf(Main.connectionHandler.price[2]));
        chocolatePriceField.setText(String.valueOf(Main.connectionHandler.price[3]));

        sugarProfitField.setText((String.valueOf(Main.connectionHandler.sold[0] * Main.connectionHandler.price[0])));
        cacaoProfitField.setText((String.valueOf(Main.connectionHandler.sold[1] * Main.connectionHandler.price[1])));
        milkProfitField.setText((String.valueOf(Main.connectionHandler.sold[2] * Main.connectionHandler.price[2])));
        chocolateProfitField.setText((String.valueOf(Main.connectionHandler.sold[3] * Main.connectionHandler.price[3])));
    }
}