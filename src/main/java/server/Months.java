package server;

import java.util.Random;

public enum Months {
    January,
    February,
    March,
    April,
    May,
    June,
    July,
    August,
    September,
    October,
    November,
    December;


    private final static Months[] monthList = Months.values();

    /**
     * generate random int between 1 and 12 to determine starting month
     * @return a random int between 1 and 12
     */
    public static int getRandomInt() {
        Random random = new Random();
        return random.nextInt(13);
    }

    /**
     * return month by index
     * @param i index
     * @return  month at index i
     */
    public static Months getMonth(int i) { return monthList[i];}
}
