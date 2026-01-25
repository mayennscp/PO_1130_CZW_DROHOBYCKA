package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.*;
import java.util.*;
import java.util.stream.Collectors;

public class StatsTracker {
    public int animals, parasites, normalAnimals, plants, freeFields;
    public List<Integer> popularGenotype = new ArrayList<>();
    public double avgEnergy, avgLifeTime, avgChildren;
    private long totalDeadLifeTime = 0, totalDeadCount = 0;

    public void update(Collection<WorldElement> all, List<Animal> current, int w, int h, List<Animal> dead) {
        this.animals = current.size();
        this.parasites = (int) current.stream().filter(a -> a instanceof Parasite).count();
        this.normalAnimals = (int) current.stream().filter(a -> a instanceof NormalAnimal).count();
        this.plants = (int) all.stream().filter(e -> e instanceof Plant).count();
        Set<Vector2d> occ = all.stream().map(WorldElement::getPosition).collect(Collectors.toSet());
        this.freeFields = (w * h) - occ.size();

        if (!current.isEmpty()) {
            this.avgEnergy = current.stream().mapToInt(Animal::getEnergy).average().orElse(0);
            this.avgChildren = current.stream().mapToInt(Animal::getChildren).average().orElse(0);
            this.popularGenotype = current.stream()
                    .collect(Collectors.groupingBy(a -> a.getGenome().getGenes(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(new ArrayList<>());
        }

        for (Animal d : dead) {
            totalDeadLifeTime += d.getAge();
            totalDeadCount++;
        }
        this.avgLifeTime = totalDeadCount == 0 ? 0 : (double) totalDeadLifeTime / totalDeadCount;
    }
}