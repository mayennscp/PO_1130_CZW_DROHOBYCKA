package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.map.GlobeMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParasiteLogicTest {
    @Test
    public void testParasiteAttachment() {
        NormalAnimal host = new NormalAnimal(new Vector2d(2, 2), 100, 10);
        Parasite parasite = new Parasite(new Vector2d(2, 2), 100, 10, 5);

        parasite.setHost(host);
        assertEquals(host, parasite.getHost());
        assertTrue(host.getParasites().contains(parasite));
    }

    @Test
    public void testParasiteMovementWithHost() {
        GlobeMap map = new GlobeMap(10, 10);
        NormalAnimal host = new NormalAnimal(new Vector2d(5, 5), 100, 10);
        Parasite parasite = new Parasite(new Vector2d(0, 0), 100, 10, 5);

        parasite.setHost(host);
        parasite.move(map);

        Vector2d spot = map.getParasiteSpot(host.getPosition());
        assertEquals(spot, parasite.getPosition());
    }

    @Test
    public void testParasitePanicAfterHostDeath() {
        GlobeMap map = new GlobeMap(10, 10);
        NormalAnimal host = new NormalAnimal(new Vector2d(5, 5), 0, 10);
        Parasite parasite = new Parasite(new Vector2d(5, 6), 100, 10, 20);

        parasite.setHost(host);
        int initialEnergy = parasite.getEnergy();
        parasite.move(map);

        assertNull(parasite.getHost());
        assertTrue(parasite.getEnergy() < initialEnergy);
    }
}