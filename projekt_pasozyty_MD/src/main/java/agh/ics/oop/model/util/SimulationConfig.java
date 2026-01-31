package agh.ics.oop.model.util;

public record SimulationConfig(
        int width, int height, int startPlants, int plantEnergy, int plantsPerDay,
        int startAnimals, int startEnergy, int dailyLoss, int readyEnergy,
        int breedLoss, int minM, int maxM, int genomeLen,
        int startParasites, int hostExtraLoss, int panicLoss
) {}