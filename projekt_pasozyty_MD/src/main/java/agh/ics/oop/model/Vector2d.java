package agh.ics.oop.model;

public record Vector2d(int x, int y) {
    public Vector2d add(Vector2d other) { return new Vector2d(x + other.x, y + other.y); }
    public Vector2d subtract(Vector2d other) { return new Vector2d(x - other.x, y - other.y); }
}