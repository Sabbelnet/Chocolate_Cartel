package server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class EcoSimTest {

    EcoSim ecoSimTest;
    Player testPlayer;
    BiMap<ConnectionHandler, Player> playerList;
    private ConnectionHandler connectionHandler;

    @Before
    public void setup() {
        testPlayer = new Player("testPlayer1");
        playerList = HashBiMap.create();
        playerList.put(Mockito.mock(ConnectionHandler.class), testPlayer);
        ecoSimTest = new EcoSim(playerList, 0, 10000000);

    }

    @Test
    public void testSellToSugar() {
        String[] expectedResponse = {"SELCK", "SUGAR", "0", "20"};
        String[] request = getSellToString(0, 10, 20);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        buildSugarfarm(testPlayer);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        doMonthlyUpdate();
        expectedResponse[2] = "10";
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));
        assertEquals(50, testPlayer.resourceAmount[0]);
        assertEquals(8000, testPlayer.capital);

        doMonthlyUpdate();
        assertEquals(90, testPlayer.resourceAmount[0]);
        assertEquals(8200, testPlayer.capital);
    }

    @Test
    public void testSellToCacao() {
        String[] expectedResponse = {"SELCK", "CACAO", "0", "32"};
        String[] request = getSellToString(1, 10, 32);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        buildCacaofarm(testPlayer);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        doMonthlyUpdate();
        expectedResponse[2] = "10";
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));
        assertEquals(25, testPlayer.resourceAmount[1]);
        assertEquals(8000, testPlayer.capital);

        doMonthlyUpdate();
        assertEquals(40, testPlayer.resourceAmount[1]);
        assertEquals(8320, testPlayer.capital);
    }

    @Test
    public void testSellToMilk() {
        String[] expectedResponse = {"SELCK", "MILK", "0", "20"};
        String[] request = getSellToString(2, 10, 20);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        buildStable(testPlayer);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        doMonthlyUpdate();
        expectedResponse[2] = "10";
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));
        assertEquals(25, testPlayer.resourceAmount[2]);
        assertEquals(8000, testPlayer.capital);

        doMonthlyUpdate();
        assertEquals(40, testPlayer.resourceAmount[2]);
        assertEquals(8200, testPlayer.capital);
    }

    @Test
    public void testSellToChocolate() {
        String[] expectedResponse = {"SELCK", "CHOCO", "0", "135"};
        String[] request = getSellToString(3, 10, 135);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        buildSugarfarm(testPlayer);
        buildCacaofarm(testPlayer);
        buildStable(testPlayer);
        buildFactory(testPlayer);
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));

        doMonthlyUpdate();
        expectedResponse[2] = "10";
        assertArrayEquals(expectedResponse, ecoSimTest.sellTo(request, testPlayer));
        assertEquals(50, testPlayer.resourceAmount[3]);
        assertEquals(0, testPlayer.capital);

        doMonthlyUpdate();
        assertEquals(90, testPlayer.resourceAmount[3]);
        assertEquals(1350, testPlayer.capital);
    }



    @Test
    public void testPriceUpdate1() {
        //check base prices at start
        assertArrayEquals(new int[]{24, 36, 24, 108}, ecoSimTest.dynamicSellPrice);

        //raise limits to max
        for (int i = 0; i < ecoSimTest.limit.length; i++) {
            ecoSimTest.limit[i][0] = ecoSimTest.limit[i][1];
        }
        ecoSimTest.updateMarketPrices();

        //check new prices
        for (int i = 0; i < ecoSimTest.sellPriceRange.length; i++) {
            assertEquals(ecoSimTest.sellPriceRange[i][1], ecoSimTest.dynamicSellPrice[i]);
        }
    }

    @Test
    public void testPriceUpdate2() {
        //check base prices at start
        assertArrayEquals(new int[]{24, 36, 24, 108}, ecoSimTest.dynamicSellPrice);

        testPlayer.capital += 100000;
        testPlayer.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer.build(new String[]{"BUILD", "SUGARFARM", "1",});
        testPlayer.build(new String[]{"BUILD", "SUGARFARM", "1",});
        testPlayer.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(3, testPlayer.buildingLevel[0]);

        testPlayer.updateResources();
        testPlayer.updateResources();
        ecoSimTest.sellTo(new String[]{"SELTO","SUGAR","300","20"}, testPlayer);

        ecoSimTest.sellToMonthlyUpdate();
        ecoSimTest.updateMarketPrices();
        assertEquals(ecoSimTest.limit[0][1], ecoSimTest.limit[0][0]);

        //check new prices
        assertEquals(ecoSimTest.sellPriceRange[0][1], ecoSimTest.dynamicSellPrice[0]);

        String[] toTest = ecoSimTest.getEcoUpdate();
        assertEquals("18,30,20,20,20,0,0,0,0,300", toTest[1]);


    }

    private String[] getSellToString(int resourceIndex, int amount, int price) {
        String resourceString = "";
        switch (resourceIndex) {
            case 0:
                resourceString = "SUGAR";
                break;
            case 1:
                resourceString = "CACAO";
                break;
            case 2:
                resourceString = "MILK";
                break;
            case 3:
                resourceString = "CHOCO";
                break;
        }
        return new String[]{"SELTO",resourceString, String.valueOf(amount), String.valueOf(price)};
    }

    private void doMonthlyUpdate() {
        ecoSimTest.sellToMonthlyUpdate();
        testPlayer.resetSell();
        ecoSimTest.updateLimits();
        testPlayer.updateResources();
    }

    private void buildSugarfarm(Player player) {
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
    }
    private void buildCacaofarm(Player player) {
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
    }
    private void buildStable(Player player) {
        player.build(new String[]{"BUILD", "STABLE", "1",});
    }
    private void buildFactory(Player player) {
        player.build(new String[]{"BUILD", "FACTORY", "1",});
    }
    private void buildWarehouse(Player player) {
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
    }
    private void buildLab(Player player) {
        player.build(new String[]{"BUILD", "LAB", "1",});
    }

    private String[][] getProductionUpdateStringArray(Player player) {
        String[] prodUp1d = player.getProductionUpdate("Month");
        String[][] prodUp = new String[7][];
        prodUp[0] = new String[]{"PRDUP"};
        prodUp[1] = new String[]{"Month"};
        prodUp[2] = prodUp1d[2].split(",");
        prodUp[3] = prodUp1d[3].split(",");
        prodUp[4] = prodUp1d[4].split(",");
        prodUp[5] = prodUp1d[5].split(",");
        prodUp[6] = prodUp1d[6].split(",");
        System.out.println(Arrays.deepToString(prodUp));
        return prodUp;
    }

    @Test
    public void testTestMethods() {
        getProductionUpdateStringArray(testPlayer);
    }
}


