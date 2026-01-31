package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import java.util.*;

public class Genome {
    private final List<Integer> genes;
    private int activeIdx;
    private final Random random = new Random();

    public Genome(int len) {
        this.genes = random.ints(len, 0, 8).boxed().toList();
        this.activeIdx = random.nextInt(len);
    }

    public Genome(Animal p1, Animal p2, int minM, int maxM) {
        int len = p1.getGenome().genes.size();
        Integer[] childGenes = new Integer[len];
        Animal s = p1.getEnergy() >= p2.getEnergy() ? p1 : p2;
        Animal w = (s == p1) ? p2 : p1;

        int split = (int) (len * ((double) s.getEnergy() / (s.getEnergy() + w.getEnergy() + 1)));
        boolean leftSide = random.nextBoolean();

        if (leftSide) {
            for (int i = 0; i < split; i++) childGenes[i] = s.getGenome().genes.get(i);
            for (int i = split; i < len; i++) childGenes[i] = w.getGenome().genes.get(i);
        } else {
            for (int i = 0; i < len - split; i++) childGenes[i] = w.getGenome().genes.get(i);
            for (int i = len - split; i < len; i++) childGenes[i] = s.getGenome().genes.get(i);
        }

        int muts = random.nextInt(maxM - minM + 1) + minM;
        for (int i = 0; i < muts; i++) childGenes[random.nextInt(len)] = random.nextInt(8);

        this.genes = List.of(childGenes);
        this.activeIdx = random.nextInt(len);
    }

    public int next() {
        int g = genes.get(activeIdx);
        activeIdx = (activeIdx + 1) % genes.size();
        return g;
    }

    public List<Integer> getGenes() { return genes; }
}