package com.javascape.sensors.digital;

import com.javascape.sensors.SensorBase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class DigitalSensor extends SensorBase {

    transient public static final String regex = ",";

    public int index;

    public String name = "Digital Sensor";

    public String className = "DigitalSensor";

    public int numValues = 1;
    public String[] valueNames;

    public ObservableList<String> values = FXCollections.observableArrayList();

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

    /** Returns the last value stored */
    public String getValue() {
        if (values == null || values.size() == 0)
            return null;
        return values.get(values.size() - 1);
    }

    /**
     * This ovverridden method will return the specified value when the sensor
     * returns more than one value at a time. e.g. a sensor that returns both
     * temperature and humidity
     */
    public String getValue(int index) {
        if (values == null || values.size() == 0)
            return null;
        return values.get(values.size() - 1).split(regex)[index];
    }

    public String getValue(String valueName) {
        if (values == null || values.size() == 0)
            return null;
        int index = 0;
        for (String s : valueNames) {
            if (s.equals(valueName))
                return getValue(index);
            index++;
        }
        return null;
    }

    public String[] getValueNames() {
        return valueNames;
    }

    public int getNumValues() {
        return numValues;
    }

    public int getIndex() {
        return index;
    }

    public abstract Node getSensorPane();

    public abstract String getCommand();
}
