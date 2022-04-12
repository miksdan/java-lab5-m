package utils;

import model.Coordinates;
import model.Movie;
import model.MpaaRating;
import model.Person;

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
            while (true) {
                try {
                    runnable.run();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        return mb;
    }

    private static String getName(Scanner scan) {
        System.out.println("Enter a movie name");
        String name = scan.nextLine().trim();
        validateMovieParams(name != null && !name.isEmpty());

        return name;
    }

    private static Coordinates getCoordinates(Scanner scan) {
        System.out.println("Enter a movie x coordinates:");
        Integer x = Integer.valueOf(scan.nextLine().trim());
        System.out.println("Enter a movie y coordinates:");
        Long y = Long.valueOf(scan.nextLine().trim());

        return new Coordinates(x, y);
    }

    private static Integer getOscarsCount(Scanner scan) {
        System.out.println("Enter a movie oscars count ");
        Integer oscarsCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(oscarsCount > 0);

        return oscarsCount;
    }

    private static int getGoldenPalmCount(Scanner scan) {
        System.out.println("Enter a movie golden palm count ");
        Integer goldenPalmCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(goldenPalmCount > 0);

        return goldenPalmCount;
    }

    private static long getLength(Scanner scan) {
        System.out.println("Enter a movie length ");
        Long length = Long.valueOf(scan.nextLine().trim());
        validateMovieParams(length > 0);

        return length;
    }

    private static MpaaRating getMpaaRating(Scanner scan) {
        System.out.println("Enter a movie MpaaRating(" + UNITS + "):");
        MpaaRating mpaaRating = MpaaRating.valueOf(scan.nextLine().trim());
        validateMovieParams(true);

        return mpaaRating;
    }

    private static Person getScreenwriter(Scanner scan, Movie movie) {
        System.out.println("Movie screenwriter");

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
        }
    }
}