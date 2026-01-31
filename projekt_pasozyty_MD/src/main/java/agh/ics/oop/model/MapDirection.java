package agh.ics.oop.model;

import java.util.Random;

public enum MapDirection {
    N, NE, E, SE, S, SW, W, NW;

    public MapDirection rotate(int angle) {
        return values()[(this.ordinal() + angle) % 8];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case N -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case S -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
        };
    }

    public MapDirection reverse() { return values()[(this.ordinal() + 4) % 8]; }
    public static MapDirection random() { return values()[new Random().nextInt(8)]; }
}