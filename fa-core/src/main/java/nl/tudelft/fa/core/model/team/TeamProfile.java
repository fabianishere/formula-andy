package nl.tudelft.fa.core.model.team;

public class TeamProfile {

    private String name;
    private int budget;

    public TeamProfile(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public boolean equals(Object other) {
        if (!(other instanceof  TeamProfile)) {
            return false;
        }

        TeamProfile that = (TeamProfile) other;
        return this.name.equals(that.name);
    }
}
