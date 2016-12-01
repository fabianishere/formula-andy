package nl.tudelft.fa.core.model.game;

/**
 * Class representing a race circuit
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class Circuit {

    private String name;
    private String country;
    private int laps;

    /**
     * Constructor
     * @param name name of circuit
     * @param country location of circuit
     * @param laps amount of laps each race
     */
    public Circuit(String name, String country, int laps) {
        this.name = name;
        this.country = country;
        this.laps = laps;
    }

    /**
     * Getter name
     * @return return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter country
     * @return retuns country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter country
     * @param country new country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter laps
     * @return returns amount of laps
     */
    public int getLaps() {
        return laps;
    }

    /**
     * Setter laps
     * @param laps new amount of laps
     */
    public void setLaps(int laps) {
        this.laps = laps;
    }
}
