package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GlobeMap implements WorldMap {
    private final int width, height;
    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2d, Plant> plants = new ConcurrentHashMap<>();
    private final int eqS, eqE;

    public GlobeMap(int w, int h) {
        this.width = w; this.height = h;
        int eh = (int) Math.max(1, h * 0.2);
        this.eqS = (h - eh) / 2; this.eqE = eqS + eh;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isEquator(Vector2d p) { return p.y() >= eqS && p.y() < eqE; }

    public Vector2d getParasiteSpot(Vector2d hostPos) {
        int ny = hostPos.y() + 1;
        if (ny >= height) ny = hostPos.y() - 1;
        return new Vector2d(hostPos.x(), ny);
    }

    public Vector2d wrap(Vector2d p, Animal a) {
        int nx = (p.x() + width) % width;
        int ny = Math.clamp(p.y(), 0, height - 1);
        if (p.y() < 0 || p.y() >= height) a.setDir(a.getDir().reverse());
        return new Vector2d(nx, ny);
    }

    @Override public void place(WorldElement e) {
        if (e instanceof Animal a) animals.computeIfAbsent(a.getPosition(), k -> Collections.synchronizedList(new ArrayList<>())).add(a);
        else if (e instanceof Plant p) plants.put(p.getPosition(), p);
    }

    public void clearAnimals() { animals.clear(); }
    @Override public void moveAll() {}

    @Override
    public Collection<WorldElement> getAll() {
        List<WorldElement> res = new ArrayList<>(plants.values());
        animals.values().forEach(res::addAll);
        return res;
    }

    @Override public void grow(int n) { growProper(n); }

    public void growProper(int n) {
        Random r = new Random();
        int tries = 0;
        while (n > 0 && tries < 100) {
            int y = r.nextInt(100) < 80 ? r.nextInt(eqE - eqS) + eqS : r.nextInt(height);
            Vector2d p = new Vector2d(r.nextInt(width), y);
            if (!plants.containsKey(p)) { plants.put(p, new Plant(p)); n--; }
            tries++;
        }
    }

    public Map<Vector2d, List<Animal>> getAnimalPositions() { return animals; }
    public Map<Vector2d, Plant> getPlants() { return plants; }
}