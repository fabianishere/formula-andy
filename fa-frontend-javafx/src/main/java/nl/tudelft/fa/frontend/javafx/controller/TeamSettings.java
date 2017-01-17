package nl.tudelft.fa.frontend.javafx.controller;

/**
 * Created by fchri on 15-1-2017.
 */
public class TeamSettings {

    private boolean raceable;

    private String mechanic1;
    private int mechanicRisk1;
    private String mechanic2;
    private int mechanicRisk2;

    private String aero1;
    private int aeroRisk1;
    private String aero2;
    private int aeroRisk2;

    private String strategist1;
    private int strategistRisk1;
    private String strategist2;
    private int strategistRisk2;

    private String driver1;
    private String driver2;

    private String tire1;
    private String tire2;

    private String engine1;
    private String engine2;

    public TeamSettings() {
        raceable = false;
    }

    public boolean isRaceable() {
        return raceable;
    }

    public void setRaceable(boolean raceable) {
        this.raceable = raceable;
    }

    public String getMechanic1() {
        return mechanic1;
    }

    public void setMechanic1(String mechanic1) {
        this.mechanic1 = mechanic1;
    }

    public int getMechanicRisk1() {
        return mechanicRisk1;
    }

    public void setMechanicRisk1(int mechanicRisk1) {
        this.mechanicRisk1 = mechanicRisk1;
    }

    public String getMechanic2() {
        return mechanic2;
    }

    public void setMechanic2(String mechanic2) {
        this.mechanic2 = mechanic2;
    }

    public int getMechanicRisk2() {
        return mechanicRisk2;
    }

    public void setMechanicRisk2(int mechanicRisk2) {
        this.mechanicRisk2 = mechanicRisk2;
    }

    public String getAero1() {
        return aero1;
    }

    public void setAero1(String aero1) {
        this.aero1 = aero1;
    }

    public int getAeroRisk1() {
        return aeroRisk1;
    }

    public void setAeroRisk1(int aeroRisk1) {
        this.aeroRisk1 = aeroRisk1;
    }

    public String getAero2() {
        return aero2;
    }

    public void setAero2(String aero2) {
        this.aero2 = aero2;
    }

    public int getAeroRisk2() {
        return aeroRisk2;
    }

    public void setAeroRisk2(int aeroRisk2) {
        this.aeroRisk2 = aeroRisk2;
    }

    public String getStrategist1() {
        return strategist1;
    }

    public void setStrategist1(String strategist1) {
        this.strategist1 = strategist1;
    }

    public int getStrategistRisk1() {
        return strategistRisk1;
    }

    public void setStrategistRisk1(int strategistRisk1) {
        this.strategistRisk1 = strategistRisk1;
    }

    public String getStrategist2() {
        return strategist2;
    }

    public void setStrategist2(String strategist2) {
        this.strategist2 = strategist2;
    }

    public int getStrategistRisk2() {
        return strategistRisk2;
    }

    public void setStrategistRisk2(int strategistRisk2) {
        this.strategistRisk2 = strategistRisk2;
    }

