import utils.CommandExecutor;
import utils.FileAccessor;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Contain main method, start the application
 */
public class Main {

    /**
     * @param args filename for input and output (same file)
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            throw new IllegalArgumentException("File name was not specified");
        }
        FileAccessor.init(args[0]);
        FileAccessor.readFromXmlFile();
        try (Scanner scan = new Scanner(System.in)) {
            CommandExecutor.startExecution(scan);
        }
    }
}