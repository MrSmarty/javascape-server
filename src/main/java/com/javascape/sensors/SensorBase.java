package com.javascape.sensors;

import com.javascape.Settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * A base class that contains commonalities between both ananlog and digital
 * sensors.
 */
public abstract class SensorBase {

    /**
     * The UID of the Receiver that contains the sensor
     */
    public String receiverID;

    /**
     * The index of the sensor on the device. Digital starts at 0 and goes to
     * the number of digital pins - 1. Analog starts at 0 and goes to the number
     * of analog pins - 1.
     */
    public int index;

    /**
     * The name of the Class that the sensor is. Used for deserialization
     */
    public String className = "SensorBase";

    /**
     * The name of the sensor itself. Used to identify sensors easier.
     * <p>
     * Sensors can be renamed using the {@link #setName(String)} method. The
     * default for this value should be the name of the Sensor.
     */
    public String name = "SensorBase";

    /**
     * The list of previously recorded values. The number of values stored is
     * set in {@link Settings}.
     */
    transient public ObservableList<String> valueList = FXCollections.observableArrayList();

    /**
     * Adds a value to the list of values. If the list is full, the oldest value
     * is removed.
     * <p>
     * This operates like a queue where the reported value is the last in.
     *
     * @param value
     */
    public void addValue(String value) {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (valueList.size() >= Settings.maxSensorData) {
            valueList.remove(valueList.size() - 1);
        }
        valueList.add(0, value);

    }

    /**
     * Returns the last value stored
     */
    public String getValue() {
        if (valueList == null || valueList.size() == 0) {
            return null;
        }
        return valueList.get(0);
    }

    /**
     * Returns the name of the sensor
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sensor
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the {@link #index} of the sensor.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns a JavaFX {@link Node} that houses all information on the sensor.
     * Analog sensors should contain a delete button.
     */
    public abstract Node getSensorPane();

    @Override
    public String toString() {
        return name;
    }
}
