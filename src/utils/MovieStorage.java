package utils;

import model.Movie;
import model.MpaaRating;

import java.time.LocalDate;
import java.util.*;

/**
 * Manipulate with stored movies
 */
public class MovieStorage {
    private static final PriorityQueue<Movie> STORAGE = new PriorityQueue<>();
    private static final Date initDate = new Date();
    private static int currentId = 0;

    public static Date getInitDate() {
        return initDate;
    }

    public static void add(Movie movie) {
        movie.setId(generateMovieId());
        STORAGE.add(movie);
        if (movie.getId() > currentId) {
            currentId = movie.getId();
        }
    }

    public static int generateMovieId() {
        return ++currentId;
    }

    public static void clear() {
        STORAGE.clear();
    }

    public static Iterator<Movie> getIterator() {
        return STORAGE.iterator();
    }

    public static int size() {
        return STORAGE.size();
    }

    public static void update(int id, Movie movie) {
        Iterator<Movie> iterator = STORAGE.iterator();
        while (iterator.hasNext()) {
            Movie curMovie = iterator.next();
            if (curMovie.getId() == id) {
                curMovie.update(
                        movie.getName(),
                        movie.getCoordinates(),
                        movie.getOscarsCount(),
                        movie.getGoldenPalmCount(),
                        movie.getLength(),
                        movie.getMpaaRating(),
                        movie.getScreenwriter()
                );
            }
        }
    }

    public static void removeById(int id) {
        Iterator<Movie> iterator = STORAGE.iterator();
        while (iterator.hasNext()) {
            Movie curMovie = iterator.next();
            if (curMovie.getId() == id) {
                STORAGE.remove(curMovie);
                return;
            }
        }
    }

    public static void removeGreater(Movie movie) {
        Iterator<Movie> iterator = STORAGE.iterator();
        List<Movie> moviesToRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Movie curMovie = iterator.next();
            if (movie.compareTo(curMovie) < 0) {
                moviesToRemove.add(curMovie);
            }
        }
        moviesToRemove.forEach(STORAGE::remove);
    }

    public static void removeLower(Movie movie) {
        Iterator<Movie> iterator = STORAGE.iterator();
        List<Movie> moviesToRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Movie curMovie = iterator.next();
            if (movie.compareTo(curMovie) > 0) {
                moviesToRemove.add(curMovie);
            }
        }
        moviesToRemove.forEach(STORAGE::remove);
    }

    public static Movie getMaxCreationDate() {
        LocalDate max = LocalDate.MIN;
        Movie movieMaxDate = null;
        for (Movie movie : STORAGE) {
            if (movie.getCreationDate().isAfter(max)) {
                max = movie.getCreationDate();
                movieMaxDate = movie;
            }
        }
        if (movieMaxDate != null) {
            return movieMaxDate;
        } else {
            throw new RuntimeException("No such element in collection");
        }
    }

    public static int countByMpaaRating(MpaaRating rating) {
        int count = 0;
        for (Movie movie : STORAGE) {
            if (movie.getMpaaRating().equals(rating)) {
                count++;
            }
        }
        return count;
    }

    public static List<Movie> filterByMpaaRating(MpaaRating rating) {
        List<Movie> movies = new ArrayList<>();
        for (Movie movie : STORAGE) {
            if (movie.getMpaaRating().equals(rating)) {
                movies.add(movie);
            }
        }
        return movies;
    }

    public static List<Movie> getStorageAsList() {
        return new ArrayList<>(STORAGE);
    }

    public static List<Movie> getSortedListByOscarCount() {
        List<Movie> movies = getStorageAsList();
        movies.sort(Movie::compareTo);
        return movies;
    }
}