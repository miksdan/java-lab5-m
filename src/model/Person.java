package model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Person description
 */
public class Person {
    private String name;        //Поле не может быть null, Строка не может быть пустой
    private Integer weight;     //Поле может быть null, Значение поля должно быть больше 0
    private Color eyeColor;     //Поле не может быть null
    private Color hairColor;    //Поле не может быть null
    private Country nationality;//Поле может быть null

    public Person(String name,
                  Integer weight,
                  Color eyeColor,
                  Color hairColor,
                  Country nationality) {
        checkState(name, weight, eyeColor, hairColor);
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Screenwriter info " + "\n" +
                "name: " + name  +
                ", weight: " + weight +
                ", eyeColor: " + eyeColor +
                ", hairColor: " + hairColor +
                ", nationality: " + nationality + "\n" + "\n";
    }

    /**
     * Write xml representation of the object.
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public void convertPersonToXML(XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement("name");
        xsw.writeCharacters(name);
        xsw.writeEndElement();

        if(nationality != null) {
            xsw.writeStartElement("weight");
            xsw.writeCharacters(String.valueOf(weight));
            xsw.writeEndElement();
        }

        xsw.writeStartElement("eyeColor");
        xsw.writeCharacters(eyeColor.name());
        xsw.writeEndElement();

        xsw.writeStartElement("hairColor");
        xsw.writeCharacters(hairColor.name());
        xsw.writeEndElement();

        if(nationality != null) {
            xsw.writeStartElement("nationality");
            xsw.writeCharacters(nationality.name());
            xsw.writeEndElement();
        }
    }

    /**
     * @return screenwriter name
     */
    public String getName() {
        return name;
    }

    /**
     * Validate params before state updating
     * @param name
     * @param weight
     * @param eyeColor
     * @param hairColor
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(String name,
                            Integer weight,
                            Color eyeColor,
                            Color hairColor) {
        if (name != null
                && !(name.isEmpty())
                && weight > 0
                && eyeColor != null
                && hairColor != null) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for person");
    }

    /**
     * Update state
     */
    public void update(String name, Integer weight, Color eyeColor, Color hairColor, Country nationality) {
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }
}
