package nl.tudelft.fa.core.model.team;

/**
 * Class representing a crew member
 * @version 30 11 2016
 * @author F.C. Slothouber
 */
public abstract class Member {

    private String name;
    private int salary;
    private String id;

    /**
     * Constructor (abstract)
     * @param name Name of crew member
     * @param salary salary of crew member
     */
    public Member(String name, int salary, String id) {
        this.name = name;
        this.salary = salary;
        this.id = id;
    }

    /**
     * Getter of name
     * @return returns String name
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
     * Getter of salary
     * @return returns int salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * Setter salary
     * @param salary new int salary
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Getter of String ID
     * @return returns String ID
     */
    public String getID() {
        return this.id;
    }

    /**
     * Setter of ID
     * @param id new ID value
     */
    public void SetID(String id) {
        this.id = id;
    }
    /**
     * equals method
     * @param other Object to be tested for equality
     * @return returns true if equal else false
     */
    public boolean equals(Object other){
        if (other instanceof  Member) {
            Member that = (Member) other;
            return this.id.equals(that.id);
        } return false;
    }
}
