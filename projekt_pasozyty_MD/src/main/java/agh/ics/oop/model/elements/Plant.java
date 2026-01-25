package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public record Plant(Vector2d position) implements WorldElement {
    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String getTexturePath() {
        return "/plant.png";
    }
}