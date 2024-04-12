package com.javascape.sensors.analog;

import com.javascape.Helper;
import com.javascape.sensors.SensorBase;

import javafx.collections.FXCollections;

/**
 * A base class for all analog sensors
 */
public abstract class Sensor extends SensorBase {

    public int maxCal = 65536;
    public int minCal = 0;

    /** Generic constructor for the Sensor class. */
    public Sensor(String receiverID, String name, int index) {
        this.name = name;
        this.receiverID = receiverID;
        this.index = index;
    }

    /**
     * This method will return the current value as a String.
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private String getCurrentValue() {
        if (valueList == null)
            valueList = FXCollections.observableArrayList();
        if (valueList.size() > 0) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    /**
     * This method will return the current value as a double instead of a String.
     */
    public Double getCurrentValueAsDouble() {
        if (valueList.size() > 0) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return percent;
        }
        return null;
    }

    /** Returns a value between 0 and 65565 as opposed to a percent */
    public String getCurrentRaw() {
        if (valueList.size() > 0) {
            return valueList.get(0);
        }
        return null;
    }

    /**
     * This method will return the current raw value as a double instead of a String.
     */
    public Double getCurrentRawAsDouble() {
        if (valueList.size() > 0) {
            return Double.parseDouble(valueList.get(0));
        }
        return null;
    }

    /** Need it for the editableLabel */
    public void setName(String name) {
        super.setName(name);
    }

}
