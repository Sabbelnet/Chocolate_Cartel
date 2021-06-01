package server;

public enum Resources {

    SUGAR,
    CACAO,
    MILK,
    CHOCO;

    private final static Resources[] resourceList = Resources.values();


    /**
     * get Resource resource to according index
     * @param i resource index
     * @return Resource object
     */
    public static Resources getResource(int i) { return resourceList[i];}

    /**
     * get index of a resource from string
     * @param resource string of resource
     * @return index matching the input resource string
     */
    public static int getIndex(String resource) {
        switch (resource) {
            case "SUGAR":
                return 0;
            case "CACAO":
                return 1;
            case "MILK":
                return 2;
            case "CHOCO":
                return 3;
            default:
                return -1;
        }
    }
}