package server;

import com.google.common.collect.BiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class EcoSim {

    public Integer[][] limit;
    public int[] dynamicSellPrice;
    public int[] dynamicBuyPrice;
    public final int[][] sellPriceRange;
    public final int[] baseSellPrice;
    public final int[] baseBuyPrice;
    public BiMap<ConnectionHandler, Player> playerList;
    Integer[][] monthlyEcoStats;
    private final int playerCount;
    private int[] eventMultiplier;
    private int winCapital;
    private static final Logger logger = LogManager.getLogger(GameInstance.class);

    /**
     * The Ecosim, core of the game.
     * Simulates the Economy, calculaltes all sells and handles the prices
     * @param playerList list of the players
     * @param playerCount couning the players
     * @param winCapital decided win capital at start
     */
    public EcoSim(BiMap<ConnectionHandler, Player> playerList, int playerCount, int winCapital) {

        this.winCapital = winCapital;
        eventMultiplier = new int[]{1, 1, 1, 1};
        baseBuyPrice = new int[4];
        baseSellPrice = new int[4];
        limit = new Integer[][]{{0, 300, 15}, {0, 200, 10}, {0, 300, 15}, {0, 500, 25}};

        //set base prices
        baseSellPrice[0] = 24 * eventMultiplier[0];
        baseSellPrice[1] = 36 * eventMultiplier[1];
        baseSellPrice[2] = 24 * eventMultiplier[2];
        baseSellPrice[3] = 108 * eventMultiplier[3];
        baseBuyPrice[0] = 40 * eventMultiplier[0];
        baseBuyPrice[1] = 60 * eventMultiplier[1];
        baseBuyPrice[2] = 40 * eventMultiplier[2];
        baseBuyPrice[3] = 180 * eventMultiplier[3];

        //set price range for dynamic prices
        sellPriceRange = new int[4][2];
        for (int i = 0; i < sellPriceRange.length; i++) {
            sellPriceRange[i][0] = baseSellPrice[i];
            sellPriceRange[i][1] = (int) (baseSellPrice[i] * 0.75);
        }


        dynamicSellPrice = new int[4];
        dynamicBuyPrice = new int[4];
        //set dynamic prices as standard prices
        for (int i = 0; i < dynamicSellPrice.length; i++) {
            dynamicSellPrice[i] = baseSellPrice[i];
            dynamicBuyPrice[i] = baseBuyPrice[i];
        }


        this.playerList = playerList;

        monthlyEcoStats = new Integer[4][10];
        this.playerCount = playerCount;
    }


    /**
     * Most important method:
     * calculate the sells and buys for the month
     * check which player offers the best price and buy/sell this first
     * store all values for monthly update
     *
     */
    public void sellToMonthlyUpdate() {
        for (int i = 0; i < monthlyEcoStats.length; i++) {
            for (int j = 0; j < monthlyEcoStats[0].length; j++) {
                monthlyEcoStats[i][j] = 0;
            }
        }

        int[][] sellPrices = getSellPriceArray();
        int[][] buyPrices = getBuyPriceArray();
        Integer[][][] sold = new Integer[4][2][]; //resourceindex,amount/price,int
        Integer[][][] bought = new Integer[4][2][];

        for (int i = 0; i < 4; i++) { //loop over all resources

            ArrayList<Integer> soldAmountList = new ArrayList<>();
            ArrayList<Integer> soldPriceList = new ArrayList<>();
            //Buying resources from players
            sort2DArrayRowsAscending(sellPrices);
            //3. loop through array, set players sugarSold, set players capital
            for (int j = 0; j < sellPrices[0].length; j++) {
                //loop through sorted array
                for (BiMap.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) { //loop over all players
                    Player player = entry.getValue();
                    //System.out.println("Buying from player " + player.getName());
                    if (player.sellAmount[i] > 0) { //check if player wants to sell
                        if (player.price[i] == sellPrices[i][j]) { //if player has lowest selling price..
                            player.sold[i] = getSellAmount(player.sellAmount[i], i); //set sold amount
                            player.sellAmount[i] = 0;
                            if (player.sold[i] > 0) {
                                soldAmountList.add(player.sold[i]);
                                soldPriceList.add(player.price[i]);
                                player.resourceAmount[i] -= player.sold[i];
                            }
                            player.capital += player.sold[i] * player.price[i]; //calc new capital
                            player.priceUpdate[i] = player.price[i];
                            player.soldUpdate[i] = player.sold[i];
                            player.price[i] = 0;
                            limit[i][0] += player.sold[i]; //set new lower limit
                            //System.out.println("Player " + player.getName() + " sold " + player.sold[i] + " " + getIndexResource(i));
                            //System.out.println("New capital: " + player.capital);
                            //System.out.println("Buying " + getIndexResource(i) + " finished");
                        }
                    }
                }
            }
            sold[i][0] = soldAmountList.toArray(new Integer[0]);
            sold[i][1] = soldPriceList.toArray(new Integer[0]);
        }

        for (int i = 0; i < buyPrices[0].length; i++) { //loop over all resources

            ArrayList<Integer> boughtAmountList = new ArrayList<>();
            ArrayList<Integer> boughtPriceList = new ArrayList<>();
            //Buying resources from players
            sort2DArrayRowsDescending(buyPrices);
            //3. loop through array, set players sugarSold, set players capital
            for (int j = 0; j < sellPrices[0].length; j++) {
                //loop through sorted array
                for (BiMap.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) { //loop over all players
                    Player player = entry.getValue();
                    if (player.sellAmount[i] < 0) { //check if player wants to buy
                        if (player.price[i] == sellPrices[i][j]) { //if player has highest buy price..
                            player.sold[i] = getSellAmount(player.sellAmount[i], i); //set sold amount
                            player.sellAmount[i] = 0;
                            if (player.sold[i] < 0) {
                                boughtAmountList.add(player.sold[i]);
                                boughtPriceList.add(player.price[i]);
                                player.resourceAmount[i] -= player.sold[i];
                            }
                            player.capital += player.sold[i] * player.price[i]; //calc new capital);
                            player.priceUpdate[i] = player.price[i];
                            player.soldUpdate[i] = player.sold[i];
                            player.price[i] = 0;
                            limit[i][0] += player.sold[i]; //set new lower limit
                            //System.out.println("Player " + player.getName() + " sold " + player.sold[i] + " " + getIndexResource(i));
                            //System.out.println("New capital: " + player.capital
                            //System.out.println("Buying " + getIndexResource(i) + " finished");
                        }
                    }
                }
            }
            bought[i][0] = boughtAmountList.toArray(new Integer[0]);
            bought[i][1] = boughtPriceList.toArray(new Integer[0]);
        }




        //resetSold();

        updateMarketPrices();

        calculateEcoUpdate(sold, bought);

        //System.out.println("--- finished sellToMonthlyUpdate in ecoSim --- ");
    }

    /**
     * reset all sells from last month
     */
    private void resetSold() {
        for (BiMap.Entry<ConnectionHandler, Player> entry: playerList.entrySet()) {
            Player player = entry.getValue();
            player.saveProductionUpdate();
            for (int i = 0; i < 4; i++) {
                player.sold[i] = 0;
            }
        }
    }

    /**
     * calculate new marketprices according to limits (stock on market)
     */
    public void updateMarketPrices() {
        for (int i = 0; i < sellPriceRange.length; i++) {
            double numerator = sellPriceRange[i][0] - sellPriceRange[i][1];
            double denom = limit[i][1];
            dynamicSellPrice[i] = (int) ((-numerator / denom) * limit[i][0] + sellPriceRange[i][0]);
            dynamicBuyPrice[i] = dynamicSellPrice[i] * 5/3;
        }
    }


    /**
     * get all sell prices fromall players
     * @return int[][] first dimension: all resources, 2nd dimension: all players
     */
    private int[][] getSellPriceArray() {
        int[][] priceList = new int[4][playerList.size()];
        for (int i = 0; i < priceList.length; i++) {//loop over all resources
            int j = 0;
            for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
                Player player = entry.getValue();
                if (player.sellAmount[i] > 0) {
                    priceList[i][j] = player.price[i];
                    j++;
                }
            }
        }
        return priceList;
    }

    /**
     * get all buy prices fromall players
     * @return int[][] first dimension: all resources, 2nd dimension: all players
     */
    private int[][] getBuyPriceArray() {
        int[][] priceList = new int[4][playerList.size()];
        for (int i = 0; i < priceList.length; i++) {
            int j = 0;
            for (Map.Entry<ConnectionHandler, Player> entry : playerList.entrySet()) {
                Player player = entry.getValue();
                if (player.sellAmount[i] < 0) {
                    priceList[i][j] = player.price[i];
                    j++;
                }
            }
        }
        return priceList;
    }

    /**
     * write data from monthly update to monthlyEcoStats[][]
     *
     * @param soldArray array of all sells this month
     * @param boughtArray array of all boughts this motnh
     */
    private void calculateEcoUpdate(Integer[][][] soldArray, Integer[][][] boughtArray) {
        //System.out.println("Calculating ecoupdate...");
        for (int i = 0; i < soldArray.length; i++) {
            monthlyEcoStats[i][0] = dynamicSellPrice[i];
            monthlyEcoStats[i][1] = dynamicBuyPrice[i];
            monthlyEcoStats[i][8] = limit[i][1] - limit[i][0];
            monthlyEcoStats[i][9] = limit[i][0];
            if (soldArray[i][0] != null) {
                if (soldArray[i][0].length == 1) {
                    monthlyEcoStats[i][2] = soldArray[i][1][0];
                    monthlyEcoStats[i][3] = soldArray[i][1][0];
                    monthlyEcoStats[i][4] = soldArray[i][1][0];
                } else if (soldArray[i][0].length > 1) {
                    monthlyEcoStats[i][3] = soldArray[i][1][0];
                    monthlyEcoStats[i][2] = soldArray[i][1][soldArray[i][1].length - 1];
                    double sum = 0;
                    double denom = 0;
                    for (int j = 0; j < soldArray[i][0].length; j++) {
                        sum += Math.abs(soldArray[i][0][j] * soldArray[i][1][j]);
                        denom += Math.abs(soldArray[i][0][j]);
                    }
                    monthlyEcoStats[i][4] = (int) (sum / denom);
                }
            }


            if (boughtArray[i][0] != null) {

                if (boughtArray[i][0].length == 1) {
                    monthlyEcoStats[i][5] = boughtArray[i][1][0];
                    monthlyEcoStats[i][6] = boughtArray[i][1][0];
                    monthlyEcoStats[i][7] = boughtArray[i][1][0];
                } else if (boughtArray[i][0].length > 1) {
                    monthlyEcoStats[i][5] = boughtArray[i][1][0];
                    monthlyEcoStats[i][6] = boughtArray[i][1][boughtArray[i][1].length - 1];
                    double sum = 0;
                    double denom = 0;
                    for (int j = 0; j < boughtArray[i][0].length; j++) {
                        sum += Math.abs(boughtArray[i][0][j] * boughtArray[i][1][j]);
                        denom += Math.abs(boughtArray[i][0][j]);
                    }
                    monthlyEcoStats[i][7] = (int) (sum / denom);
                }
            }
        }
        //System.out.println("New EcoStatistics: " + Arrays.deepToString(monthlyEcoStats));
        //System.out.println("Calculating ecoupdate finished!");
    }

    /**
     * check limits to determine sell/buy amounts
     * @param amountToSell wts amount
     * @param resourceIndex index of resource
     * @return effective sell amount
     */
    private int getSellAmount(int amountToSell, int resourceIndex) {
        int effectiveSellAmount = amountToSell;
        if ((limit[resourceIndex][0] + amountToSell) > limit[resourceIndex][1]) {
            effectiveSellAmount = amountToSell - (limit[resourceIndex][1] - limit[resourceIndex][0]);
            limit[resourceIndex][0] = limit[resourceIndex][1];
        } else if ((limit[resourceIndex][0] + amountToSell) < 0) {
            effectiveSellAmount = amountToSell + limit[resourceIndex][0];
            limit[resourceIndex][0] = 0;
        }
        //System.out.println("getSellAmount method: amountToSell = " + amountToSell + "  effectiveSellAmount = " + effectiveSellAmount);
        return effectiveSellAmount;
    }

    /**
     * handling of sells
     * @param request request from client
     * @param player playr objcet of player that wants to sell
     * @return respons SELCK according to protocol
     */
    public String[] sellTo(String[] request, Player player) {
        //System.out.println("sellTo in Ecosim activated");
        String[] response = new String[4];
        response[0] = "SELCK";
        response[1] = request[1];
        response[3] = request[3];
        int resourceIndex = getResourceIndex(request[1]);
        int sellAmount = Integer.parseInt(request[2]);
        int price = Integer.parseInt(request[3]);
        if (sellAmount > 0) {
            if (checkResourceAmount(sellAmount, resourceIndex, player)
                    && checkSellPriceDeviation(price, resourceIndex)) {
                response[2] = request[2];
                player.sellAmount[resourceIndex] = sellAmount;
                player.price[resourceIndex] = price;
            } else {
                response[2] = "0";
            }
        } else {
            int buyPrice = sellAmount * dynamicBuyPrice[resourceIndex];
            if (checkCapital(buyPrice, player)
                    && checkBuyPriceDeviation(price, resourceIndex)) {
                response[2] = request[2];
                player.sellAmount[resourceIndex] = sellAmount;
                player.price[resourceIndex] = price;
            } else {
                response[2] = "0";
            }
        }
        return response;
    }


    /**
     * write response[] with ecoupdate, according to protocol
     * @return returns an Stringarray with the economic updates for the clients
     */
    public String[] getEcoUpdate() {


        String[] response = new String[5];
        response[0] = NetworkProtocol.ECOUP.toString();
        for (int i = 0; i < monthlyEcoStats.length; i++) {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (int j = 0; j < monthlyEcoStats[0].length; j++) {
                stringJoiner.add(String.valueOf(monthlyEcoStats[i][j]));
            }
            response[i + 1] = stringJoiner.toString();
        }
        return response;
    }

    /**
     * set new limits (monthly)
     */
    public void updateLimits() {
        for (int i = 0; i < limit.length; i++) {
            limit[i][1] += (int) (0.9 * limit[i][2] * playerCount);
            limit[i][0] -= (int) (0.3 * limit[i][1]);
            if (limit[i][0] < 0) {
                limit[i][0] = 0;
            }
        }
    }


    /**
     * check if player has enough capital
     * @param check $ amount to check
     * @param player player object
     * @return true if capital is sufficent, false else
     */
    private boolean checkCapital(int check, Player player) {
        return player.capital >= check;
    }

    /**
     * check if player buy price is not too low to avoid exploitation
     * @param price price to check
     * @param resourceIndex resource index
     * @return true if valid buy, false else
     */
    private boolean checkBuyPriceDeviation (int price, int resourceIndex) {
        return !(price < 0.75 * dynamicBuyPrice[resourceIndex]);
    }

    /**
     * check if player sell price is not too low to avoid exploitation
     * @param price price to check
     * @param resourceIndex resource index
     * @return true if valid sell, false else
     */
    private boolean checkSellPriceDeviation (int price, int resourceIndex) {
        return !(price > 1.25 * dynamicBuyPrice[resourceIndex]);
    }

    /**
     * check if player has enough resources
     * @param change resource amount change
     * @param resourceIndex index of resource
     * @param player player object
     * @return true if enough resources are present, false else
     */
    private boolean checkResourceAmount(int change, int resourceIndex, Player player) {
            return change <= player.resourceAmount[resourceIndex];
    }

    /**
     * get index of a resource from name
     * @param s resource String name
     * @return index of that resource
     */
    private int getResourceIndex(String s) {
        int index;
        switch (s) {
            case "SUGR":
            case "SUGAR":
                index = 0;
                break;
            case "CACO":
            case "CACAO":
                index = 1;
                break;
            case "MLK":
            case "MILK":
                index = 2;
                break;
            case "CHOC":
            case "CHOCO":
                index = 3;
                break;
            default:
                index = -1;
                break;
        }
        return index;
    }

    /**
     * bubble sorting to sort values inside of every row of an int[][] in ascending order
     * @param arr
     */
    static void sort2DArrayRowsAscending(int[][] arr){
        int n = arr.length;
        int m = arr[0].length;
        int i, j, k, temp;
        for(k = 0; k < n; ++k) {
            for(i = 0; i < m; ++i) {
                for(j = 0; j < m - 1 - i; ++j) {
                    if (arr[k][j] > arr[k][j+1]) {
                        temp = arr[k][j+1];
                        arr[k][j+1] = arr[k][j];
                        arr[k][j] = temp;
                    }
                }
            }
        }
    }

    /**
     * bubble sorting to sort values inside of every row of an int[][] in descending order
     * @param arr
     */
    static void sort2DArrayRowsDescending(int[][] arr){
        int n = arr.length;
        int m = arr[0].length;
        int i, j, k, temp;
        for (k = 0; k < n; ++k) {
            for (i = 0; i < m; ++i) {
                for (j = 0; j < m - 1 - i; ++j) {
                    if (arr[k][j] < arr[k][j+1]) {
                        temp = arr[k][j+1];
                        arr[k][j+1] = arr[k][j];
                        arr[k][j] = temp;
                    }
                }
            }
        }
    }

    /**
     * called if an event occurs
     * @param event true or false
     */
    public void setEvent(boolean event) {
        if (event) {
            eventMultiplier[3] = 2;
        } else {
            eventMultiplier[3] = 1;
        }
    }
}
