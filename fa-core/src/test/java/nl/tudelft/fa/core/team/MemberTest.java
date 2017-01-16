package nl.tudelft.fa.core.team;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * @author Fabian Mastenbroek <mail.fabianm@gmail.com>
 */
public class MemberTest {

    UUID id;
    String name;
    int salary;
    Member member;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        name = "Hank";
        salary = 123;
        member = new Strategist(id, null, name, salary, 123);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(id, member.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, member.getName());
    }

    @Test
    public void getSalary() throws Exception {
        assertEquals(salary, member.getSalary());
    }

    @Test
    public void setSalary() throws Exception {
        member.setSalary(100);
        assertEquals(100, member.getSalary());
    }

    @Test
    public void equalsNull() {
        assertThat(member, not(equalTo(null)));
    }

    @Test
    public void equalsDifferentType() {
        assertThat(member, not(equalTo("")));
    }

    @Test
    public void equalsReference() {
        assertEquals(member, member);
    }

    @Test
    public void equalsData() {
        assertEquals(new Strategist(id, null, name, salary, 123), member);
    }

    @Test
    public void equalsDifferentId() {
        assertThat(member, not(equalTo(new Strategist(UUID.randomUUID(), null, name, salary, 123))));
    }

    @Test
    public void equalsOtherPropertiesHaveNoEffect() {
        assertEquals(new Strategist(id, null, "Test", salary + 1,15), member);
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(id), member.hashCode());
    }
}
