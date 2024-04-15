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

    /**
     * Generic constructor for the Sensor class.
     *
     * @param receiverID the UID of the Receiver that contains the sensor
     * @param name the name of the sensor itself
     * @param index the index of the sensor on the Receiver/Device
     */
    public Sensor(String receiverID, String name, int index) {
        this.name = name;
        this.receiverID = receiverID;
        this.index = index;
    }

    /**
     * This method will return the current value as a String.
     * <p>
     * value is calculated as follows: (value - minCal) / (maxCal - minCal) *
     * 100 to produce a percentage
     *
     * @return the current value as a String
     */
    public String getCurrentValue() {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (!valueList.isEmpty()) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    /**
     * This method will return the current value as a double instead of a
     * String.
     * <p>
     * value is calculated as follows: (value - minCal) / (maxCal - minCal) *
     * 100 to produce a percentage
     *
     * @return the current value as a double
     */
    public Double getCurrentValueAsDouble() {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (!valueList.isEmpty()) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return percent;
        }
        return null;
    }

    /**
     * Returns a value between 0 and 65565 as opposed to a percent
     *
     * @return the current raw value as a String
     */
    public String getCurrentRaw() {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (!valueList.isEmpty()) {
            return valueList.get(0);
        }
        return null;
    }

    /**
     * This method will return the current raw value as a double instead of a
     * String.
     *
     * @return the current raw value as a double
     */
    public Double getCurrentRawAsDouble() {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (!valueList.isEmpty()) {
            return Double.valueOf(valueList.get(0));
        }
        return null;
    }

    /**
     * Need it for the editableLabel
     *
     * @param name the new name of the sensor
     */
    @Override
    public void setName(String name) {
        super.setName(name);
    }

}
