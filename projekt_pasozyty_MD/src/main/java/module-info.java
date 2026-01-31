module agh.ics.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens agh.ics.oop to javafx.fxml;
    opens agh.ics.oop.presenter to javafx.fxml;

    exports agh.ics.oop;
    exports agh.ics.oop.model;
    exports agh.ics.oop.model.map;
    exports agh.ics.oop.model.elements;
    exports agh.ics.oop.presenter;
}