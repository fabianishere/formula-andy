package nl.tudelft.fa.core.model.team;

/**
 * Class representing a car engine
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public class Engine {

    private String brand;
    private double power;
    private double driveAbility;
    private double fuelConsumption;

    /**
     * Constructor
     * @param brand brand name
     * @param power numeric representation of power
     * @param driveAbility numeric representation of driveability
     * @param fuelConsumption numeric representation of fuel consumption
     */
    public Engine(String brand, double power, double driveAbility, double fuelConsumption) {
        this.brand = brand;
        this.power = power;
        this.driveAbility = driveAbility;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Getter of brand
     * @return returns String brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Setter of brand
     * @param brand new brand name
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Getter of power
     * @return returns double power
     */
    public double getPower() {
        return power;
    }

    /**
     * Setter of  power
     * @param power new power setting
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Getter of driveability
     * @return return double driveability
     */
    public double getDriveAbility() {
        return driveAbility;
    }

    /**
     * Setter of driveabilty
     * @param driveAbility new driveability setting
     */
    public void setDriveAbility(double driveAbility) {
        this.driveAbility = driveAbility;
    }

    /**
     * Getter of fuel consumption
     * @return return double fuelConsumption
     */
    public double getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Setter of fuelConsumption
     * @param fuelConsumption new fuel Consumption setting
     */
    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * equals method
     * @param other Object to be tested for equality
     * @return returns true if Object other is equal to this else returns false
     */
    public boolean equals(Object other) {
        if (!(other instanceof Engine)) return false;

        Engine that = (Engine) other;

        if (!(this.power == that.power)) {
            return false;
        }
        if (!(this.driveAbility == that.driveAbility)) {
            return false;
        }
        if (!(this.fuelConsumption == that.fuelConsumption)) {
            return false;
        }
        return this.brand.equals(that.brand);
    }
}
