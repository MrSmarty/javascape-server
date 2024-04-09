package com.javascape.sensors.analog;

import com.javascape.sensors.SensorBase;

/**
 * A base class for all analog sensors
 */
public abstract class Sensor extends SensorBase {

    /** Generic constructor for the Sensor class. */
    public Sensor(String receiverID, String name, int index) {
        this.name = name;
        this.receiverID = receiverID;
        this.index = index;
    }

    /**
     * This method will return the current value as a double instead of a
     * String.
     */
    public abstract Double getCurrentValueAsDouble();

    /** Need it for the editableLabel */
    public void setName(String name) {
        super.setName(name);
    }

}
