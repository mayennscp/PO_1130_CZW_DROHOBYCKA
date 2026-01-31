package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class SimulationPresenter {
    @FXML private Canvas canvas;
    @FXML private Pane canvasContainer;
    @FXML private Label dayLabel, animalCountLabel, normalCountLabel, parasiteCountLabel;
    @FXML private Label plantCountLabel, freeFieldsLabel, avgEnergyLabel, avgLifeLabel, avgChildrenLabel, genotypeLabel;
    @FXML private Button pauseButton;

    private Simulation sim;
    private final Map<String, Image> imageCache = new HashMap<>();
    private volatile boolean viewRunning = true;

    @FXML public void initialize() {
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());
        canvas.widthProperty().addListener((obs, ov, nv) -> draw());
        canvas.heightProperty().addListener((obs, ov, nv) -> draw());

        imageCache.put("jungle", new Image(getClass().getResourceAsStream("/jungle.png")));
        imageCache.put("steppe", new Image(getClass().getResourceAsStream("/steppe.png")));
        imageCache.put("/animal.png", new Image(getClass().getResourceAsStream("/animal.png")));
        imageCache.put("/parasite.png", new Image(getClass().getResourceAsStream("/parasite.png")));
        imageCache.put("/plant.png", new Image(getClass().getResourceAsStream("/plant.png")));
    }

    public void set(Simulation s) {
        this.sim = s;
        Thread t = new Thread(() -> {
            try {
                while (viewRunning && !Thread.interrupted()) {
                    Platform.runLater(() -> {
                        draw();
                        updateUI();
                    });
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
        });
        t.setDaemon(true); t.start();
    }

    public void stop() {
        this.viewRunning = false;
        if (sim != null) sim.stop();
    }

    @FXML private void onPauseClick() {
        sim.toggle();
        pauseButton.setText(pauseButton.getText().equals("Pauza") ? "Wznów" : "Pauza");
    }

    private void updateUI() {
        if (sim == null) return;
        var st = sim.getStats();
        dayLabel.setText("Dzień: " + sim.getDay());
        animalCountLabel.setText("Suma zwierząt: " + st.animals);
        normalCountLabel.setText("Żywiciele: " + st.normalAnimals);
        parasiteCountLabel.setText("Pasożyty: " + st.parasites);
        plantCountLabel.setText("Rośliny: " + st.plants);
        freeFieldsLabel.setText("Wolne pola: " + st.freeFields);
        avgEnergyLabel.setText("Średnia energia: " + String.format("%.2f", st.avgEnergy));
        avgLifeLabel.setText("Średnie życie: " + String.format("%.2f", st.avgLifeTime));
        avgChildrenLabel.setText("Średnia dzieci: " + String.format("%.2f", st.avgChildren));
        genotypeLabel.setText(st.popularGenotype.toString());
    }

    private void draw() {
        if (sim == null || canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        if (w <= 0 || h <= 0) return;
        int mw = sim.getMap().getWidth(), mh = sim.getMap().getHeight();
        double cs = Math.min(w/mw, h/mh);
        double ox = (w - (cs * mw)) / 2, oy = (h - (cs * mh)) / 2;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        Image jungle = imageCache.get("jungle");
        Image steppe = imageCache.get("steppe");
        for(int x=0; x<mw; x++) for(int y=0; y<mh; y++)
            gc.drawImage(sim.getMap().isEquator(new Vector2d(x,y)) ? jungle : steppe, ox+x*cs, oy+y*cs, cs, cs);

        var best = sim.getStats().popularGenotype;
        sim.getMap().getAll().forEach(e -> {
            double ex = ox + e.getPosition().x()*cs, ey = oy + e.getPosition().y()*cs;
            Image img = imageCache.get(e.getTexturePath());
            if (e instanceof Animal a) {
                if (a.getGenome().getGenes().equals(best)) {
                    gc.setStroke(Color.CYAN); gc.setLineWidth(2); gc.strokeRect(ex, ey, cs, cs);
                }
                gc.save();
                gc.translate(ex+cs/2, ey+cs/2); gc.rotate(a.getDir().ordinal()*45);
                gc.drawImage(img, -cs/2, -cs/2, cs, cs);
                gc.restore();
                double r = Math.clamp((double)a.getEnergy()/sim.getConfig().startEnergy(), 0, 1);
                gc.setFill(Color.color(Math.min(1.0, 2.0*(1.0-r)), Math.min(1.0, 2.0*r), 0));
                gc.fillRect(ex, ey+cs-3, cs*r, 3);
            } else gc.drawImage(img, ex, ey, cs, cs);
        });
    }
}