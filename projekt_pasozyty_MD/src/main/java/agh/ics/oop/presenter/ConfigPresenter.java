package agh.ics.oop.presenter;

import agh.ics.oop.model.util.SimulationConfig;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ConfigPresenter {
    @FXML private TextField widthField, heightField, startPlantsField, plantEnergyField, plantsPerDayField;
    @FXML private TextField startAnimalsField, startEnergyField, dailyLossField, readyEnergyField, breedLossField;
    @FXML private TextField minMutField, maxMutField, genomeLenField, startParasitesField, hostExtraLossField, panicLossField;

    private final SimulationEngine engine = new SimulationEngine();

    @FXML
    public void onStartClick() {
        try {
            SimulationConfig cfg = new SimulationConfig(
                    Integer.parseInt(widthField.getText()),
                    Integer.parseInt(heightField.getText()),
                    Integer.parseInt(startPlantsField.getText()),
                    Integer.parseInt(plantEnergyField.getText()),
                    Integer.parseInt(plantsPerDayField.getText()),
                    Integer.parseInt(startAnimalsField.getText()),
                    Integer.parseInt(startEnergyField.getText()),
                    Integer.parseInt(dailyLossField.getText()),
                    Integer.parseInt(readyEnergyField.getText()),
                    Integer.parseInt(breedLossField.getText()),
                    Integer.parseInt(minMutField.getText()),
                    Integer.parseInt(maxMutField.getText()),
                    Integer.parseInt(genomeLenField.getText()),
                    Integer.parseInt(startParasitesField.getText()),
                    Integer.parseInt(hostExtraLossField.getText()),
                    Integer.parseInt(panicLossField.getText())
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            BorderPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Darwin World Simulation");

            Scene scene = new Scene(root, 1300, 900);
            stage.setScene(scene);

            SimulationPresenter presenter = loader.getController();
            Simulation sim = new Simulation(cfg);
            presenter.set(sim);

            engine.run(sim);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}