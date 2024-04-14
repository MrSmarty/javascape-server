package com.javascape.sensors.digital;

import com.javascape.sensors.SensorBase;

/**
 * A base class for all digital sensors
 */
public abstract class DigitalSensor extends SensorBase {

    /**
     * The delimiter used to split the value string into it's individual parts.
     */
    transient public static final String DELIMITER = ",";

    /**
     * The length of the valueNames array.
     */
    public int numValues = 1;
    /**
     * This type of sensor can collect multiple types of data at once. This
     * array stores the names of those values at the indices they appear in the
     * value string.
     */
    public String[] valueNames;

    /**
     * Generic constructor for the DigitalSensor class.
     *
     * @param receiverID ID of the receiver the sensor is connected to.
     * @param index The index of the sensor on the receiver.
     */
    public DigitalSensor(String receiverID, int index) {
        this.receiverID = receiverID;
        this.index = index;

        name = "Digital Sensor";

        className = "DigitalSensor";
    }

    /**
     * This overridden method will return the specified value given the
     * <strong>index</strong> from {@link #valueNames} when the sensor returns
     * more than one value at a time. e.g. a sensor that returns both
     * temperature and humidity
     *
     * @param index The index of the value to returnn
     * @return The value of the variable at the specified index
     */
    public String getValue(int index) {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        return valueList.get(0).split(DELIMITER)[index];
    }

    /**
     * This overridden method will return the specified value given the
     * <strong>name</strong> from {@link #valueNames} when the sensor returns
     * more than one value at a time. e.g. a sensor that returns both
     * temperature and humidity
     *
     * @param valueName The name of the value to return
     * @return The value of the variable with the specified name
     */
    public String getValue(String valueName) {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        int tempIndex = 0;
        for (String s : valueNames) {
            if (s.equals(valueName)) {
                return getValue(tempIndex);
            }
            tempIndex++;
        }
        return null;
    }

    /**
     * This method returns the index of the value with the specified name
     *
     * @param valueName The name of the value to return
     * @return The index of the value with the specified name
     */
    public int getNameOfValueAtIndex(String valueName) {
        int tempIndex = 0;
        for (String s : valueNames) {
            if (s.equals(valueName)) {
                return tempIndex;
            }
            tempIndex++;
        }
        return -1;
    }

    /**
     * Returns the array with the valueNames
     *
     * @return The array with the valueNames
     */
    public String[] getValueNames() {
        return valueNames;
    }

    /**
     * Returns the number of values the sensor reports on.
     *
     * @return The number of values the sensor reports on.
     */
    public int getNumValues() {
        return numValues;
    }

    /**
     * Returns the command to send to the device to get the value.
     * <p>
     * Used because different sensors require different commands to get the
     * value.
     *
     * @return The command to send to the device to get the value.
     */
    public abstract String getCommand();

    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
