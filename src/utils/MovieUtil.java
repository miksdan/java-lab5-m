package utils;

import model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility that helps to read the parameters of the movie
 */
public class MovieUtil {
    public static class MovieBuilder {
        public Integer id;
        public String name;
        public Coordinates coordinates;
        public LocalDate creationDate;
        public Integer oscarsCount;
        public int goldenPalmCount;
        public long length;
        public MpaaRating mpaaRating;
        public Person screenwriter;
    }

    /**
     * Contains Mpaa Rating values name
     */
    private static final String UNITS = Arrays.stream(MpaaRating.values()).map(Enum::name)
            .collect(Collectors.joining(", "));

    /**
     * creates Movie
     * @param scan provides params of a Movie
     * @return crated Movie
     */
    public static Movie createMovie(Scanner scan) {
        MovieBuilder mb = readMovie(scan, null);
        return new Movie(MovieStorage.generateMovieId(),
                mb.name,
                mb.coordinates,
                LocalDate.now(),
                mb.oscarsCount,
                mb.goldenPalmCount,
                mb.length,
                mb.mpaaRating,
                mb.screenwriter);
    }

    /**
     * simplifies movie creation
     * (movie parameters have restrictions)
     */
    private static MovieBuilder readMovie(Scanner scan, Movie movie) {
        final MovieBuilder mb = new MovieBuilder();
        Runnable[] paramFillingRunnable = {() -> mb.name = getName(scan),
                () -> mb.coordinates = getCoordinates(scan),
                () -> mb.oscarsCount = getOscarsCount(scan),
                () -> mb.goldenPalmCount = getGoldenPalmCount(scan),
                () -> mb.length = getLength(scan),
                () -> mb.mpaaRating = getMpaaRating(scan),
                () -> mb.screenwriter = getScreenwriter(scan, movie)};

        for (Runnable runnable : paramFillingRunnable) {
            int counter = 0;
            int maxError = 5;
            while (true) {
                try {
                    runnable.run();
                    break;
                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println(e.getClass().getName() + ": " + e.getMessage());
                    System.out.println("Not valid parameter." + " Attempts left: " + (maxError - counter));
                    counter++;
                    if (!((maxError - counter) >= 0)) {
                        System.out.println("ooops... something went wrong");
                        System.exit(0);
                    }
                }
            }
        }
        return mb;
    }

    /**
     *
     * @param scan
     * @return name
     */
    private static String getName(Scanner scan) {
        System.out.println("Enter a movie name");
        System.out.println("The field cannot be empty.");
        String name = scan.nextLine().trim();
        validateMovieParams(name != null && !name.isEmpty());

        return name;
    }

    /**
     *
     * @param scan
     * @return coordinates
     */
    private static Coordinates getCoordinates(Scanner scan) {
        System.out.println("Enter a movie x coordinates:");
        System.out.println("The field must be more than -162");
        Integer x = Integer.valueOf(scan.nextLine().trim());
        System.out.println("Enter a movie y coordinates:");
        System.out.println("The field must be no more than 232");
        Long y = Long.valueOf(scan.nextLine().trim());

        return new Coordinates(x, y);
    }

    /**
     *
     * @param scan
     * @return oscars count
     */
    private static Integer getOscarsCount(Scanner scan) {
        System.out.println("Enter a movie oscars count ");
        System.out.println("The field must be more than 0");
        Integer oscarsCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(oscarsCount > 0);

        return oscarsCount;
    }

    /**
     *
     * @param scan
     * @return golden palm count
     */
    private static int getGoldenPalmCount(Scanner scan) {
        System.out.println("Enter a movie golden palm count ");
        System.out.println("The field must be more than 0");
        Integer goldenPalmCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(goldenPalmCount > 0);

        return goldenPalmCount;
    }

    /**
     *
     * @param scan
     * @return movie length
     */
    private static long getLength(Scanner scan) {
        System.out.println("Enter a movie length ");
        System.out.println("The field must be more than 0");
        Long length = Long.valueOf(scan.nextLine().trim());
        validateMovieParams(length > 0);

        return length;
    }

    /**
     *
     * @param scan
     * @return movie mpaa rating
     */
    private static MpaaRating getMpaaRating(Scanner scan) {
        System.out.println("Enter a movie MpaaRating(" + UNITS + ", null):");
        System.out.println("The field can be empty. To enter NULL, use an empty line and Enter");
        String mpaaRating = scan.nextLine().trim();
        validateMovieParams(true);

        return mpaaRating.isEmpty() ? null : MpaaRating.valueOf(mpaaRating);
    }

    /**
     *
     * @param scan
     * @param movie
     * @return screenwriter
     */
    private static Person getScreenwriter(Scanner scan, Movie movie) {
        System.out.println("Movie screenwriter");
        System.out.println("The field can be empty. To enter NULL, use an empty line and Enter");

        return movie != null ? PersonUtil.updatePerson(scan, movie.getScreenwriter())
                : PersonUtil.createPerson(scan);
    }

    /**
     * throw exception for invalid params
     * @param isValid displays param validity
     * @throws IllegalArgumentException if invalid
     */
    private static void validateMovieParams(boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Illegal argument value for movie");
//            System.out.println("Illegal argument value for movie");
        }
    }
}