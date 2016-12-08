package nl.tudelft.fa.core.team.inventory;

/**
 * An enumeration of all types of tires used in a Formula 1 race.
 *
 * @author Fabian Mastenbroek
 */
public enum TireType {
    ULTRA_SOFT("Ultra Soft"),
    SUPER_SOFT("Super Soft"),
    SOFT("Soft"),
    MEDIUM("Medium"),
    HARD("Hard"),
    INTERMEDIATE("Intermediate"),
    WET("Wet");

    /**
     * The name of this type of tire.
     */
    private String name;

    /**
     * Construct a {@link TireType}.
     *
     * @param name The name of this type of tire.
     */
    TireType(String name) {
        this.name = name;
    }

    /**
     * Return the name of this type of tire.
     *
     * @return The name of this type of tire.
     */
    public String getName() {
        return name;
    }

    /**
     * Return a string representation of this tire type.
     *
     * @return A string representation of this tire type.
     */
    @Override
    public String toString() {
        return name;
    }
}
