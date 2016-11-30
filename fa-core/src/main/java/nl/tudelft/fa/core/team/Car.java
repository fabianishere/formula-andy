package nl.tudelft.fa.core.team;

/**
 * A class representing a car
 * @version 30 11 2016
 * @author  F.C. Slothouber
 */
public class Car {

    private String skin;

    /**
     * Constructor
     * @param skin
     */
    public Car (String skin) {
        this.skin = skin;
    }

    /**
     * Getter of skin
     * @return returns the String skin
     */
    public String getSkin() {
        return skin;
    }

    /**
     * Setter of skin
     * @param skin the new String skin
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }
}
