package agh.ics.oop.view;

import agh.ics.oop.model.elements.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldElementBox {
    private static final Map<String, Image> cache = new HashMap<>();

    public static void draw(GraphicsContext gc, WorldElement e, double cellSize) {
        String cleanPath = e.getTexturePath().startsWith("/") ? e.getTexturePath().substring(1) : e.getTexturePath();
        Image img = cache.computeIfAbsent(cleanPath, p -> {
            var stream = WorldElementBox.class.getClassLoader().getResourceAsStream(p);
            return (stream != null) ? new Image(stream) : null;
        });

        if (img == null) return;

        Random jitter = new Random(e.hashCode());
        double offset = cellSize * 0.2;
        double jX = (jitter.nextDouble() - 0.5) * offset;
        double jY = (jitter.nextDouble() - 0.5) * offset;

        double x = e.getPosition().x() * cellSize + jX;
        double y = e.getPosition().y() * cellSize + jY;

        if (e instanceof Animal a) {
            gc.save();
            double angle = a.getDir().ordinal() * 45;
            Rotate r = new Rotate(angle, x + cellSize/2, y + cellSize/2);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            gc.drawImage(img, x, y, cellSize, cellSize);
            gc.restore();
        } else {
            gc.drawImage(img, x, y, cellSize, cellSize);
        }
    }
}