package utils;

import model.Movie;
import model.MpaaRating;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Execute commands from console or from file
 */
public class CommandExecutor {

    /**
     *defines the mapping of command names to its calls
     */
    private static final Map<String, BiConsumer<String, Scanner>> COMMAND_FUNCTION_MAP;
    private static final String HELP_INFO =
                    "help : вывести справку по доступным командам\n" +
                    "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                    "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                    "add {element} : добавить новый элемент в коллекцию\n" +
                    "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                    "remove_by_id id : удалить элемент из коллекции по его id\n" +
                    "clear : очистить коллекцию\n" +
                    "save : сохранить коллекцию в файл\n" +
                    "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                    "exit : завершить программу (без сохранения в файл)\n" +
                    "head : вывести первый элемент коллекции\n" +
                    "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                    "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                    "max_by_creation_date : вывести любой объект из коллекции, значение поля creationDate которого является максимальным\n" +
                    "count_by_mpaa_rating mpaaRating : вывести количество элементов, значение поля mpaaRating которых равно заданному\n" +
                    "filter_by_mpaa_rating mpaaRating : вывести элементы, значение поля mpaaRating которых равно заданному\n";
    static {
        Map<String, BiConsumer<String, Scanner>> cfmTemp = new HashMap();
        cfmTemp.put("help", CommandExecutor::help);
        cfmTemp.put("info", CommandExecutor::info);
        cfmTemp.put("show", CommandExecutor::show);
        cfmTemp.put("add", CommandExecutor::add);
        cfmTemp.put("update", CommandExecutor::update);
        cfmTemp.put("remove_by_id", CommandExecutor::removeById);
        cfmTemp.put("clear", CommandExecutor::clear);
        cfmTemp.put("save", CommandExecutor::save);
        cfmTemp.put("execute_script", CommandExecutor::executeScript);
        cfmTemp.put("exit", CommandExecutor::exit);
        cfmTemp.put("head", CommandExecutor::head);
        cfmTemp.put("remove_greater", CommandExecutor::removeGreater);
        cfmTemp.put("remove_lower", CommandExecutor::removeLower);
        cfmTemp.put("max_by_creation_date", CommandExecutor::maxByCreationDate);
        cfmTemp.put("count_by_mpaa_rating", CommandExecutor::countByMpaaRating);
        cfmTemp.put("filter_by_mpaa_rating", CommandExecutor::filterByMpaaRating);
        COMMAND_FUNCTION_MAP = Collections.unmodifiableMap(cfmTemp);
    }

    private static boolean isExitCommand = false;

    /**
     * Starts an endless loop of receiving commands from the console
     * @param scan console scanner
     */
    public static void startExecution(Scanner scan) {
        do {
            if (scan.hasNext()) {
                executeCommand(scan);
            } else {
                System.out.println("The program terminated.");
                System.exit(0);
            }
        } while (scan.hasNext());
    }

    /**
     * execute script from file
     * @param scan script file scanner
     */
    public static void executeScriptCommands(Scanner scan) {
        while (scan.hasNext()) {
            executeCommand(scan);
        }
    }

    /**
     * execute script from file
     * @param scan script file scanner
     */
    private static void executeScript(String params, Scanner scan) {
        if (UniqueValuesUtil.isScriptAlreadyRunning(params)) {
            System.out.println();
            throw new IllegalArgumentException("Infinite loop detected, command 'execute_script " + params + "' skipped" + "\n");
        }
        FileAccessor.readScript(params);
    }

    /**
     * execute one command from scanner
     * @param scan script file scanner
     * @throws IllegalArgumentException for invalid command name
     */
    private static void executeCommand(Scanner scan) {

            String[] currentCommand = (scan.nextLine().trim() + " ").split(" ", 2);
            try {
                Optional.ofNullable(COMMAND_FUNCTION_MAP.get(currentCommand[0]))
                        .orElseThrow(() -> new IllegalArgumentException("Invalid command, type \"help\" for information about commands"))
                        .accept(currentCommand[1].trim(), scan);
            } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("The error of the command" + ": " + e.getMessage());
            }
    }

    /**
     * checks if additional parameters were on the same line with the command
     * @param params should be empty
     * @throws IllegalArgumentException for invalid command name
     */
    private static void isAdditionalParamsEmpty(String params) {
        if(!params.isEmpty()) {
            throw new IllegalArgumentException("This command doesn't need parameters");
//            System.out.println("This command doesn't need parameters");
        }
    }
    /**
     * help command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
   private static void help(String params, Scanner scan) {
       isAdditionalParamsEmpty(params);
       System.out.println(HELP_INFO);
    }

    /**
     * info command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void info(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.out.println("PriorityQueue\nDate: " + MovieStorage.getInitDate() + "\nCount of elements: " + MovieStorage.size());
    }

    /**
     * show command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void show(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        StringBuilder sb = new StringBuilder();
        List<Movie> movies = MovieStorage.getSortedListByOscarCount();
        for (Movie movie : movies) {
            String writer = movie.getScreenwriter() != null ? movie.getScreenwriter().toString() : "";
            sb.append(movie).append("\n").append(writer).append("\n");
        }
        System.out.println(sb);
    }

    /**
     * add command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void add(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        MovieStorage.add(MovieUtil.createMovie(scan));
    }

    /**
     * update command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void update(String params, Scanner scan) {
        int id = Integer.parseInt(params);
        Movie movie = MovieUtil.createMovie(scan);
        MovieStorage.update(id, movie);
    }

    /**
     * remove_by_id command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void removeById(String params, Scanner scan) {
        MovieStorage.removeById(Integer.parseInt(params));
    }

    /**
     * clear command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void clear(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        MovieStorage.clear();
    }

    /**
     * save command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void save(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        FileAccessor.writeXmlFile();
    }

    /**
     * exit command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void exit(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.exit(0);
    }

    /**
     * head command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void head(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.out.println(MovieStorage.getIterator().next().toString());
    }

    /**
     * remove_greater command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void removeGreater(String params, Scanner scan) {
        MovieStorage.removeGreater(MovieUtil.createMovie(scan));
    }

    /**
     * remove_lower command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void removeLower(String params, Scanner scan) {
        MovieStorage.removeLower(MovieUtil.createMovie(scan));
    }

    /**
     * max_by_creationDate command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void maxByCreationDate(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.out.println(MovieStorage.getMaxCreationDate());
    }

    /**
     * count_by_mpaa_rating command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void countByMpaaRating(String params, Scanner scan) {
        System.out.println(MovieStorage.countByMpaaRating(MpaaRating.valueOf(params)));
    }

    /**
     * filter_by_mpaa_rating command
     * @param params command additional params (id, filename etc.)
     * @param scan helps to get the params of an object (for example for 'add' command)
     */
    private static void filterByMpaaRating(String params, Scanner scan) {
        List<Movie> list = MovieStorage.filterByMpaaRating(MpaaRating.valueOf(params));
        for (Movie movie : list) {
            System.out.println(movie);
        }
    }
}
