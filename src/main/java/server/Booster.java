package server;

public enum Booster {


    FERTILIZER1(5000, 1.2, "FARMS", 1),
    FERTILIZER2(15000, 1.5, "FARMS", 2),

    SPY(5000, 1, "NONE", 1),
    SABOTAGE(10000, 0.5, "CHOOSE", 2),
    OOMPALOOMPA(5000, 1.2, "FACTORY", 3),


    DEFAULT(0, 1, "NONE", 0);

    private final static Booster[] boosterList = Booster.values();
    public final int costs;   // in Credits (capital)
    public final double multiplicator;
    public final String affectedBuildings;
    public final int labNeeded;


    /**
     * Booster enum
     * @param costs costs of a booster
     * @param multiplicator Multiplier given by booster
     * @param affectedBuildings buildings affected by booster
     * @param labNeeded lab needed for booster
     */
    Booster(int costs, double multiplicator, String affectedBuildings, int labNeeded) {
        this.costs = costs;
        this.multiplicator = multiplicator;
        this.affectedBuildings = affectedBuildings;
        this.labNeeded = labNeeded;

    }

    /**
     * check if enum contains a certain value
     *
     * @param value the value to check
     * @return true or false
     */
    public static boolean enumContainsValue(String value) {
        for (Booster booster : Booster.values()) {
            if (booster.name().equals(value)) return true;
        }
        return false;
    }

    /**
     * @param boosterName Name of the booster
     * @return cost of the booster
     */
    public static int getCosts(String boosterName) {
        int costs = -1;
        for (Booster booster : boosterList) {
            if (booster.name().equals(boosterName)) {
                costs = booster.costs;
            }
        }
        return costs;
    }

    /**
     * @param boosterName Name of the booster
     * @return multiplier provided by the booster
     */
    public static double getMultiplier(String boosterName) {
        double multiplier = 1;
        for (Booster booster : boosterList) {
            if (booster.name().equals(boosterName)) {
                multiplier = booster.multiplicator;
            }
        }
        return multiplier;
    }

    /**
     * @param boosterName Name of the booster
     * @return lablevel needed for the booster
     */
    public static int getLabNeeded(String boosterName) {
        int labNeeded = 0;
        for (Booster booster : boosterList) {
            if (booster.name().equals(boosterName)) {
                labNeeded = booster.labNeeded;
            }
        }
        return labNeeded;
    }
}
