package model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * XY coordinates
 */
public class Coordinates {
    private Integer x;  //Значение поля должно быть больше -162, Поле не может быть null
    private Long y;     //Максимальное значение поля: 232, Поле не может быть null

    public Coordinates(Integer x, Long y) {
        checkState(x, y);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x=" + x + ", " +
                "y=" + y;
    }

    /**
     * Write xml representation of the object.
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public void convertCoordinatesToXml(XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement("x");
        xsw.writeCharacters(String.valueOf(x));
        xsw.writeEndElement();

        xsw.writeStartElement("y");
        xsw.writeCharacters(y.toString());
        xsw.writeEndElement();
    }

    /**
     * Validate params before state updating
     * @param y
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(Integer x, Long y) {
        if (x > -162 && x != null && y <= 232 && y != null) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for coordinates");
    }
}
