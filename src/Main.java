import org.w3c.dom.Document;
import utils.CommandExecutor;
import utils.FileAccessor;
import utils.SchemeGenerator;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Contain main method, start the application
 */
public class Main {

    /**
     * @param args filename for input and output (same file)
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("The command line argument was expected.\n");
            System.out.println("The program terminated.");
            System.exit(0);
        }

        boolean isFileValid;
        boolean isFileReadebleWritable;
        String schemaName = "config.xsd";
        String xmlFileName = args[0];

        /*
         xsd file verification unit
         */
        {
            File file = new File(schemaName);

            if (!file.exists()) {
                SchemeGenerator.generateXsdScheme();
            }
            if (!file.canRead()) {
                file.setReadable(true);
            }
        }

        Scanner sc = new Scanner(System.in);
        String userInput = "Y";

        /*
         Interaction logic before starting program
         */
        while (true) {
            isFileValid = false;
            isFileReadebleWritable = false;

            File file = new File(xmlFileName);
            if (!file.exists() || file.isDirectory()) {
                System.out.println("File '" + xmlFileName + "' does not exist.\n");
                System.out.println("Want to try to use another file? \n['Y' to accept / Any symbol for cancellation]");
                if (sc.hasNext()) {
                    userInput = sc.nextLine();
                } else {
                    System.out.println("The program terminated.");
                    System.exit(0);
                }
                if (userInput.equals("Y")) {
                    System.out.println("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                } else {
                    System.out.println("Want to use default file structure? \n['Y' to accept / Any symbol for cancellation]");
                    if (sc.hasNext()) {
                        userInput = sc.nextLine();
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                    if (userInput.equals("Y")) {
                        System.out.println("Enter free file name.");
                        if (sc.hasNext()) {
                            userInput = sc.nextLine();

                            String regex = "^([a-zA-Z0-9])+$";

                            if (!Pattern.matches(regex, userInput)) {
                                System.out.println("\nThe file name is required to contain only letters and (or) numbers.");
                                continue;
                            }
                        } else {
                            System.out.println("The program terminated.");
                            System.exit(0);
                        }
                        File newFile = new File(userInput);
                        try {
                            if (newFile.createNewFile()) {
                                FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write("<?xml version=\"1.0\" ?>\n" +
                                        "<movies>\n</movies>");
                                fileWriter.flush();
                                fileWriter.close();
                                xmlFileName = newFile.getName();
                                isFileValid = true;
                                isFileReadebleWritable = true;
                                System.out.println("File ready to work.\n");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("Enter the name of another file:\n");
                            if (sc.hasNext()) {
                                xmlFileName = sc.nextLine();
                            } else {
                                System.out.println("The program terminated.");
                                System.exit(0);
                            }
                            continue;
                        }
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                }
            } else {
                System.out.println("The file exists.\n");
            }

            if (!file.canRead() && !file.canWrite()) {
                System.out.println("No enought rights of access to file '" + xmlFileName + "'\n");
                try {
                    file.setWritable(true);
                    file.setReadable(true);
                    System.out.println("Rights were added successfully to '" + xmlFileName + "'\n");
                    isFileReadebleWritable = true;
                    continue;
                } catch (Exception e) {
                    System.out.println("Rights were not added successfully to '" + xmlFileName + "'\n");
                    System.out.println("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                }
            } else {
                System.out.println("The file has all rights.\n");
                isFileReadebleWritable = true;

                /*
                 Checking the XML document using the scheme XSD
                 */
                System.out.println("Checking the structure of the document in progress.\n");
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true); //default value false
                    DocumentBuilder builder = factory.newDocumentBuilder();

                    Document doc = builder.parse(new File(xmlFileName));

                    SchemaFactory aSF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema = aSF.newSchema(new File("config.xsd"));
                    Validator validator = schema.newValidator();

                    Source dsource = new DOMSource(doc);
                    validator.validate(dsource);
                    System.out.println("Checking is passed.\n");
                    isFileValid = true;
                    break;
                } catch (Throwable e) {
                    isFileValid = false;
                    System.out.println("'" + xmlFileName + "'" + " structure is damaged!");
                }

                System.out.println("Want to try to use another file? \n['Y' to accept / Any symbol for cancellation]");
                if (sc.hasNext()) {
                    userInput = sc.nextLine();
                } else {
                    System.out.println("The program terminated.");
                    System.exit(0);
                }
                if (userInput.equals("Y")) {
                    System.out.println("Enter the name of another file:\n");
                    if (sc.hasNext()) {
                        xmlFileName = sc.nextLine();
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                    continue;
                } else {
                    System.out.println("Want to use default file structure? \n['Y' to accept / Any symbol for cancellation]");
                    if (sc.hasNext()) {
                        userInput = sc.nextLine();

                        String regex = "^([a-zA-Z0-9])+$";

                        if (!Pattern.matches(regex, userInput)) {
                            System.out.println("\nThe file name should contain only letters and (or) numbers.");
                            continue;
                        }
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                    if (userInput.equals("Y")) {
                        System.out.println("Enter free file name.");
                        if (sc.hasNext()) {
                            userInput = sc.nextLine();
                        } else {
                            System.out.println("The program terminated.");
                            System.exit(0);
                        }
                        File newFile = new File(userInput);
                        try {
                            if (newFile.createNewFile()) {
                                FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write("<?xml version=\"1.0\" ?>\n" +
                                        "<movies>\n</movies>");
                                fileWriter.flush();
                                fileWriter.close();
                                xmlFileName = newFile.getName();
                                isFileValid = true;
                                isFileReadebleWritable = true;
                                System.out.println("File ready to work.\n");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("Enter the name of another file:\n");
                            if (sc.hasNext()) {
                                xmlFileName = sc.nextLine();
                            } else {
                                System.out.println("The program terminated.");
                                System.exit(0);
                            }
                            continue;
                        }
                    } else {
                        System.out.println("The program terminated.");
                        System.exit(0);
                    }
                }
            }
        }

        /*
        The start of the main program
         */
        if (isFileValid && isFileReadebleWritable) {
            FileAccessor.init(xmlFileName);
            FileAccessor.readFromXmlFile();
            try (Scanner scan = new Scanner(System.in)) {
                CommandExecutor.startExecution(scan);
            }
        }
    }
}