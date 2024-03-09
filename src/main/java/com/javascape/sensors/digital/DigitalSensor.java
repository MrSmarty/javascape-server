package com.javascape.sensors.digital;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class DigitalSensor {

    public String receiverID;

    public int index;

    public String name = "Digital Sensor";

    public String className = "DigitalSensor";

    public ObservableList<String> values;

    public DigitalSensor(String receiverID, int index) {
        this.receiverID = receiverID;
        this.index = index;
    }

    public void addValue(String value) {
        if (values == null)
            values = FXCollections.observableArrayList();
        if (values.size() >= 10)
            values.remove(0);
        values.add(value);

    }

    public String getValue() {
        if (values == null || values.size() == 0)
            return null;
        return values.get(values.size() - 1);
    }

    public abstract Node getSensorPane();
    public abstract String getCommand();
}