    public String getDriver1() {
        return driver1;
    }

    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }

    public String getDriver2() {
        return driver2;
    }

    public void setDriver2(String driver2) {
        this.driver2 = driver2;
    }

    public String getTire1() {
        return tire1;
    }

    public void setTire1(String tire1) {
        this.tire1 = tire1;
    }

    public String getTire2() {
        return tire2;
    }

    public void setTire2(String tire2) {
        this.tire2 = tire2;
    }

    public String getEngine1() {
        return engine1;
    }

    public void setEngine1(String engine1) {
        this.engine1 = engine1;
    }

    public String getEngine2() {
        return engine2;
    }

    public void setEngine2(String engine2) {
        this.engine2 = engine2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamSettings)) return false;

        TeamSettings settings = (TeamSettings) o;

        if (isRaceable() != settings.isRaceable()) return false;
        if (getMechanicRisk1() != settings.getMechanicRisk1()) return false;
        if (getMechanicRisk2() != settings.getMechanicRisk2()) return false;
        if (getAeroRisk1() != settings.getAeroRisk1()) return false;
        if (getAeroRisk2() != settings.getAeroRisk2()) return false;
        if (getStrategistRisk1() != settings.getStrategistRisk1()) return false;
        if (getStrategistRisk2() != settings.getStrategistRisk2()) return false;
        if (getMechanic1() != null ? !getMechanic1().equals(settings.getMechanic1()) : settings.getMechanic1() != null)
            return false;
        if (getMechanic2() != null ? !getMechanic2().equals(settings.getMechanic2()) : settings.getMechanic2() != null)
            return false;
        if (getAero1() != null ? !getAero1().equals(settings.getAero1()) : settings.getAero1() != null) return false;
        if (getAero2() != null ? !getAero2().equals(settings.getAero2()) : settings.getAero2() != null) return false;
        if (getStrategist1() != null ? !getStrategist1().equals(settings.getStrategist1()) : settings.getStrategist1() != null)
            return false;
        if (getStrategist2() != null ? !getStrategist2().equals(settings.getStrategist2()) : settings.getStrategist2() != null)
            return false;
        if (getDriver1() != null ? !getDriver1().equals(settings.getDriver1()) : settings.getDriver1() != null)
            return false;
        if (getDriver2() != null ? !getDriver2().equals(settings.getDriver2()) : settings.getDriver2() != null)
            return false;
        if (getTire1() != null ? !getTire1().equals(settings.getTire1()) : settings.getTire1() != null) return false;
        if (getTire2() != null ? !getTire2().equals(settings.getTire2()) : settings.getTire2() != null) return false;
        if (getEngine1() != null ? !getEngine1().equals(settings.getEngine1()) : settings.getEngine1() != null)
            return false;
        return getEngine2() != null ? getEngine2().equals(settings.getEngine2()) : settings.getEngine2() == null;

    }

    @Override
    public int hashCode() {
        int result = (isRaceable() ? 1 : 0);
        result = 31 * result + (getMechanic1() != null ? getMechanic1().hashCode() : 0);
        result = 31 * result + getMechanicRisk1();
        result = 31 * result + (getMechanic2() != null ? getMechanic2().hashCode() : 0);
        result = 31 * result + getMechanicRisk2();
        result = 31 * result + (getAero1() != null ? getAero1().hashCode() : 0);
        result = 31 * result + getAeroRisk1();
        result = 31 * result + (getAero2() != null ? getAero2().hashCode() : 0);
        result = 31 * result + getAeroRisk2();
        result = 31 * result + (getStrategist1() != null ? getStrategist1().hashCode() : 0);
        result = 31 * result + getStrategistRisk1();
        result = 31 * result + (getStrategist2() != null ? getStrategist2().hashCode() : 0);
        result = 31 * result + getStrategistRisk2();
        result = 31 * result + (getDriver1() != null ? getDriver1().hashCode() : 0);
        result = 31 * result + (getDriver2() != null ? getDriver2().hashCode() : 0);
        result = 31 * result + (getTire1() != null ? getTire1().hashCode() : 0);
        result = 31 * result + (getTire2() != null ? getTire2().hashCode() : 0);
        result = 31 * result + (getEngine1() != null ? getEngine1().hashCode() : 0);
        result = 31 * result + (getEngine2() != null ? getEngine2().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TeamSettings{" +
                "raceable=" + raceable +
                ", mechanic1='" + mechanic1 + '\'' +
                ", mechanicRisk1=" + mechanicRisk1 +
                ", mechanic2='" + mechanic2 + '\'' +
                ", mechanicRisk2=" + mechanicRisk2 +
                ", aero1='" + aero1 + '\'' +
                ", aeroRisk1=" + aeroRisk1 +
                ", aero2='" + aero2 + '\'' +
                ", aeroRisk2=" + aeroRisk2 +
                ", strategist1='" + strategist1 + '\'' +
                ", strategistRisk1=" + strategistRisk1 +
                ", strategist2='" + strategist2 + '\'' +
                ", strategistRisk2=" + strategistRisk2 +
                ", driver1='" + driver1 + '\'' +
                ", driver2='" + driver2 + '\'' +
                ", tire1='" + tire1 + '\'' +
                ", tire2='" + tire2 + '\'' +
                ", engine1='" + engine1 + '\'' +
                ", engine2='" + engine2 + '\'' +
                '}';
    }
}
