package model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.time.LocalDate;

public class Movie implements Comparable<Movie> {   //implements Comparable<Movie>
    private int id;                                 //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name;                            //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates;                //Поле не может быть null
    private LocalDate creationDate;                 //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer oscarsCount;                    //Значение поля должно быть больше 0, Поле может быть null
    private int goldenPalmCount;                    //Значение поля должно быть больше 0
    private long length;                            //Значение поля должно быть больше 0
    private MpaaRating mpaaRating;                  //Поле может быть null
    private Person screenwriter;                    //Не указано -> Поле может быть null

    public Movie(int id,
                 String name,
                 Coordinates coordinates,
                 LocalDate creationDate,
                 Integer oscarsCount,
                 int goldenPalmCount,
                 long length,
                 MpaaRating mpaaRating,
                 Person screenwriter) {
        checkState(id,
                name,
                coordinates,
                creationDate,
                oscarsCount,
                goldenPalmCount,
                length);
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.mpaaRating = mpaaRating;
        this.screenwriter = screenwriter;
    }

    /**
     * @return movie id
     */
    public int getId() {
        return id;
    }

    /**
     * @return screenwriter
     */
    public Person getScreenwriter() {
        return screenwriter;
    }

    @Override
    public String toString() {
        return "Movie: " +"\n" +
                "id: " + id + "\n" +
                "name: '" + name + '\'' + "\n" +
                "coordinates: " + coordinates + "\n" +
                "creation date: " + creationDate + "\n" +
                "oscars count: " + oscarsCount + "\n" +
                "golden palm count: " + goldenPalmCount + "\n" +
                "length: " + length + "\n" +
                "mpaa rating: " + mpaaRating + "\n";
    }

    /**
     * Write xml representation of the object.
     *
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public void convertMovieToXml(XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement("id");
        xsw.writeCharacters(String.valueOf(id));
        xsw.writeEndElement();

        xsw.writeStartElement("name");
        xsw.writeCharacters(name);
        xsw.writeEndElement();

        xsw.writeStartElement("coordinates");
        coordinates.convertCoordinatesToXml(xsw);
        xsw.writeEndElement();

        xsw.writeStartElement("creationDate");
        xsw.writeCharacters(creationDate.toString());
        xsw.writeEndElement();

        xsw.writeStartElement("oscarsCount");
        xsw.writeCharacters(String.valueOf(oscarsCount));
        xsw.writeEndElement();

        xsw.writeStartElement("goldenPalmCount");
        xsw.writeCharacters(String.valueOf(goldenPalmCount));
        xsw.writeEndElement();

        xsw.writeStartElement("length");
        xsw.writeCharacters(String.valueOf(length));
        xsw.writeEndElement();

        if (mpaaRating != null) {
            xsw.writeStartElement("mpaaRating");
            xsw.writeCharacters(mpaaRating.name());
            xsw.writeEndElement();
        } else {
            xsw.writeStartElement("mpaaRating");
            xsw.writeCharacters("");
            xsw.writeEndElement();
        }

        if (screenwriter != null) {
            xsw.writeStartElement("screenwriter");
            screenwriter.convertPersonToXML(xsw);
            xsw.writeEndElement();
        }
    }

    /**
     * Validate params before state updating
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(int id,
                            String name,
                            Coordinates coordinates,
                            LocalDate creationDate,
                            Integer oscarsCount,
                            int goldenPalmCount,
                            long length) {
        if (id > 0
                && name != null
                && !name.isEmpty()
                && coordinates != null
                && creationDate != null
                && oscarsCount > 0
                && goldenPalmCount > 0
                && length > 0) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for product");
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Update state
     * @throws IllegalArgumentException illegal args value
     */
    public void update(String name, Coordinates coordinates, Integer oscarsCount, int goldenPalmCount, long length, MpaaRating mpaaRating, Person screenwriter) {
        checkState(id,
                name,
                coordinates,
                creationDate,
                oscarsCount,
                goldenPalmCount,
                length);
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.mpaaRating = mpaaRating;
        this.screenwriter = screenwriter;
    }

    /**
     * @return movie name
     */
    public String getName() {
        return name;
    }

    /**
     * @return movie coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return movie creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @return movie oscar count
     */
    public Integer getOscarsCount() {
        return oscarsCount;
    }

    /**
     * @return movie golden palm count
     */
    public int getGoldenPalmCount() {
        return goldenPalmCount;
    }

    /**
     * @return movie length
     */
    public long getLength() {
        return length;
    }

    /**
     * @return movie mpaa rating
     */
    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    @Override
    public int compareTo(Movie o) {
        if (this.oscarsCount > o.getOscarsCount()) {
            return 1;
        } else if (this.oscarsCount.equals(o.getOscarsCount())) {
            return 0;
        }
        return -1;
    }
}