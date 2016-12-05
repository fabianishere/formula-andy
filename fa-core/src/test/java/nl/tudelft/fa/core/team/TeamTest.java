package nl.tudelft.fa.core.team;

import nl.tudelft.fa.core.team.manager.Manager;
import nl.tudelft.fa.core.team.manager.UserManager;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class TeamTest {

    UUID id;
    String name;
    int budget;
    Manager manager;
    List<Member> staff;
    Team team;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        name = "Redbull";
        budget = 1000;
        manager = new UserManager();
        staff = new ArrayList<>();
        staff.add(new Strategist(UUID.randomUUID(),"Barnes", 200, 100));
        team = new Team(id, name, budget, manager, staff);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, team.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, team.getName());
    }

    @Test
    public void getBudget() throws Exception {
        assertEquals(budget, team.getBudget());
    }

    @Test
    public void getManager() throws Exception {
        assertEquals(manager, team.getManager());
    }

    @Test
    public void getStaff() throws Exception {
        assertEquals(staff, team.getStaff());
    }

    @Test
    public void equalsNull() {
        assertThat(team, not(equalTo(null)));
    }

    @Test
    public void equalsReference() {
        assertEquals(team, team);
    }

    @Test
    public void equalsData() {
        assertEquals(new Team(id, name, budget, manager, staff), team);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(team, not(equalTo(new Team(UUID.randomUUID(), name, budget, manager, staff))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Team(id, "Test", budget + 1, null, Collections.emptyList()), team);
    }

    @Test
    public void testHashCode() {
        assertEquals(id.hashCode(), team.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(String.format("Team(id=%s, name=%s)", id, name), team.toString());
    }

}
