package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.WorldElement;
import java.util.*;

public interface WorldMap {
    void place(WorldElement e);
    void moveAll();
    Collection<WorldElement> getAll();
    void grow(int n);
}