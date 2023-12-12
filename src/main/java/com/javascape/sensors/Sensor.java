package com.javascape.sensors;

import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

public abstract class Sensor {
    protected String className;

    /** The list of previous recorded values */
    protected transient ObservableList<Object> valueList;

    protected String recieverID;

    /** The name of the sensor */
    protected String name;

    protected int index;

    public Sensor(String recieverID, String name, int index) {
        this.name = name;
        this.recieverID = recieverID;
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

    /**
     * Returns the GridPane of the Sensor.
     * Should include a delete button
     * 
     * @return The GridPane of the Sensor
     */
    abstract public GridPane getSensorPane();

    @Override
    public String toString() {
        return name;
    }
}
