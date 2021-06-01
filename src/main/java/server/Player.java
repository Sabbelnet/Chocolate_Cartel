package server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Player {

    public int capital;
    public int income;
    private final String name;

    public int[] resourceAmount;
    public double[] eventMultiplier;
    public int[] resourceAmountEndOfMonth;
    public int[] resourceProduction;
    public int[] resourceStorage;
    public int[] buildingLevel;
    public int[] sellAmount;
    public int[] price;
    public int[] sold;
    public int[] soldUpdate;
    public final int chocoMultiplier = 2;
    public int[] priceUpdate;
    private boolean spy;
    public boolean sabot;
    private static final Logger logger = LogManager.getLogger(GameInstance.class);



    public Player(String name) {
        this.name = name;
        setDefault();
    }

    /**
     * constructr method
     * can be called from outside to reset player
     */
    public void setDefault() {
        capital = 10000;
        resourceAmount = new int[]{0, 0, 0, 0};
        eventMultiplier = new double[]{1, 1, 1, 1};
        resourceAmountEndOfMonth = new int[]{0, 0, 0, 0};
        resourceProduction = new int[]{0, 0, 0, 0};
        resourceStorage = new int[]{0, 0, 0, 0};
        buildingLevel = new int[]{0, 0, 0, 0, 0, 0};
        sellAmount = new int[]{0, 0, 0, 0};
        price = new int[]{0, 0, 0, 0};
        priceUpdate = new int[]{0, 0, 0, 0};
        sold = new int[]{0, 0, 0, 0};
        soldUpdate = new int[]{0, 0, 0, 0};
        spy = false;
        sabot = false;
    }

    /**
     * get players name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if player has enough capital
     * @param check amount to check
     * @return true if capital is sufficent, else false
     */
    public boolean checkCapital(int check) {
        return capital >= check;
    }

    /**
     * check if players lab level is high enough to upgrade buildings
     * @param newBuildingLvl building lvl to check
     * @return true if building action is valid, else fasle
     */
    private boolean checkLab(int newBuildingLvl) {
        return (buildingLevel[5] + 1) >= newBuildingLvl;
    }

    /**
     * get index of building from building name
     * @param buildingString
     * @return
     */
    public static int getBuildingIndex(String buildingString) {
        switch (buildingString) {
            case "SUGARFARM":
                return 0;
            case "CACAOFARM":
                return 1;
            case "STABLE":
                return 2;
            case "FACTORY":
                return 3;
            case "WAREHOUSE":
                return 4;
            case "LAB":
                return 5;
            default:
                return -1;
        }
    }

    /**
     * get Building in string form from index
     * @param buildingIndex building index
     * @return corresponding string
     */
    public String getIndexBuilding(int buildingIndex) {
        switch(buildingIndex) {
            case 0:
                return "SUGARFARM";
            case 1:
                return "CACAOFARM";
            case 2:
                return "STABLE";
            case 3:
                return "FACTORY";
            case 4:
                return "WAREHOUSE";
            case 5:
                return "LAB";
            default:
                return null;
        }
    }


    /**
     * save monthly sells in temporary array to reset sells
     */
    public void saveProductionUpdate() {
        for (int i = 0; i < soldUpdate.length; i++) {
            soldUpdate[i] = sold[i];
        }
    }

    /**
     * generate String[] to send PRDUP to client
     * @param newMonth actual month
     * @return string[] with produpstats
     */
    public String[] getProductionUpdate(String newMonth) {
        String[] response = new String[7];
        response[0] = "PRDUP";
        response[1] = newMonth;
        response[2] = capital + "," + income;
        response[3] = resourceAmount[0] + "," + soldUpdate[0] + "," + priceUpdate[0];
        response[4] = resourceAmount[1] + "," + soldUpdate[1] + "," + priceUpdate[1];
        response[5] = resourceAmount[2] + "," + soldUpdate[2] + "," + priceUpdate[2];
        response[6] = resourceAmount[3] + "," + soldUpdate[3] + "," + priceUpdate[3];
        for (int i = 0; i < 4; i++) {
            sold[i] = 0;
            soldUpdate[i] = 0;
            price[i] = 0;
            priceUpdate[i] = 0;
        }
        return response;
    }

    /**
     * monthly update;
     * -add monthly production to storage
     * -enable building production from buildings built during last month
     */
    public void updateResources() {
        //update resource storage
        for (int i = 0; i < resourceAmount.length; i++) {
            if (resourceAmount[i] < 0) {
                resourceAmount[i] = 0;
            }
        }
        resourceAmount[3] += resourceProduction[3];
        int excessChoco;
        if (resourceAmount[3] >= resourceStorage[3]) {
            excessChoco = resourceAmount[3] - resourceStorage[3];
            resourceAmount[3] = resourceStorage[3];

            int excessMinRaw = excessChoco / chocoMultiplier;
            resourceAmount[0] += resourceProduction[0] + 2 * excessMinRaw;
            if (resourceAmount[0] > resourceStorage[0]) {
                resourceAmount[0] = resourceStorage[0];
            }
            resourceAmount[1] += resourceProduction[1] + excessMinRaw;
            if (resourceAmount[1] > resourceStorage[1]) {
                resourceAmount[1] = resourceStorage[1];
            }
            resourceAmount[2] += resourceProduction[2] + excessMinRaw;
            if (resourceAmount[2] > resourceStorage[2]) {
                resourceAmount[2] = resourceStorage[2];
            }


        } else {
            for (int i = 0; i < resourceProduction.length - 1; i++) {
                resourceAmount[i] += resourceProduction[i];
                if (resourceAmount[i] > resourceStorage[i]) {
                    resourceAmount[i] = resourceStorage[i];
                }
            }
        }
    }

    /**
     * monthly updated.
     * resets sells from last month
     */
    public void resetSell() {
        for (int i = 0; i < sellAmount.length; i++) {
            sellAmount[i] = 0;
            price[i] = 0;
        }
    }

    /**
     * proccessing of building actions.
     * checks if build or demolish
     * checks capital
     * checks lab level
     * @param request
     * @return
     */
    public String[] build(String[] request) {
        int buildingIndex = getBuildingIndex(request[1]);
        int newLevel = buildingLevel[buildingIndex] + Integer.parseInt(request[2]);
        String[] response = new String[4];
        response[0] = "BLDCK";
        response[1] = request[1];
        boolean ct = false;
        if (request.length == 4) ct = true;
        if (newLevel >= 0 && newLevel < 4) {
            int buildingCost;
            int newProduction;
            int newStorage;
            switch (newLevel) {
                case 0:
                    //demolish - return half of the buildidng costs
                    buildingCost = BuildingStats.getBuilding(buildingIndex * 3).demolitionGain;
                    newProduction = 0;
                    newStorage = 0;
                    break;
                case 1:
                    if (Integer.parseInt(request[2]) < 0) { //demolish - return half of the buildidng costs
                        buildingCost = BuildingStats.getBuilding(buildingIndex * 3 + 1).demolitionGain;
                    } else {
                        buildingCost = BuildingStats.getBuilding(buildingIndex * 3).buildingCost;
                    }
                    newProduction = BuildingStats.getBuilding(buildingIndex * 3).productionPerMonth;
                    newStorage = BuildingStats.getBuilding(buildingIndex * 3).storageCapacity;
                    break;
                case 2:
                    if (Integer.parseInt(request[2]) < 0) { //demolish - return half of the buildidng costs
                        buildingCost = BuildingStats.getBuilding(buildingIndex * 3 + 2).demolitionGain;
                    } else {
                        buildingCost = BuildingStats.getBuilding(buildingIndex * 3 + 1).buildingCost;
                    }
                    newProduction = BuildingStats.getBuilding(buildingIndex * 3 + 1).productionPerMonth;
                    newStorage = BuildingStats.getBuilding(buildingIndex * 3 + 1).storageCapacity;
                    break;
                case 3:
                    buildingCost = BuildingStats.getBuilding(buildingIndex * 3 + 2).buildingCost;
                    newProduction = BuildingStats.getBuilding(buildingIndex * 3 + 2).productionPerMonth;
                    newStorage = BuildingStats.getBuilding(buildingIndex * 3 + 2).storageCapacity;
                    break;
                default:
                    throw new IllegalStateException("Invalid building upgrade: " + newLevel);
            }
            if (checkCapital(buildingCost) && checkLab(newLevel)) { //ensure enough capital and lab level
                if (!ct) {
                    capital -= buildingCost;
                }
                buildingLevel[buildingIndex] = newLevel; //set new building level
                if (buildingIndex < 3) { //case for farm buildings
                    resourceProduction[buildingIndex] = newProduction;
                    getChocoProduction(buildingLevel[3]);
                }
                if (buildingIndex == 3) { //case Factory
                    resourceProduction[3] = getChocoProduction(newLevel);
                }
                updateStorage();

            }
        }
        response[2] = String.valueOf(buildingLevel[buildingIndex]);
        response[3] = String.valueOf(capital);
        return response;
    }

    /**
     * update chocolate production by checking needed raw materials, storage etc..
     * @param factoryLevel actual factory level
     * @return new choco production
     */
    public int getChocoProduction(int factoryLevel) {
        int minRawProduction = (int) ((Math.min(Math.min(
                BuildingStats.getProduction("SUGARFARM", buildingLevel[0]) * 0.5,
                BuildingStats.getProduction("CACAOFARM", buildingLevel[1])),
                BuildingStats.getProduction("STABLE", buildingLevel[2]))));
        int newChocoProduction = chocoMultiplier * minRawProduction;
        if (factoryLevel == 0) newChocoProduction = 0;
        if (factoryLevel > 0 && newChocoProduction < BuildingStats.getProduction("FACTORY", factoryLevel)) {
            resourceProduction[0] = BuildingStats.getProduction("SUGARFARM", buildingLevel[0])
                    - 2 * minRawProduction;
            resourceProduction[1] = BuildingStats.getProduction("CACAOFARM", buildingLevel[1])
                    - minRawProduction;
            resourceProduction[2] = BuildingStats.getProduction("STABLE", buildingLevel[2])
                    - minRawProduction;
        }
        if (factoryLevel > 0 && newChocoProduction >= BuildingStats.getProduction("FACTORY", factoryLevel)) {
            resourceProduction[0] = BuildingStats.getProduction("SUGARFARM", buildingLevel[0])
                    - BuildingStats.getProduction("FACTORY", factoryLevel) / chocoMultiplier * 2;
            resourceProduction[1] = BuildingStats.getProduction("CACAOFARM", buildingLevel[1])
                    - BuildingStats.getProduction("FACTORY", factoryLevel) / chocoMultiplier;
            resourceProduction[2] = BuildingStats.getProduction("STABLE", buildingLevel[2])
                    - BuildingStats.getProduction("FACTORY", factoryLevel) / chocoMultiplier;
            newChocoProduction = BuildingStats.getProduction("FACTORY", factoryLevel);
        }
        resourceProduction[3] = newChocoProduction;
        return newChocoProduction;
    }

    /**
     * update storage amount for all resources.
     * called after building
     */
    public void updateStorage() {
        int warehouseStorage;
        if (buildingLevel[4] == 0) {
            warehouseStorage = 0;
        } else if (buildingLevel[4] == 1) {
            warehouseStorage = BuildingStats.getStorage("WAREHOUSE", 1);
        } else {
            warehouseStorage = BuildingStats.getStorage("WAREHOUSE", buildingLevel[4]);
        }
        for (int i = 0; i < resourceStorage.length; i++) {
            if (buildingLevel[i] != 0) {
                String building = getIndexBuilding(i);
                int buildingLevel = this.buildingLevel[i];
                resourceStorage[i] = BuildingStats.getStorage(building, buildingLevel) + warehouseStorage;
            } else {
                resourceStorage[i] = warehouseStorage;
            }
        }
    }

    /**
     * get the building with the highest level
     * @return building string
     */
    public String getMaxBuilding() {
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < buildingLevel.length; i++) {
            if (buildingLevel[i] > max) {
                max = buildingLevel[i];
                maxIndex = i;
            }
        }
        return getIndexBuilding(maxIndex);
    }

    /**
     * booster handling
     * todo: implement fertilizer
     * @param request request from client
     * @return response to client
     */
    public String[] getBoost(String[] request) {
        Booster booster = null;
        String[] response = new String[3];
        if (Booster.enumContainsValue(request[1])) {
            booster = Booster.valueOf(request[1]);
        } else response[1] = Booster.DEFAULT.name();
        response[0] = "BSTCK";
        assert booster != null;
        if (checkCapital(booster.costs) && checkLab(booster.labNeeded + 1)) {
                switch (booster) {
                    case FERTILIZER1:
                    case FERTILIZER2:
                        getFertilizerBoost(booster);
                        break;
                    case SPY:
                        getSpyBooster(booster);
                        break;
                    case SABOTAGE:
                        if (hasSpy()) {
                            getSabotageBooster(booster);
                        }
                        break;
                }
                if (booster == Booster.SABOTAGE && !hasSpy()) {
                    response[1] = Booster.DEFAULT.name();
                } else {
                    response[1] = booster.name();
                }

        } else {
            response[1] = Booster.DEFAULT.name();
        }
        response[2] = String.valueOf(capital);
        return response;
    }

    /**
     * handle the buy of sabotage booster
     * @param booster booster object
     */
    private void getSabotageBooster(Booster booster) {
        if (checkLab(booster.labNeeded + 1) && checkCapital(booster.costs) && hasSpy()) {
            capital -= booster.costs;
            sabot = true;
        }
    }

    /**
     * handling for spy booster
     * @param booster Booster object of the spy
     * @return
     */
    private String getSpyBooster(Booster booster) {
        spy = true;
        capital -= booster.costs;
        return booster.name();
    }

    /**
     * handling for fertilizer booster
     * todo: make it work
     * @param booster Booster object of fertilizer 1 or 2
     * @return
     */
    private String getFertilizerBoost(Booster booster) {
            capital -= booster.costs;
            for (int i = 0; i < 3; i++) {
                resourceProduction[i] = (int) (resourceProduction[i] * booster.multiplicator);
            }
            return booster.name();
    }

    /**
     * called to check if spy is present
     * @return true if spy, false else
     */
    public boolean hasSpy() {
        return spy;
    }

    /**
     * check if player has a saboteur (bought the sabotage booster)
     * @return true if has, false else
     */
    public boolean hasSaboteur() {
        return sabot;
    }
}
