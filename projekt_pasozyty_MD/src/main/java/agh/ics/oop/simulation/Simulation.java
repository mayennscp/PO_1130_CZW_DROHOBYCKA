package agh.ics.oop.simulation;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.map.GlobeMap;
import agh.ics.oop.model.util.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Simulation implements Runnable {
    private final GlobeMap map;
    private final List<Animal> animals = new CopyOnWriteArrayList<>();
    private final SimulationConfig cfg;
    private final StatsTracker stats = new StatsTracker();
    private final CSVLogger logger;
    private boolean paused = false;
    private volatile boolean running = true;
    private int day = 0;
    private final Random random = new Random();

    public Simulation(SimulationConfig cfg) {
        this.cfg = cfg;
        this.map = new GlobeMap(cfg.width(), cfg.height());
        this.logger = new CSVLogger(UUID.randomUUID().toString().substring(0, 8));
        init();
    }

    private void init() {
        for (int i = 0; i < cfg.startAnimals(); i++) {
            NormalAnimal a = new NormalAnimal(new Vector2d(random.nextInt(cfg.width()), random.nextInt(cfg.height())), cfg.startEnergy(), cfg.genomeLen());
            animals.add(a); map.place(a);
        }
        for (int i = 0; i < cfg.startParasites(); i++) {
            Parasite p = new Parasite(new Vector2d(random.nextInt(cfg.width()), random.nextInt(cfg.height())), cfg.startEnergy() * 3, cfg.genomeLen(), cfg.panicLoss());
            animals.add(p); map.place(p);
        }
        map.growProper(cfg.startPlants());
    }

    public void toggle() { paused = !paused; }
    public void stop() { running = false; logger.close(); }

    @Override public void run() {
        while (running && !Thread.interrupted()) {
            if (!paused) try { step(); } catch (Exception e) { e.printStackTrace(); }
            try { Thread.sleep(150); } catch (InterruptedException e) { break; }
        }
    }

    private void step() {
        List<Animal> dead = animals.stream().filter(a -> a.getEnergy() <= 0).toList();
        animals.removeIf(a -> a.getEnergy() <= 0);
        map.clearAnimals();

        for (Animal a : animals) {
            a.move(map);
            if (a instanceof Parasite p && p.getHost() == null) {
                map.getAnimalPositions().getOrDefault(p.getPosition(), List.of()).stream()
                        .filter(h -> h instanceof NormalAnimal).map(h -> (NormalAnimal)h)
                        .findFirst().ifPresent(p::setHost);
            }
            map.place(a);
        }

        Map<Vector2d, List<Animal>> posMap = map.getAnimalPositions();
        for (var entry : posMap.entrySet()) {
            if (map.getPlants().containsKey(entry.getKey())) {
                entry.getValue().stream().filter(a -> a instanceof NormalAnimal)
                        .max(Comparator.comparingInt(Animal::getEnergy)).ifPresent(b -> {
                            b.addEnergy(cfg.plantEnergy() * 2);
                            map.getPlants().remove(entry.getKey());
                        });
            }
        }

        for (Animal a : animals) {
            a.subEnergy(cfg.dailyLoss());
            if (a instanceof NormalAnimal n) {
                List<Parasite> attached = n.getParasites();
                if (!attached.isEmpty()) {
                    n.subEnergy(attached.size() * cfg.hostExtraLoss());
                    attached.forEach(p -> p.addEnergy(cfg.hostExtraLoss() + cfg.dailyLoss() + 5));
                }
            }
            a.incAge();
        }

        reproduce();
        map.growProper(cfg.plantsPerDay());
        stats.update(map.getAll(), new ArrayList<>(animals), cfg.width(), cfg.height(), dead);
        logger.log(day, stats);
        day++;
    }

    private void reproduce() {
        Map<Vector2d, List<Animal>> posMap = map.getAnimalPositions();
        for (var entry : posMap.entrySet()) {
            List<Animal> list = new ArrayList<>(entry.getValue());
            if (list.size() < 2) continue;
            list.sort(Comparator.comparingInt(Animal::getEnergy).reversed());

            List<NormalAnimal> ns = list.stream().filter(a -> a instanceof NormalAnimal && a.getEnergy() >= cfg.readyEnergy()).map(a -> (NormalAnimal)a).limit(2).toList();
            if (ns.size() == 2) {
                NormalAnimal child = new NormalAnimal(entry.getKey(), cfg.breedLoss() * 2, cfg.genomeLen());
                child.setGenome(new Genome(ns.get(0), ns.get(1), cfg.minM(), cfg.maxM()));
                newborns_add(child, ns.get(0), ns.get(1));
            }

            List<Parasite> ps = list.stream().filter(a -> a instanceof Parasite && a.getEnergy() >= cfg.readyEnergy()).map(a -> (Parasite)a).limit(2).toList();
            if (ps.size() == 2) {
                Parasite child = new Parasite(entry.getKey(), cfg.breedLoss() * 4, cfg.genomeLen(), cfg.panicLoss());
                child.setGenome(new Genome(ps.get(0), ps.get(1), cfg.minM(), cfg.maxM()));
                newborns_add(child, ps.get(0), ps.get(1));
            }
        }
    }

    private void newborns_add(Animal c, Animal p1, Animal p2) {
        animals.add(c); map.place(c);
        p1.subEnergy(cfg.breedLoss()); p2.subEnergy(cfg.breedLoss());
        p1.incChildren(); p2.incChildren();
    }

    public GlobeMap getMap() { return map; }
    public StatsTracker getStats() { return stats; }
    public int getDay() { return day; }
    public SimulationConfig getConfig() { return cfg; }
}