package nl.tudelft.fa.core.model.game;

/**
 * Class representing a Grand Prix
 * @version 01 12 2016
 * @author F.C. Slothouber
 */
public class GrandPrix {

    private Circuit circuit;
    private String date;

    /**
     * Constructor
     * @param circuit the race circuit
     * @param date date of the race
     */
    public GrandPrix(Circuit circuit, String date) {
        this.circuit = circuit;
        this.date = date;
    }

    /**
     * Getter circuit
     * @return returns circuit
     */
    public Circuit getCircuit() {
        return circuit;
    }

    /**
     * Setter circuit
     * @param circuit new circuit
     */
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Getter date
     * @return returns date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter date
     * @param date new date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
