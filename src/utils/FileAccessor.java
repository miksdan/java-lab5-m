package utils;

import model.*;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * implements reading and writing files
 */
public class FileAccessor {

    /**
     * defines the mapping of movie params its parsing
     */
    private static final Map<String, BiConsumer<MovieUtil.MovieBuilder, XMLEventReader>> MOVIE_PARAMS_PARSER;

    /**
     * defines the mapping of movie person its parsing
     */
    private static final Map<String, BiConsumer<PersonUtil.PersonBuilder, XMLEventReader>> PERSON_PARAMS_PARSER;

    static {
        Map<String, BiConsumer<MovieUtil.MovieBuilder, XMLEventReader>> pppTemp = new HashMap<>();

        pppTemp.put("movie", (mb, xmlEventReader) -> {});
        pppTemp.put("id", (mb, xmlEventReader) -> mb.id = Integer.valueOf(xmlToStr(xmlEventReader)));
        pppTemp.put("name", (mb, xmlEventReader) -> mb.name = xmlToStr(xmlEventReader));
        pppTemp.put("coordinates", (mb, xmlEventReader) -> mb.coordinates = parseCoordinates(xmlEventReader));
        pppTemp.put("creationDate", (mb, xmlEventReader) -> mb.creationDate = LocalDate.parse(xmlToStr(xmlEventReader)));
        pppTemp.put("oscarsCount", (mb, xmlEventReader) -> mb.oscarsCount = Integer.valueOf(xmlToStr(xmlEventReader)));
        pppTemp.put("goldenPalmCount", (mb, xmlEventReader) -> mb.goldenPalmCount = Integer.valueOf(xmlToStr(xmlEventReader)));
        pppTemp.put("length", (mb, xmlEventReader) -> mb.length = Long.valueOf(xmlToStr(xmlEventReader)));
        pppTemp.put("mpaaRating", (pb, xmlEventReader) -> {
            final String xmlToStr = xmlToStr(xmlEventReader);
            pb.mpaaRating = xmlToStr != null ? MpaaRating.valueOf(xmlToStr) : null;
        });
        pppTemp.put("screenwriter", (pb, xmlEventReader) -> pb.screenwriter = parsePerson(xmlEventReader));

        MOVIE_PARAMS_PARSER = Collections.unmodifiableMap(pppTemp);

        Map<String, BiConsumer<PersonUtil.PersonBuilder, XMLEventReader>> oppTemp = new HashMap<>();

        oppTemp.put("name", (pb, xmlEventReader) -> pb.name = xmlToStr(xmlEventReader));
        oppTemp.put("weight", (pb, xmlEventReader) -> pb.weight = Integer.valueOf(xmlToStr(xmlEventReader)));
        oppTemp.put("eyeColor", (ob, xmlEventReader) -> ob.eyeColor = Color.valueOf(xmlToStr(xmlEventReader)));
        oppTemp.put("hairColor", (ob, xmlEventReader) -> ob.hairColor = Color.valueOf(xmlToStr(xmlEventReader)));
        oppTemp.put("nationality", (ob, xmlEventReader) -> {
            final String xmlToStr = xmlToStr(xmlEventReader);
            ob.nationality = xmlToStr != null ? Country.valueOf(xmlToStr) : null;
        });

        PERSON_PARAMS_PARSER = Collections.unmodifiableMap(oppTemp);
    }

    private static String xmlFileName;

    /**
     * save xml fileName for reading and writing
     *
     * @param xmlFileName path to xml file
     */
    public static void init(String xmlFileName) {
        FileAccessor.xmlFileName = xmlFileName;
    }

    /**
     * Load movie from xml
     */
    public static void readFromXmlFile() {
        List<Movie> movies = new ArrayList<>();
        try (FileInputStream stream = new FileInputStream(xmlFileName)) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(stream);

            while (xmlEventReader.hasNext()) {
                try {
                    Movie mov = parseMovie(xmlEventReader);
                    if (mov != null) {
                        movies.add(mov);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println(e.getClass().getName() + ": " + e.getMessage());
                    System.out.println("Reading error from XML file, element missed" + ": " + e.getMessage());
                }

            }
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Reading error from XML file" + ": " + e.getMessage());
        }
        movies.forEach(MovieStorage::add);
    }

