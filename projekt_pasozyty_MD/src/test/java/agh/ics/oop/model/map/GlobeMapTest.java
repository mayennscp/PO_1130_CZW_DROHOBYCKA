package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.NormalAnimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GlobeMapTest {
    @Test
    public void testWrapX() {
        GlobeMap map = new GlobeMap(10, 10);
        NormalAnimal a = new NormalAnimal(new Vector2d(9, 5), 100, 10);
        Vector2d wrapped = map.wrap(new Vector2d(10, 5), a);
        assertEquals(new Vector2d(0, 5), wrapped);
    }

    @Test
    public void testEquatorDetection() {
        GlobeMap map = new GlobeMap(10, 10);
        assertTrue(map.isEquator(new Vector2d(5, 4)));
        assertTrue(map.isEquator(new Vector2d(5, 5)));
        assertFalse(map.isEquator(new Vector2d(5, 0)));
        assertFalse(map.isEquator(new Vector2d(5, 9)));
    }
}