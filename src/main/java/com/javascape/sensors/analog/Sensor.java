package com.javascape.sensors.analog;

import com.javascape.sensors.SensorBase;

import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

public abstract class Sensor extends SensorBase {
    public String className;

    /** The list of previous recorded values */
    protected transient ObservableList<Object> valueList;

    /** The name of the sensor */
    public String name = "Analog Sensor";

    public int index;

    public Sensor(String receiverID, String name, int index) {
        this.name = name;
        this.receiverID = receiverID;
        this.index = index;
    }

    /** Sets the name of the sensor */
    public void setName(String newName) {
        name = newName;
    }

    /** returns the name of the sensor */
    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public abstract void addValue(String value);

    public abstract Double getCurrentValueAsDouble();


    public abstract GridPane getSensorPane();

    @Override
    public String toString() {
        return name;
    }
}
