package wastewise.listing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Listing microservice application.
 */
@SpringBootApplication
public class Listing {
    /**
     * Main Application for Listing Microservice.
     *
     * @param args Startup args.
     * @throws IOException Exception.
     */
    public static void main(String[] args) throws IOException {
        // Ask user for input
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(inputStreamReader)) {
            System.out.println("Is this the first time you run the program? (yes/no)");
            String userInput = reader.readLine().trim().toLowerCase(Locale.US);
            SpringApplication.run(Listing.class, args);
            startMongoDB(userInput);
        }
    }

    /**
     * Starts Mongo Server up.
     *
     * @param userInput User input about first time (yes/no).
     */
    public static void startMongoDB(String userInput) {
        boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows");
        try {
            String scriptPath = "";
            if (userInput.equalsIgnoreCase("yes")) {
                // Get the script path for starting servers
                scriptPath += Paths
                        .get(System.getProperty("user.dir"), "listing-microservice", "data", "server_scripts",
                                "set-up-listing-servers.sh")
                        .toString();
                System.out.println("path is: " + scriptPath);
            } else {
                // Get the script path for setting up servers
                scriptPath += Paths
                        .get(System.getProperty("user.dir"), "listing-microservice", "data", "server_scripts",
                                "start-listing-servers.sh")
                        .toString();
                System.out.println("path is: " + scriptPath);
            }

            if (isWindows) {
                // script running logic for Windows
                Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start /wait", scriptPath });
                System.out.println("Servers are running for Windows!");
            } else {
                // script running logic for Linux/Mac
                ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", scriptPath);

                // Redirect the output of the shell script to the console
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                // Read and print the output from the script in real-time
                try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                        BufferedReader processOutput = new BufferedReader(inputStreamReader)) {
                    String line = processOutput.readLine();
                    while ((line) != null) {
                        System.out.println(line); // Print each line of output to the console
                        line = processOutput.readLine();
                    }
                }

                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    System.out.println("MongoDB setup complete!");
                } else {
                    System.err.println("Error: The process exited with code " + exitCode);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Error starting MongoDB servers.");
        }
    }
}
