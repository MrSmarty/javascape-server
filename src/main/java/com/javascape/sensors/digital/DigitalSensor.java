package com.javascape.sensors.digital;

import javafx.scene.Node;

public abstract class DigitalSensor {

    String receiverID;

    int index;

    public DigitalSensor(String receiverID, int index) {
        this.receiverID = receiverID;
        this.index = index;
    }

    public abstract void addValue(String value);
    public abstract Node getSensorPane();
    public abstract String getCommand();
}
