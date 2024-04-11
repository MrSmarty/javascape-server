package com.javascape.sensors.digital;

import com.javascape.sensors.SensorBase;

/**
 * A base class for all digital sensors
 */
public abstract class DigitalSensor extends SensorBase {

    /** The delimiter used to split the value string into it's individual parts. */
    transient public static final String DELIMITER = ",";

    /** The length of the valueNames array. */
    public int numValues = 1;
    /**
     * This type of sensor can collect multiple types of data at once. This array
     * stores the names of those values at the indices they appear in the value
     * string.
     */
    public String[] valueNames;

    /** Generic constructor for the DigitalSensor class. */
    public DigitalSensor(String receiverID, int index) {
        this.receiverID = receiverID;
        this.index = index;

        name = "Digital Sensor";

        className = "DigitalSensor";
    }

    /**
     * This overridden method will return the specified value given the
     * <strong>index</strong> from {@link #valueNames} when the sensor
     * returns more than one value at a time. e.g. a sensor that returns both
     * temperature and humidity
     */
    public String getValue(int index) {
        if (valueList == null || valueList.size() == 0)
            return null;
        return valueList.get(valueList.size() - 1).split(DELIMITER)[index];
    }

    /**
     * This overridden method will return the specified value given the
     * <strong>name</strong> from {@link #valueNames} when the sensor
     * returns more than one value at a time. e.g. a sensor that returns both
     * temperature and humidity
     */
    public String getValue(String valueName) {
        if (valueList == null || valueList.size() == 0)
            return null;
        int index = 0;
        for (String s : valueNames) {
            if (s.equals(valueName))
                return getValue(index);
            index++;
        }
        return null;
    }

    /** Returns the array with the valueNames */
    public String[] getValueNames() {
        return valueNames;
    }

    /** Returns the number of values the sensor reports on. */
    public int getNumValues() {
        return numValues;
    }

    /**
     * Returns the command to send to the device to get the value.
     * <p>
     * Used because different sensors require different commands to get the value.
     */
    public abstract String getCommand();

    public void setName(String name) {
        super.setName(name);
    }
}
