package utils;

import model.Color;
import model.Country;
import model.Person;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility that helps to read the parameters of the person
 */
public class PersonUtil {
    private static final String PERSON_COUNTRIES = Arrays.stream(Country.values()).map(Enum::name)
            .collect(Collectors.joining(", "));

    private static final String PERSON_COLOR = Arrays.stream(Color.values()).map(Enum::name)
            .collect(Collectors.joining(", "));

    public static class PersonBuilder {
        String name;
        Integer weight;
        Color eyeColor;
        Color hairColor;
        Country nationality;
    }

    /**
     * creates Person
     * @param scan provides params of a Person
     * @return crated Person
     */
    public static Person createPerson(Scanner scan) {
        PersonBuilder pb = readPerson(scan);
        if (pb == null) {
            return null;
        }
        return new Person(pb.name,
                pb.weight,
                pb.eyeColor,
                pb.hairColor,
                pb.nationality);
    }

    public static Person updatePerson(Scanner scan, Person person) {
        PersonBuilder pb = readPerson(scan);
        if (pb == null) {
            UniqueValuesUtil.removePerson(person);
            return null;
        }
        UniqueValuesUtil.updatePerson(person, pb.name);
        person.update(pb.name,
                pb.weight,
                pb.eyeColor,
                pb.hairColor,
                pb.nationality);
        return person;
    }

    public static PersonBuilder readPerson(Scanner scan) {
        if(!aimValidation(scan)) {
            return null;
        }
        final PersonBuilder pb = new PersonBuilder();
        Runnable[] paramFillingRunnable = {() -> pb.name = getName(scan),
                () -> pb.weight = getWeight(scan),
                () -> pb.eyeColor = getEyeColor(scan),
                () -> pb.hairColor = getHairColor(scan),
                () -> pb.nationality = getNationality(scan)};

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
        return pb;
    }

    /**
     * makes sure the person is needed (it nullable)
     * @param scan provides answer
     * @return true if person is needed
     */
    private static boolean aimValidation(Scanner scan) {
        System.out.println("Add a person [Y/N]: ");
        String answer = scan.nextLine().trim();

        return answer.equals("Y");
    }

    private static String getName(Scanner scan) {
        System.out.println("Enter a person name: ");
        String name = scan.nextLine().trim();
        validatePersonParams(name != null && !name.isEmpty());

        return name;
    }

    private static Integer getWeight(Scanner scan) {
        System.out.println("Enter a person weight: ");
        Integer weight = Integer.valueOf(scan.nextLine().trim());
        validatePersonParams(weight == null || weight > 0);

        return weight;
    }


    private static Color getEyeColor(Scanner scan) {
        System.out.println("Enter a person eye color: " + PERSON_COLOR);
        String eyeColorStr = scan.nextLine().trim();
        validatePersonParams(!eyeColorStr.equals(null));

        return Color.valueOf(eyeColorStr);
    }

    private static Color getHairColor(Scanner scan) {
        System.out.println("Enter a person hair color: " + PERSON_COLOR);
        String hairColorStr = scan.nextLine().trim();
        validatePersonParams(!hairColorStr.equals(null));

        return Color.valueOf(hairColorStr);
    }

    private static Country getNationality(Scanner scan) {
        System.out.println("Enter a person nationality: " + PERSON_COUNTRIES);
        String nationality = scan.nextLine().trim();

        return nationality.isEmpty() ? null : Country.valueOf(nationality);
    }

    /**
     * throw exception for invalid params
     * @param isValid displays param validity
     * @throws IllegalArgumentException if invalid
     */
    private static void validatePersonParams(boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Illegal argument value for person");
        }
    }
}