package agh.ics.oop.model;

import agh.ics.oop.model.elements.NormalAnimal;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GenomeTest {
    @Test
    public void testGenomeInheritance() {
        NormalAnimal p1 = new NormalAnimal(new Vector2d(0, 0), 100, 10);
        NormalAnimal p2 = new NormalAnimal(new Vector2d(0, 0), 100, 10);

        Genome childGenome = new Genome(p1, p2, 0, 0);
        List<Integer> genes = childGenome.getGenes();

        assertEquals(10, genes.size());
        for (int gene : genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
    }
}