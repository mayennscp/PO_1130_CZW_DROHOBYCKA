package agh.ics.oop.model.elements;

import agh.ics.oop.model.map.GlobeMap;
import java.util.ArrayList;
import java.util.List;

public class NormalAnimal extends Animal {
    private final List<Parasite> parasites = new ArrayList<>();

    public NormalAnimal(agh.ics.oop.model.Vector2d pos, int energy, int genLen) {
        super(pos, energy, genLen);
    }

    @Override public void move(GlobeMap map) { performStandardMove(map); }
    public void addParasite(Parasite p) { parasites.add(p); }
    public void removeParasite(Parasite p) { parasites.remove(p); }
    public List<Parasite> getParasites() {
        parasites.removeIf(p -> p.getEnergy() <= 0 || p.getHost() != this);
        return parasites;
    }
    @Override public String getTexturePath() { return "/animal.png"; }
}