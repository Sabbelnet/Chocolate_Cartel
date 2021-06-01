package server;

public enum BuildingStats {

    SUGARFARM1 (2000, -1000, 50, 100),
    SUGARFARM2 (3000, -1500, 100, 200),
    SUGARFARM3 (4000, -2000,200,400),

    CACAOFARM1 (2000, -1000, 25, 50),
    CACAOFARM2 (3000, -1500, 50, 100),
    CACAOFARM3 (4000, -2000, 100, 200),

    STABLE1 (2000, -1000, 25, 50),
    STABLE2 (3000, -1500, 50, 100),
    STABLE3 (4000, -2000, 100, 200),

    FACTORY1 (4000, -2000,80, 160),
    FACTORY2 (6000, -3000,150, 300),
    FACTORY3 (10000, -5000, 300, 600),

    WAREHOUSE1 (3000, -1500, 0, 500),
    WAREHOUSE2 (5000, -3000, 0, 1000),
    WAREHOUSE3 (8000, -4000, 0, 2000),

    LAB1 (5000, -2500,0,0),
    LAB2 (8000, -4000,0,0),
    LAB3 (12000, -6000,0,0);


    public final int buildingCost;   // in Credits (capital)
    public final int productionPerMonth;
    public final int demolitionGain;
    public final int storageCapacity;


    private final static BuildingStats[] buildingList = BuildingStats.values();

    /**
     * Enum with stats for all buildings
     * @param buildingCost cost of a building
     * @param demolitionGain cost return in case of demolition
     * @param productionPerMonth the monthly production
     * @param storageCapacity storage capacity
     */
    BuildingStats(int buildingCost, int demolitionGain, int productionPerMonth, int storageCapacity) {
        this.buildingCost = buildingCost;
        this.productionPerMonth = productionPerMonth;
        this.demolitionGain = demolitionGain;
        this.storageCapacity = storageCapacity;
    }


    /**
     * get building by index
     * @param i is the number of the building
     * @return returns the Buildingstats
     */
    public static BuildingStats getBuilding(int i) {
        return buildingList[i];
    }

    /**
     * check if enum contains value
     * @param value string building
     * @return true if exists, false else
     */
    public static boolean enumContainsValue(String value)
    {
        for (BuildingStats buildingStats : BuildingStats.values())
        {
            if (buildingStats.name().equals(value))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * get production of of a building at a level
     * @param building building string
     * @param level level
     * @return production at level
     */
    public static int getProduction(String building, int level) {
        if (level == 0) {
            return 0;
        } else {
            switch (building) {
                case "SUGARFARM":
                    return getBuilding(level - 1).productionPerMonth;
                case "CACAOFARM":
                    return getBuilding(2 + level).productionPerMonth;
                case "STABLE":
                    return getBuilding(5 + level).productionPerMonth;
                case "FACTORY":
                    return getBuilding(8 + level).productionPerMonth;
                default:
                    return -1000;
            }
        }
    }

    /**
     * get storage of a building with fixed level
     * @param building building string
     * @param level level of building
     * @return storage capacity of building at level
     */
    public static int getStorage(String building, int level) {
        switch (building) {
            case "SUGARFARM":
                return getBuilding(level - 1).storageCapacity;
            case "CACAOFARM":
                return getBuilding(2 + level).storageCapacity;
            case "STABLE":
                return getBuilding(5 + level).storageCapacity;
            case "FACTORY":
                return getBuilding(8 + level).storageCapacity;
            case "WAREHOUSE":
                return getBuilding(11 + level).storageCapacity;
            default: return -100000;
        }
    }


    /**
     * get monthly production of a building
     * @return production per month
     */
    public int getProduction() {
        return productionPerMonth;
    }

    /**
     * get building costs
     * @return building cost
     */
    public int getBuildingCost() {
        return buildingCost;
    }

    /**
     * Used to get the demolition gain of a building
     * @return demolition gain
     */
    public int getDemolitionGain() {
        return demolitionGain;
    }


}