    /**
     * movie parsing
     * @param xmlEventReader
     * @return movie
     */
    private static Movie parseMovie(XMLEventReader xmlEventReader) {
        MovieUtil.MovieBuilder mb = new MovieUtil.MovieBuilder();
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    String startElementStr = xmlEvent.asStartElement().getName().getLocalPart();

                    Optional.ofNullable(MOVIE_PARAMS_PARSER.get(startElementStr))
                            .ifPresent(cons -> cons.accept(mb, xmlEventReader));

                }
                if (xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("movie")) {
                    if (!UniqueValuesUtil.isMovieIdUnique(mb.id)) {
//                        throw new IllegalArgumentException("Illegal Argument");
                        System.out.println("Illegal argument is found in the movie");
                    }
                    return new Movie(mb.id,
                            mb.name,
                            mb.coordinates,
                            mb.creationDate,
                            mb.oscarsCount,
                            mb.goldenPalmCount,
                            mb.length,
                            mb.mpaaRating,
                            mb.screenwriter);
                }
            }
        } catch (XMLStreamException e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Illegal argument is found in the movie" + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * person parsing
     * @param xmlEventReader
     * @return person
     */
    private static Person parsePerson(XMLEventReader xmlEventReader) {
        PersonUtil.PersonBuilder pb = new PersonUtil.PersonBuilder();
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    String startElementStr = xmlEvent.asStartElement().getName().getLocalPart();
                    Optional.ofNullable(PERSON_PARAMS_PARSER.get(startElementStr)).ifPresent(cons -> cons.accept(pb, xmlEventReader));
                }
                if (xmlEvent.isEndElement()
                        && xmlEvent.asEndElement().getName().getLocalPart().equals("screenwriter")) {
                    if (!UniqueValuesUtil.isPersonNameAvailable(pb.name)) {
//                        throw new IllegalArgumentException("Illegal Argument");
                        System.out.println("Illegal argument is found in the person");
                    }
                    return new Person(pb.name,
                            pb.weight,
                            pb.eyeColor,
                            pb.hairColor,
                            pb.nationality);
                }
            }
        } catch (XMLStreamException e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Illegal argument is found in the person" + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * coordinates parsing
     * @param xmlEventReader
     * @return coordinates
     */
    private static Coordinates parseCoordinates(XMLEventReader xmlEventReader) {
        Integer x = 0;
        Long y = null;
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    String startElementStr = xmlEvent.asStartElement().getName().getLocalPart();
                    if (startElementStr.equals("x")) {
                        x = Integer.valueOf(xmlToStr(xmlEventReader));
                    }
                    if (startElementStr.equals("y")) {
                        y = Long.valueOf(xmlToStr(xmlEventReader));
                    }
                }
                if (xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("coordinates")) {
                    return new Coordinates(x, y);
                }
            }
        } catch (XMLStreamException e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Illegal argument is found in coordinates" + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * convert XML characters to string
     *
     * @param xmlEventReader iterator over XML file
     * @return string representation of XML characters
     */
    private static String xmlToStr(XMLEventReader xmlEventReader) {
        XMLEvent xmlEvent = null;
        try {
            xmlEvent = xmlEventReader.nextEvent();
            return xmlEvent.asCharacters().getData();
        } catch (XMLStreamException e) {
            System.out.println("Cannot parse " + xmlEvent);
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (ClassCastException ignore) {
        }
        return null;
    }

    /**
     * saves a collection of movies from memory to a file(specified in init)
     */
    public static void writeXmlFile() {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xsw;
        try (FileWriter stream = new FileWriter(xmlFileName)) {
            xsw = xof.createXMLStreamWriter(stream);
            xsw.writeStartDocument();
            XMLStreamWriter finalXsw = xsw;
            xsw.writeStartElement("movies");
            List<Movie> movies = MovieStorage.getSortedListByOscarCount();
            for (Movie movie : movies) {
                xsw.writeStartElement("movie");
                movie.convertMovieToXml(finalXsw);
                xsw.writeEndElement();
            }
            xsw.writeEndDocument();
            xsw.writeEndDocument();
            xsw.flush();
            xsw.close();
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("File write error" + ": " + e.getMessage());
        }
    }

    /**
     * starts execution of a script in a file
     *
     * @param scriptFile path to script file
     */
    public static void readScript(String scriptFile) {
        try (FileInputStream stream = new FileInputStream(scriptFile); InputStreamReader reader = new InputStreamReader(stream);
             Scanner scan = new Scanner(reader)) {
            UniqueValuesUtil.addScript(scriptFile);
            CommandExecutor.executeScriptCommands(scan);
            UniqueValuesUtil.removeScript(scriptFile);
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Script reading error" + ": " + e.getMessage());
        }
    }
}
