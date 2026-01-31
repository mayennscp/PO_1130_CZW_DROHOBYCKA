package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void testAdd() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        assertEquals(new Vector2d(3, 4), v1.add(v2));
    }

    @Test
    public void testEquality() {
        Vector2d v1 = new Vector2d(5, 5);
        Vector2d v2 = new Vector2d(5, 5);
        Vector2d v3 = new Vector2d(1, 2);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }
}