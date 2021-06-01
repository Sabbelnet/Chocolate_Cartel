package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import server.BuildingStats;

public class GUIBuildingController {
    @FXML Button buildButton, tearDownButton;
    @FXML Label buildingInfo, choosingInfo, buildingLabel;
    @FXML ImageView sugarView1, sugarView2, sugarView3, cacaoView1, cacaoView2, cacaoView3, milkView1, milkView2, milkView3,
            chocolateView1, chocolateView2, chocolateView3, labView1, labView2, labView3,  warehouseView1, warehouseView2, warehouseView3;
    String building;
    String sendBuilding;
    int currentLevel;
    int currentProduction;
    int nextProduction;
    int price;
    int gainForDemolition;

    /**
     * sends buildingcommand to server for buildButton action
     */
    @FXML
    void build() {
        Main.commandHandler.handleCommand("build " + sendBuilding);
        buildingLabel.setVisible(false);
        setNoPicture();
        buildingInfo.setVisible(false);
    }

    /**
     * tears down tearing down command to server for tearDownButton action
     */
    @FXML
    void tearDown() {
        Main.commandHandler.handleCommand("tear " + sendBuilding);
        buildingLabel.setVisible(false);
        setNoPicture();
        buildingInfo.setVisible(false);
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    public void buildCacao() {
        building = "CACAOFARM";
        sendBuilding = "cacao";
        currentLevel = Main.connectionHandler.CACAOFARM;
        setProductionBuildingInfos("CACAOFARM");
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    public void buildChocolate() {
        building = "FACTORY";
        sendBuilding = "chocolate";
        currentLevel = Main.connectionHandler.FACTORY;
        setProductionBuildingInfos("FACTORY");
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    public void buildLab() {
        building = "LAB";
        sendBuilding = "laboratory";
        currentLevel = Main.connectionHandler.LAB;
        setProductionBuildingInfos("LAB");
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    public void buildMilk() {
        building = "STABLE";
        sendBuilding = "milk";
        currentLevel = Main.connectionHandler.STABLE;
        setProductionBuildingInfos("STABLE");
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    public void buildSugar() {
        building = "SUGARFARM";
        sendBuilding = "sugar";
        currentLevel = Main.connectionHandler.SUGARFARM;
        setProductionBuildingInfos("SUGARFARM");
    }

    /**
     * handles buildingdecision by setting building to proper buildingname, currentlevel and calles methode setProductionBuildingInfos
     */
    @FXML
    void buildWarehouse() {
        building = "WAREHOUSE";
        sendBuilding = "warehouse";
        currentLevel = Main.connectionHandler.WAREHOUSE;
        setProductionBuildingInfos("WAREHOUSE");
    }

    /**
     * sets chosingInfo visible
     * sets priceInfo, productionInfo and demolitionInfo to build buildingInfo and set itn visible
     * calles getBuildingStatsData
     * @param building is the building
     */
    private void setProductionBuildingInfos(String building) {
        choosingInfo.setVisible(false);
        getBuildingStatsData(building);
        String priceInfo;
        if (price == 0) {
            priceInfo = "this building already has the highest level\n";
        } else {
            priceInfo = "price for a levelup:   " + price + "\n\n";
        }
        String productionInfo;
        if (building.equals("WAREHOUSE") || building.equals("LAB")) {
            productionInfo = "";
        } else {
            productionInfo = "current production per month:   " + currentProduction + "\n\nproduction after a levelup:   " + nextProduction + "\n\n";
        }
        String demolitionInfo;
        if (gainForDemolition == 0) {
            demolitionInfo = "";
        } else {
            demolitionInfo = "gain if you tear down this building:   " + gainForDemolition;
        }
        buildingInfo.setText(sendBuilding + "\n\n" + priceInfo + productionInfo + demolitionInfo);
        buildingInfo.setVisible(true);

        if (currentLevel != 0) {
            tearDownButton.setVisible(true);
        }
        if (currentLevel != 3) {
            buildButton.setVisible(true);
        }
    }

    /**
     * sets currenProduction, next Production
     * sets price, gainForDemolition, buildingLabel and picture in a switch case
     * @param building chosen building
     */
    private void getBuildingStatsData(String building) {
        currentProduction = Main.commandHandler.getProduction(building, currentLevel);
        nextProduction = Main.commandHandler.getProduction(building, currentLevel + 1);
        String buildingStats = building + currentLevel;
        switch (buildingStats) {
            case "SUGARFARM0":
                price = BuildingStats.SUGARFARM1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(sugarView1);
                break;
            case "SUGARFARM1":
                price = BuildingStats.SUGARFARM2.buildingCost;
                gainForDemolition = -BuildingStats.SUGARFARM1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(sugarView2);
                break;
            case "SUGARFARM2":
                price = BuildingStats.SUGARFARM3.buildingCost;
                gainForDemolition = -BuildingStats.SUGARFARM2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(sugarView3);
                break;
            case "SUGARFARM3":
                price = 0;
                gainForDemolition = -BuildingStats.SUGARFARM3.getDemolitionGain();
                break;
            case "CACAOFARM0":
                price = BuildingStats.CACAOFARM1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(cacaoView1);
                break;
            case "CACAOFARM1":
                price = BuildingStats.CACAOFARM2.buildingCost;
                gainForDemolition = -BuildingStats.CACAOFARM1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(cacaoView2);
                break;
            case "CACAOFARM2":
                price = BuildingStats.CACAOFARM3.buildingCost;
                gainForDemolition = -BuildingStats.CACAOFARM2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(cacaoView3);
                break;
            case "CACAOFARM3":
                price = 0;
                gainForDemolition = -BuildingStats.CACAOFARM3.getDemolitionGain();
                break;
            case "STABLE0":
                price = BuildingStats.STABLE1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(milkView1);
                break;
            case "STABLE1":
                price = BuildingStats.STABLE2.buildingCost;
                gainForDemolition = -BuildingStats.STABLE1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(milkView2);
                break;
            case "STABLE2":
                price = BuildingStats.STABLE3.buildingCost;
                gainForDemolition = -BuildingStats.STABLE2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(milkView3);
                break;
            case "STABLE3":
                price = 0;
                gainForDemolition = -BuildingStats.STABLE3.getDemolitionGain();
                break;
            case "FACTORY0":
                price = BuildingStats.FACTORY1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(chocolateView1);
                break;
            case "FACTORY1":
                price = BuildingStats.FACTORY2.buildingCost;
                gainForDemolition = -BuildingStats.FACTORY1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(chocolateView2);
                break;
            case "FACTORY2":
                price = BuildingStats.FACTORY3.buildingCost;
                gainForDemolition = -BuildingStats.FACTORY2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(chocolateView3);
                break;
            case "FACTORY3":
                price = 0;
                gainForDemolition = -BuildingStats.FACTORY3.getDemolitionGain();
                break;
            case "LAB0":
                price = BuildingStats.LAB1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(labView1);
                break;
            case "LAB1":
                price = BuildingStats.LAB2.buildingCost;
                gainForDemolition = -BuildingStats.LAB1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(labView2);
                break;
            case "LAB2":
                price = BuildingStats.LAB3.buildingCost;
                gainForDemolition = -BuildingStats.LAB2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(labView3);
                break;
            case "LAB3":
                price = 0;
                gainForDemolition = -BuildingStats.LAB3.getDemolitionGain();
                break;
            case "WAREHOUSE0":
                price = BuildingStats.WAREHOUSE1.buildingCost;
                gainForDemolition = 0;
                buildingLabel.setVisible(true);
                setBuildingPicture(warehouseView1);
                break;
            case "WAREHOUSE1":
                price = BuildingStats.WAREHOUSE2.buildingCost;
                gainForDemolition = -BuildingStats.WAREHOUSE1.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(warehouseView2);
                break;
            case "WAREHOUSE2":
                price = BuildingStats.WAREHOUSE3.buildingCost;
                gainForDemolition = -BuildingStats.WAREHOUSE2.getDemolitionGain();
                buildingLabel.setVisible(true);
                setBuildingPicture(warehouseView3);
                break;
            case "WAREHOUSE3":
                price = 0;
                gainForDemolition = -BuildingStats.WAREHOUSE3.getDemolitionGain();
                break;
        }
    }

    /**
     *places the picture for the builing on the space
     * @param building building
     */
    void setBuildingPicture(ImageView building) {
        setNoPicture();
        building.setVisible(true);
    }

    /**
     * when there are no build buildings
     */
    void setNoPicture() {
        sugarView1.setVisible(false);
        sugarView2.setVisible(false);
        sugarView3.setVisible(false);
        cacaoView1.setVisible(false);
        cacaoView2.setVisible(false);
        cacaoView3.setVisible(false);
        milkView1.setVisible(false);
        milkView2.setVisible(false);
        milkView3.setVisible(false);
        chocolateView1.setVisible(false);
        chocolateView2.setVisible(false);
        chocolateView3.setVisible(false);
        labView1.setVisible(false);
        labView2.setVisible(false);
        labView3.setVisible(false);
        warehouseView1.setVisible(false);
        warehouseView2.setVisible(false);
        warehouseView3.setVisible(false);
    }

    /**
     * is called by connectionhandler in BLDCK to set the buildingInfo according to how the build command ended
     * @param info building info
     */
    public void setBuildingInfo(String info) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              buildingInfo.setText(info);
              buildingInfo.setVisible(true);
            }
        });
    }

    /**
     * warning if there are no buildings and someone wants to build a laboratory
     */
    public void setNoProductionBuildingWarning() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                buildingInfo.setText("you need to have built at least one resource-production-building to build a laboratory");
                buildingInfo.setVisible(true);
            }
        });
    }

    /**
     * makes possible that other classes are able to run methods in the same fxml-controller
     */
    private static GUIBuildingController instance;

    public GUIBuildingController() {
        instance = this;
    }

    public static GUIBuildingController getInstance() {
        return instance;
    }
}
