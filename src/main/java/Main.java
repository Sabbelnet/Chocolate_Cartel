import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    /**
     * This is the entry Point for the jar. Console arguments are parsed here
     *
     * Start from console with "java -jar example-project_Alhpa_0.3.jar  (client 'hostadress':'port' ['username'] | server 'port')
     * @param args Input Arguments from console
     */
    public static void main(String[] args) {


        if (args.length == 2 || args.length == 3) {
            if (args[0].equals("client")) {

                ArrayList<String> parsedArgsArrayList = new ArrayList<>();
                String[] split = args[1].split(":");
                parsedArgsArrayList.add(split[0]);
                parsedArgsArrayList.add(split[1]);
                if (args.length == 3) {
                    parsedArgsArrayList.add(args[2]);
                } else {
                    parsedArgsArrayList.add(System.getProperty("user.name"));
                }
                String[] parsedArgs = parsedArgsArrayList.toArray(new String[0]);
                System.out.println(Arrays.toString(parsedArgs));
                client.Main.main(parsedArgs);
            } else if (args[0].equals("server")) {
                int port = Integer.parseInt(args[1]);
                server.Start.start(port);

            } else {
                System.out.println("Invalid command");
            }
        } else {
            System.out.println("Invalid amount of arguments");
        }
    }
}
