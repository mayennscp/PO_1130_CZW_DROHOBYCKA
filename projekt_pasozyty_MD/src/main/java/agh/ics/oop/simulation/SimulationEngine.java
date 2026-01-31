package agh.ics.oop.simulation;

import java.util.concurrent.*;

public class SimulationEngine {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    public void run(Simulation s) { executor.submit(s); }
}