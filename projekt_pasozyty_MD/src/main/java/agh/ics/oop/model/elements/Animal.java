package agh.ics.oop.model.elements;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.GlobeMap;

public abstract class Animal implements WorldElement {
    protected Vector2d pos;
    protected MapDirection dir;
    protected int energy;
    protected Genome genome;
    protected int age = 0;
    protected int children = 0;

    public Animal(Vector2d pos, int energy, int genLen) {
        this.pos = pos;
        this.energy = energy;
        this.genome = new Genome(genLen);
        this.dir = MapDirection.random();
    }

    public void performStandardMove(GlobeMap map) {
        this.dir = this.dir.rotate(genome.next());
        this.pos = map.wrap(this.pos.add(this.dir.toUnitVector()), this);
    }

    public abstract void move(GlobeMap map);
    public abstract String getTexturePath();

    public Vector2d getPosition() { return pos; }
    public void setPosition(Vector2d p) { this.pos = p; }
    public int getEnergy() { return energy; }
    public void addEnergy(int e) { this.energy += e; }
    public void subEnergy(int e) { this.energy -= e; }
    public Genome getGenome() { return genome; }
    public void setGenome(Genome g) { this.genome = g; }
    public MapDirection getDir() { return dir; }
    public void setDir(MapDirection d) { dir = d; }
    public int getAge() { return age; }
    public int getChildren() { return children; }
    public void incAge() { age++; }
    public void incChildren() { children++; }
}