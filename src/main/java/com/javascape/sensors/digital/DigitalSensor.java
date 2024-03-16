package com.javascape.sensors.digital;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class DigitalSensor {

    transient public static final String regex = ",";

    public String receiverID;

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

    public abstract Node getSensorPane();

    public abstract String getCommand();
}
