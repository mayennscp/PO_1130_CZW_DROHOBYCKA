package agh.ics.oop.model.util;

import java.io.*;

public class CSVLogger implements AutoCloseable {
    private PrintWriter writer;

    public CSVLogger(String id) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter("stats_" + id + ".csv")));
            writer.println("Day,Animals,Normal,Parasites,Plants,Free,Energy,Life,Children");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void log(int day, StatsTracker s) {
        if (writer == null) return;
        writer.printf("%d,%d,%d,%d,%d,%d,%.2f,%.2f,%.2f%n",
                day, s.animals, s.normalAnimals, s.parasites, s.plants, s.freeFields, s.avgEnergy, s.avgLifeTime, s.avgChildren);
        writer.flush();
    }

    @Override
    public void close() {
        if (writer != null) writer.close();
    }
}