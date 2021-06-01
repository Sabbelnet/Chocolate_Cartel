package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIChartsController implements Initializable {

    @FXML @SuppressWarnings("unchecked") private LineChart<String, Double> sugarSellChart, sugarBuyChart, cacaoSellChart,
            cacaoBuyChart, milkSellChart, milkBuyChart, chocolateSellChart, chocolateBuyChart;
    @FXML private Button chocolateChartButton, cacaoChartButton, milkChartButton, sugarChartButton;
    int productIndex;

    /**
     * sets the ecoData of every product in every category
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (sugarSellChart != null) {
            productIndex = 0;
        } else if (cacaoSellChart != null) {
            productIndex = 1;
        } else if (milkSellChart != null) {
            productIndex = 2;
        } else if (chocolateSellChart != null) {
            productIndex = 3;
        }
        try {
            for (int i = 0; i < 6; i++) {
                setData(i);
            }
        } catch(Exception e) {
            System.out.println("couldn't set ecoData");
        }
    }


    /**
     * sets the ecodata for the given product and given category
     * @param categoryIndex
     */
    @SuppressWarnings("unchecked")
    private void setData(int categoryIndex) {
        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
        int startMonth = 12;
        for (int i = 0; i < 13; i++) {
            if (!Main.connectionHandler.monthArray[i].equals("-")) {
                startMonth = i;
            }
        }
        for (int i = startMonth; i >= 0; i--) {
            if (Main.connectionHandler.ecoData[0][i][categoryIndex] == null) {
                Main.connectionHandler.ecoData[0][i][categoryIndex] = "0";
            }
            series.getData().add(new XYChart.Data<String, Double>(Main.connectionHandler.monthArray[i], Double.parseDouble(Main.connectionHandler.ecoData[productIndex][i][categoryIndex])));

        }
        switch(categoryIndex) {
            case 0:
                series.setName("max price");
                break;
            case 1:
                series.setName("min price");
                break;
            case 2:
                series.setName("average price");
                break;
            case 3:
                series.setName("max price");
                break;
            case 4:
                series.setName("min price");
                break;
            case 5:
                series.setName("average price");
                break;
        }

        switch(productIndex) {
            case 0:
                try {
                    if (categoryIndex <= 2) {
                        sugarSellChart.getData().addAll(series);
                    } else {
                        sugarBuyChart.getData().addAll(series);
                    }
                } catch (Exception e) {
                    System.out.println("couldn't add data to sugarSellChart");
                }
                break;
            case 1:
                try {
                    if (categoryIndex <= 2) {
                        cacaoSellChart.getData().addAll(series);
                    } else {
                        cacaoBuyChart.getData().addAll(series);
                    }
                } catch (Exception e) {
                    System.out.println("couldn't add data to cacaoSellChart");
                }
                break;
            case 2:
                try {
                    if (categoryIndex <= 2) {
                        milkSellChart.getData().addAll(series);
                    } else {
                        milkBuyChart.getData().addAll(series);
                    }
                } catch (Exception e) {
                    System.out.println("couldn't add data to milkSellChart");
                }
                break;
            case 3:
                try {
                    if (categoryIndex <= 2) {
                        chocolateSellChart.getData().addAll(series);
                    } else {
                        chocolateBuyChart.getData().addAll(series);
                    }
                } catch (Exception e) {
                    System.out.println("couldn't add data to chocolateSellChart");
                }
                break;
        }
    }
}
