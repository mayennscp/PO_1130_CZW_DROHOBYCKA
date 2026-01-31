package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.NormalAnimal;
import agh.ics.oop.model.elements.Plant;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StatsTrackerTest {
    @Test
    public void testStatsUpdate() {
        StatsTracker tracker = new StatsTracker();
        NormalAnimal a1 = new NormalAnimal(new Vector2d(1, 1), 50, 10);
        NormalAnimal a2 = new NormalAnimal(new Vector2d(2, 2), 70, 10);
        Plant p1 = new Plant(new Vector2d(3, 3));

        tracker.update(List.of(a1, a2, p1), List.of(a1, a2), 10, 10, List.of());

        assertEquals(2, tracker.animals);
        assertEquals(1, tracker.plants);
        assertEquals(97, tracker.freeFields);
        assertEquals(60.0, tracker.avgEnergy);
    }
}