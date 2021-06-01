package server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class PlayerTest {

    Player testPlayer1;
    Player testPlayerAllLevel1;
    Player testPlayerAllLevel2;
    Player testPlayerAllLevel3;

    String sugarfarm = "SUGARFARM";
    String cacaofarm = "CACAOFARM";
    String stable = "STABLE";
    String factory = "FACTORY";
    String warehouse = "WAREHOUSE";
    String lab = "LAB";

    int chocoMultiplier;

    @Before
    public void setUp() {
        testPlayer1 = new Player("testPlayer1");
        testPlayer1.capital = Integer.MAX_VALUE;
        chocoMultiplier = testPlayer1.chocoMultiplier;

        testPlayerAllLevel1 = new Player("testPlayerAllLevel1");
        testPlayerAllLevel1.capital = Integer.MAX_VALUE;
        buildAll(testPlayerAllLevel1);

        testPlayerAllLevel2 = new Player("testPlayerAllLevel2");
        testPlayerAllLevel2.capital = Integer.MAX_VALUE;
        buildAllLevel2(testPlayerAllLevel2);

        testPlayerAllLevel3 = new Player("testPlayerAllLevel3");
        testPlayerAllLevel3.capital = Integer.MAX_VALUE;
        buildAllLevel3(testPlayerAllLevel3);
    }

    @Test
    public void testBuildResponseFormat() {
        String[] response;
        response = testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1"});
        assertArrayEquals(new String[]{"BLDCK", "SUGARFARM", "1", String.valueOf(testPlayer1.capital)}, response);

        response = testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1"});
        assertArrayEquals(new String[]{"BLDCK", "CACAOFARM", "1", String.valueOf(testPlayer1.capital)}, response);

        response = testPlayer1.build(new String[]{"BUILD", "STABLE", "1"});
        assertArrayEquals(new String[]{"BLDCK", "STABLE", "1", String.valueOf(testPlayer1.capital)}, response);

        response = testPlayer1.build(new String[]{"BUILD", "FACTORY", "1"});
        assertArrayEquals(new String[]{"BLDCK", "FACTORY", "1", String.valueOf(testPlayer1.capital)}, response);

        response = testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1"});
        assertArrayEquals(new String[]{"BLDCK", "WAREHOUSE", "1", String.valueOf(testPlayer1.capital)}, response);

        response = testPlayer1.build(new String[]{"BUILD", "LAB", "1"});
        assertArrayEquals(new String[]{"BLDCK", "LAB", "1", String.valueOf(testPlayer1.capital)}, response);


    }

    @Test
    public void testSugarfarmLevel1() {
        buildSugarfarm(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction(sugarfarm,1), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage(sugarfarm, 1), testPlayer1.resourceStorage[0]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testCacaofarmLevel1() {
        buildCacaofarm(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",1), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 1), testPlayer1.resourceStorage[1]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testStableLevel1() {
        buildStable(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",1), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 1), testPlayer1.resourceStorage[2]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testFactoryLevel1() {
        buildFactory(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 1), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testWarehouseLevel1() {
        buildWarehouse(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testLabLevel1() {
        buildLab(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[5]); //check buildinglevel

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testSugarfarmLevel2() {
        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("SUGARFARM",2), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage("SUGARFARM", 2), testPlayer1.resourceStorage[0]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testCacaofarmLevel2() {
        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",2), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 2), testPlayer1.resourceStorage[1]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testStableLevel2() {
        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",2), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 2), testPlayer1.resourceStorage[2]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testFactoryLevel2() {
        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 2), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testWarehouseLevel2() {
        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testLabLevel2() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[5]); //check buildinglevel

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testSugarfarmLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("SUGARFARM",3), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage("SUGARFARM", 3), testPlayer1.resourceStorage[0]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testCacaofarmLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",3), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 3), testPlayer1.resourceStorage[1]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testStableLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",3), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 3), testPlayer1.resourceStorage[2]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testFactoryLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 3), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testWarehouseLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testLabLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[5]); //check buildinglevel

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testSugarfarmInvalid() {
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("SUGARFARM",1), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage("SUGARFARM", 1), testPlayer1.resourceStorage[0]); //check storage

        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("SUGARFARM",2), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage("SUGARFARM", 2), testPlayer1.resourceStorage[0]); //check storage

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[0]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("SUGARFARM",3), testPlayer1.resourceProduction[0]); //check production
        assertEquals(BuildingStats.getStorage("SUGARFARM", 3), testPlayer1.resourceStorage[0]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testCacaofarmInvalid() {
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",1), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 1), testPlayer1.resourceStorage[1]); //check storage

        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",2), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 2), testPlayer1.resourceStorage[1]); //check storage

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[1]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("CACAOFARM",3), testPlayer1.resourceProduction[1]); //check production
        assertEquals(BuildingStats.getStorage("CACAOFARM", 3), testPlayer1.resourceStorage[1]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testStableInvalid() {
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",1), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 1), testPlayer1.resourceStorage[2]); //check storage

        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",2), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 2), testPlayer1.resourceStorage[2]); //check storage

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[2]); //check buildinglevel
        assertEquals(BuildingStats.getProduction("STABLE",3), testPlayer1.resourceProduction[2]); //check production
        assertEquals(BuildingStats.getStorage("STABLE", 3), testPlayer1.resourceStorage[2]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testFactoryInvalid() {
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 1), testPlayer1.resourceStorage[3]); //check storage

        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 2), testPlayer1.resourceStorage[3]); //check storage

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[3]); //check buildinglevel
        assertEquals(0, testPlayer1.resourceProduction[3]); //check production
        assertEquals(BuildingStats.getStorage("FACTORY", 3), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testWarehouseInvalid() {
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 1), testPlayer1.resourceStorage[3]); //check storage

        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        assertEquals(2, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 2), testPlayer1.resourceStorage[3]); //check storage

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[4]); //check buildinglevel
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[0]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[1]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[2]); //check storage
        assertEquals(BuildingStats.getStorage("WAREHOUSE", 3), testPlayer1.resourceStorage[3]); //check storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testLabInvalid() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        assertEquals(3, testPlayer1.buildingLevel[5]); //check buildinglevel

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other stoarge
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other stoarge

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testAllFarmBuildings() {
        buildSugarfarm(testPlayer1);
        buildStable(testPlayer1);
        buildCacaofarm(testPlayer1);

        assertArrayEquals(new int[]{1,1,1,0,0,0},testPlayer1.buildingLevel);
        assertEquals(BuildingStats.getProduction(sugarfarm, 1), testPlayer1.resourceProduction[0]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1), testPlayer1.resourceProduction[1]);
        assertEquals(BuildingStats.getProduction(stable, 1), testPlayer1.resourceProduction[2]);
    }

    @Test
    public void testTearDownSugarfarmLevel1() {
        buildSugarfarm(testPlayer1);
        tearDownSugarfarm(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceProduction[0]);

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownCacaofarmLevel1() {
        buildCacaofarm(testPlayer1);
        tearDownCacaofarm(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.resourceStorage[1]);
        assertEquals(0, testPlayer1.resourceProduction[1]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownStableLevel1() {
        buildStable(testPlayer1);
        tearDownStable(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.resourceStorage[2]);
        assertEquals(0, testPlayer1.resourceProduction[2]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownFactoryLevel1() {
        buildFactory(testPlayer1);
        tearDownFactory(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[3]);
        assertEquals(0, testPlayer1.resourceStorage[3]);
        assertEquals(0, testPlayer1.resourceProduction[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testTearDownWarehouseLevel1() {
        buildWarehouse(testPlayer1);
        tearDownWarehouse(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[4]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownLabLevel1() {
        buildLab(testPlayer1);
        tearDownLab(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownSugarfarmLevel2() {
        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        tearDownSugarfarm(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[0]);
        assertEquals(BuildingStats.getStorage("SUGARFARM", 1), testPlayer1.resourceStorage[0]);
        assertEquals(BuildingStats.getProduction("SUGARFARM", 1), testPlayer1.resourceProduction[0]);

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownCacaofarmLevel2() {
        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        tearDownCacaofarm(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[1]);
        assertEquals(BuildingStats.getStorage(cacaofarm, 1), testPlayer1.resourceStorage[1]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1), testPlayer1.resourceProduction[1]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownStableLevel2() {
        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        tearDownStable(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[2]);
        assertEquals(BuildingStats.getStorage(stable, 1), testPlayer1.resourceStorage[2]);
        assertEquals(BuildingStats.getProduction(stable, 1), testPlayer1.resourceProduction[2]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownFactoryLevel2() {
        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        tearDownFactory(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[3]);
        assertEquals(BuildingStats.getStorage(factory, 1), testPlayer1.resourceStorage[3]);
        assertEquals(0, testPlayer1.resourceProduction[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testTearDownWarehouseLevel2() {
        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        tearDownWarehouse(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[4]);

        assertEquals(BuildingStats.getStorage(warehouse, 1), testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 1), testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 1), testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 1), testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownLabLevel2() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        tearDownLab(testPlayer1);

        assertEquals(1, testPlayer1.buildingLevel[5]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownSugarfarmLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        buildSugarfarm(testPlayer1);
        tearDownSugarfarm(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[0]);
        assertEquals(BuildingStats.getStorage("SUGARFARM", 2), testPlayer1.resourceStorage[0]);
        assertEquals(BuildingStats.getProduction("SUGARFARM", 2), testPlayer1.resourceProduction[0]);

        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownCacaofarmLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        buildCacaofarm(testPlayer1);
        tearDownCacaofarm(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[1]);
        assertEquals(BuildingStats.getStorage(cacaofarm, 2), testPlayer1.resourceStorage[1]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 2), testPlayer1.resourceProduction[1]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownStableLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        buildStable(testPlayer1);
        tearDownStable(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[2]);
        assertEquals(BuildingStats.getStorage(stable, 2), testPlayer1.resourceStorage[2]);
        assertEquals(BuildingStats.getProduction(stable, 2), testPlayer1.resourceProduction[2]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownFactoryLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        buildFactory(testPlayer1);
        tearDownFactory(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[3]);
        assertEquals(BuildingStats.getStorage(factory, 2), testPlayer1.resourceStorage[3]);
        assertEquals(0, testPlayer1.resourceProduction[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
    }

    @Test
    public void testTearDownWarehouseLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        buildWarehouse(testPlayer1);
        tearDownWarehouse(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[4]);

        assertEquals(BuildingStats.getStorage(warehouse, 2), testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 2), testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 2), testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(BuildingStats.getStorage(warehouse, 2), testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownLabLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        tearDownLab(testPlayer1);

        assertEquals(2, testPlayer1.buildingLevel[5]);

        assertEquals(0, testPlayer1.resourceStorage[0]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testWarehouseStorage() {

        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        //test storage with lvl 1 sugarfarm
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(100, testPlayer1.resourceStorage[0]);

        //test storage with lvl 2 sugarfarm
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(3, testPlayer1.buildingLevel[5]);
        assertEquals(2, testPlayer1.buildingLevel[0]);
        assertEquals(200, testPlayer1.resourceStorage[0]);

        //test storage with lvl 3 sugarfarm
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(3, testPlayer1.buildingLevel[5]);
        assertEquals(3, testPlayer1.buildingLevel[0]);
        assertEquals(400, testPlayer1.resourceStorage[0]);

        //check that other storages are still 0
        for (int i = 1; i < 4; i++) {
            assertEquals(0, testPlayer1.resourceStorage[i]);
        }

        //test warehouse lvl 1 giving all storages
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(900, testPlayer1.resourceStorage[0]);
        for (int i = 1; i < 4; i++) {
            assertEquals(500, testPlayer1.resourceStorage[i]);
        }

        //test warehouse lvl 2 giving all storages
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(2, testPlayer1.buildingLevel[4]);
        assertEquals(1400, testPlayer1.resourceStorage[0]);
        for (int i = 1; i < 4; i++) {
            assertEquals(1000, testPlayer1.resourceStorage[i]);
        }

        //test warehouse lvl 3 giving all storages
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(2400, testPlayer1.resourceStorage[0]);
        for (int i = 1; i < 4; i++) {
            assertEquals(2000, testPlayer1.resourceStorage[i]);
        }
    }

    @Test
    public void testTearDownSugarfarmInvalid() {
        tearDownSugarfarm(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownCacaofarmInvalid() {
        tearDownCacaofarm(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownStableInvalid() {
        tearDownStable(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownFactoryInvalid() {
        tearDownFactory(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownWarehouseInvalid() {
        tearDownWarehouse(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }

    @Test
    public void testTearDownLabInvalid() {
        tearDownLab(testPlayer1);

        assertEquals(0, testPlayer1.buildingLevel[0]);
        assertEquals(0, testPlayer1.buildingLevel[1]);
        assertEquals(0, testPlayer1.buildingLevel[2]);
        assertEquals(0, testPlayer1.buildingLevel[3]);

        assertEquals(0, testPlayer1.resourceStorage[0]);
        assertEquals(0, testPlayer1.resourceStorage[1]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[2]); //check other storage
        assertEquals(0, testPlayer1.resourceStorage[3]); //check other storage

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[2]); //check other production
        assertEquals(0, testPlayer1.resourceProduction[3]); //check other production
    }


    @Test
    public void testBuildingCosts() {
        testPlayer1.capital = 18000;
        //level 1
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(1, testPlayer1.buildingLevel[0]);
        assertEquals(16000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1",});
        assertEquals(1, testPlayer1.buildingLevel[1]);
        assertEquals(14000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "STABLE", "1"});
        assertEquals(1, testPlayer1.buildingLevel[2]);
        assertEquals(12000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "FACTORY", "1",});
        assertEquals(1, testPlayer1.buildingLevel[3]);
        assertEquals(8000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(1, testPlayer1.buildingLevel[3]);
        assertEquals(5000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        assertEquals(1, testPlayer1.buildingLevel[3]);
        assertEquals(0, testPlayer1.capital);

        //level 2
        testPlayer1.capital = 28000;
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(2, testPlayer1.buildingLevel[0]);
        assertEquals(25000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1",});
        assertEquals(2, testPlayer1.buildingLevel[1]);
        assertEquals(22000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "STABLE", "1"});
        assertEquals(2, testPlayer1.buildingLevel[2]);
        assertEquals(19000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "FACTORY", "1",});
        assertEquals(2, testPlayer1.buildingLevel[3]);
        assertEquals(13000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(2, testPlayer1.buildingLevel[3]);
        assertEquals(8000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        assertEquals(2, testPlayer1.buildingLevel[3]);
        assertEquals(0, testPlayer1.capital);

        //level 3
        testPlayer1.capital = 42000;
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        assertEquals(3, testPlayer1.buildingLevel[0]);
        assertEquals(38000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1",});
        assertEquals(3, testPlayer1.buildingLevel[1]);
        assertEquals(34000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "STABLE", "1"});
        assertEquals(3, testPlayer1.buildingLevel[2]);
        assertEquals(30000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "FACTORY", "1",});
        assertEquals(3, testPlayer1.buildingLevel[3]);
        assertEquals(20000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        assertEquals(3, testPlayer1.buildingLevel[3]);
        assertEquals(12000, testPlayer1.capital);
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        assertEquals(3, testPlayer1.buildingLevel[3]);
        assertEquals(0, testPlayer1.capital);
    }

    @Test
    public void testChocoProductionAllLevel1() {
        buildAllProductionBuildings(testPlayer1);

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);
    }

    @Test
    public void testChocoProductionAllLevel2() {
        buildLab(testPlayer1);
        buildAllProductionBuildings(testPlayer1);
        buildAllProductionBuildings(testPlayer1);
        assertArrayEquals(new int[]{2,2,2,2,0,1}, testPlayer1.buildingLevel);

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 2) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);
    }

    @Test
    public void testChocoProductionAllLevel3() {
        buildLab(testPlayer1);
        buildLab(testPlayer1);
        buildAllProductionBuildings(testPlayer1);
        buildAllProductionBuildings(testPlayer1);
        buildAllProductionBuildings(testPlayer1);
        assertArrayEquals(new int[]{3,3,3,3,0,2}, testPlayer1.buildingLevel);

        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 3) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);
    }

    @Test
    public void testChocoProductionWithUnevenFarms() {
        buildFactory(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertArrayEquals(new int[]{1,0,0,1,0,0,}, testPlayer1.buildingLevel);
        assertEquals(BuildingStats.getProduction(sugarfarm, testPlayer1.buildingLevel[0]),
                testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);
        assertEquals(0, testPlayer1.resourceProduction[3]);

        buildCacaofarm(testPlayer1);
        assertEquals(BuildingStats.getProduction(sugarfarm, 1), testPlayer1.resourceProduction[0]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1), testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);
        assertEquals(0, testPlayer1.resourceProduction[3]);

        buildStable(testPlayer1);
        assertEquals(0, testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0,testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);

        buildLab(testPlayer1);
        buildSugarfarm(testPlayer1);
        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                - (2L * testPlayer1.resourceProduction[3]) / chocoMultiplier,
                testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0,testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);

        buildCacaofarm(testPlayer1);
        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                        - (2L * testPlayer1.resourceProduction[3]) / chocoMultiplier,
                testPlayer1.resourceProduction[0]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 2)
                        - testPlayer1.resourceProduction[3] / chocoMultiplier,
                testPlayer1.resourceProduction[1]);
        assertEquals(0,testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);

        tearDownCacaofarm(testPlayer1);
        buildStable(testPlayer1);
        assertArrayEquals(new int[]{2,1,2,1,0,1}, testPlayer1.buildingLevel);
        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                        - (2L * testPlayer1.resourceProduction[3]) / chocoMultiplier,
                testPlayer1.resourceProduction[0]);
        assertEquals(0,testPlayer1.resourceProduction[1]);
        assertEquals(BuildingStats.getProduction(stable, 2)
                        - testPlayer1.resourceProduction[3] / chocoMultiplier,
                testPlayer1.resourceProduction[2]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 1) * chocoMultiplier,
                testPlayer1.resourceProduction[3]);
    }

    @Test
    public void testChocoProduction222101() {
        buildAll(testPlayer1);
        tearDownWarehouse(testPlayer1);
        buildSugarfarm(testPlayer1);

        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                        - BuildingStats.getProduction(sugarfarm, 1),
                testPlayer1.resourceProduction[0]);
        assertEquals(0, testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);

        buildCacaofarm(testPlayer1);
        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                        - BuildingStats.getProduction(sugarfarm, 1),
                testPlayer1.resourceProduction[0]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 2)
                        - BuildingStats.getProduction(cacaofarm, 1),
                testPlayer1.resourceProduction[1]);
        assertEquals(0, testPlayer1.resourceProduction[2]);


        buildStable(testPlayer1);
        System.out.println("222101");

        assertArrayEquals(new int[]{2,2,2,1,0,1}, testPlayer1.buildingLevel);
        assertEquals(BuildingStats.getProduction(sugarfarm, 2)
                - 2L * BuildingStats.getProduction(factory, 1) / chocoMultiplier,
                testPlayer1.resourceProduction[0]);
        assertEquals(BuildingStats.getProduction(cacaofarm, 2)
                        - BuildingStats.getProduction(factory, 1) / chocoMultiplier,
                testPlayer1.resourceProduction[1]);
        assertEquals(BuildingStats.getProduction(stable, 2)
                        - BuildingStats.getProduction(factory, 1) / chocoMultiplier,
                testPlayer1.resourceProduction[2]);
    }

    @Test
    public void testTestMethods() {
        buildSugarfarm(testPlayer1);
        assertEquals(1, testPlayer1.buildingLevel[0]);
    }

    @Test
    public void testResourceUpdate() {
        String sugarProd1 = String.valueOf(BuildingStats.getProduction("SUGARFARM", 1));
        String cacaoProd1 = String.valueOf(BuildingStats.getProduction("CACAOFARM", 1));
        String milkProd1 = String.valueOf(BuildingStats.getProduction("STABLE", 1));
        String chocoProd1 = String.valueOf(BuildingStats.getProduction("FACTORY", 1));
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1",});
        testPlayer1.build(new String[]{"BUILD", "STABLE", "1",});

        assertArrayEquals(new int[]{50,25,25,0}, testPlayer1.resourceProduction);
        assertArrayEquals(new int[]{0,0,0,0}, testPlayer1.resourceAmount);
        testPlayer1.updateResources();
        assertArrayEquals(new int[]{50,25,25,0}, testPlayer1.resourceAmount);
        String arg2 = testPlayer1.capital + ",0";
        String arg3 = sugarProd1 + ",0,0";
        String arg4 = cacaoProd1 + ",0,0";
        String arg5 = milkProd1 + ",0,0";
        String arg6 = String.valueOf(Integer.parseInt(cacaoProd1) * chocoMultiplier) + ",0,0";

        assertArrayEquals(new String[]{"PRDUP", "Month", arg2, arg3, arg4, arg5, "0,0,0"},
                testPlayer1.getProductionUpdate("Month"));

        testPlayer1.build(new String[]{"BUILD", "FACTORY", "1",});
        assertArrayEquals(new int[]{0,0,0,50}, testPlayer1.resourceProduction);
        assertArrayEquals(new int[]{50,25,25,0}, testPlayer1.resourceAmount);
        testPlayer1.updateResources();
        assertArrayEquals(new int[]{50,25,25,50}, testPlayer1.resourceAmount);
        arg2 = testPlayer1.capital + ",0";
        assertArrayEquals(new String[]{"PRDUP", "Month", arg2, arg3, arg4, arg5, arg6},
                testPlayer1.getProductionUpdate("Month"));
    }

    @Test
    public void testBooster() {
        //test Fertilizer
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer1.build(new String[]{"BUILD", "LAB", "1",});
        testPlayer1.build(new String[]{"BUILD", "SUGARFARM", "1",});
        testPlayer1.build(new String[]{"BUILD", "CACAOFARM", "1",});
        testPlayer1.build(new String[]{"BUILD", "STABLE", "1",});

        testPlayer1.capital = 20000;
        Booster booster = Booster.FERTILIZER1;
        int exceptedCapital = testPlayer1.capital - booster.costs;
        int exceptedSugarProduction = (int) (testPlayer1.resourceProduction[0] * booster.multiplicator);
        int exceptedCacaoProduction = (int) (testPlayer1.resourceProduction[1] * booster.multiplicator);
        int exceptedMilkProduction  = (int) (testPlayer1.resourceProduction[2] * booster.multiplicator);
        testPlayer1.getBoost(new String[]{"BOOST", "FERTILIZER1"});
        assertEquals(exceptedCapital, testPlayer1.capital);
        assertArrayEquals(new int[]{exceptedSugarProduction, exceptedCacaoProduction, exceptedMilkProduction, 0},
                testPlayer1.resourceProduction);


    }

    @Test
    public void testSpy() {
        //ensure that spy cannot be bought because of lab not high enough
        assertFalse(testPlayer1.hasSpy());
        assertEquals(0, testPlayer1.buildingLevel[5]);
        testPlayer1.getBoost(new String[]{"BOOST","SPY"});
        assertFalse(testPlayer1.hasSpy());

        //build lab and check that spy can be bought
        buildLab(testPlayer1);
        testPlayer1.getBoost(new String[]{"BOOST","SPY"});
        assertTrue(testPlayer1.hasSpy());


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

    private void buildAllProductionBuildings(Player player) {
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
    }

    private void buildAll(Player player) {
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        player.build(new String[]{"BUILD", "LAB", "1",});
    }

    private void buildAllLevel2(Player player) {
        player.build(new String[]{"BUILD", "LAB", "1",});
        player.build(new String[]{"BUILD", "LAB", "1",});
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
    }

    private void buildAllLevel3(Player player) {
        player.build(new String[]{"BUILD", "LAB", "1",});
        player.build(new String[]{"BUILD", "LAB", "1",});
        player.build(new String[]{"BUILD", "LAB", "1",});
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "SUGARFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "CACAOFARM", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "STABLE", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "FACTORY", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
        player.build(new String[]{"BUILD", "WAREHOUSE", "1",});
    }

    private void tearDownSugarfarm(Player player) {
        player.build(new String[]{"BUILD", "SUGARFARM", "-1",});
    }
    private void tearDownCacaofarm(Player player) {
        player.build(new String[]{"BUILD", "CACAOFARM", "-1",});
    }
    private void tearDownStable(Player player) {
        player.build(new String[]{"BUILD", "STABLE", "-1",});
    }
    private void tearDownFactory(Player player) {
        player.build(new String[]{"BUILD", "FACTORY", "-1",});
    }
    private void tearDownWarehouse(Player player) {
        player.build(new String[]{"BUILD", "WAREHOUSE", "-1",});
    }
    private void tearDownLab(Player player) {
        player.build(new String[]{"BUILD", "LAB", "-1",});
    }
}