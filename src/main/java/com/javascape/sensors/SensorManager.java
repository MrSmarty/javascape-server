package com.javascape.sensors;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Scanner;

import com.javascape.Logger;
import com.javascape.Settings;
import com.javascape.sensors.analog.Sensor;
import com.javascape.sensors.digital.DigitalSensor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SensorManager {

    /**
     * The list of analog sensors that are available to be used.
     */
    private static final ObservableList<String> sensors = FXCollections.observableArrayList();
    /**
     * The list of digital sensors that are available to be used.
     */
    private static final ObservableList<String> digitalSensors = FXCollections.observableArrayList();

    /**
     * Returns the list of analog sensors available to be used.
     *
     * @return
     */
    public static ObservableList<String> getSensorList() {
        return sensors;
    }

    /**
     * Returns the list of digital sensors available to be used.
     *
     * @return
     */
    public static ObservableList<String> getDigitalSensorList() {
        return digitalSensors;
    }

    /**
     * Initializes the list of sensors from the sensors.txt file, and the list
     * of digitalSensors from the digitalsensors.txt file.
     */
    public static void initializeSensorLists() {
        try {
            try (Scanner scan = new Scanner(new File(Settings.storageLocation + "sensors.txt"))) {
                while (scan.hasNextLine()) {
                    String[] item = scan.nextLine().split(" ");

                    sensors.add(item[0]);
                }
            }
        } catch (IOException e) {
            Logger.error("Error trying to fetch sensors from sensor list");
        }

        try {
            try (Scanner scan = new Scanner(new File(Settings.storageLocation + "digitalsensors.txt"))) {
                while (scan.hasNextLine()) {
                    String[] item = scan.nextLine().split(" ");

                    digitalSensors.add(item[0]);
                }
            }
        } catch (IOException e) {
            Logger.error("Error trying to fetch digital sensors from digital sensor list");
        }
    }

    /**
     * Creates and returns a new analog sensor.
     *
     * @param deviceName the Name of the sensor to create
     * @param receiverID the ID of the receiver that the sensor is connected to
     * @param index the index of the sensor on the receiver
     * @return the new sensor
     */
    public static Sensor createNewAnalogSensor(String deviceName, String receiverID, int index) {
        try {
            Class<?> tempClass = Class.forName("com.javascape.sensors.analog." + deviceName);
            Constructor<?> constructor = tempClass.getConstructor(String.class, String.class, Integer.TYPE);

            Object instance = constructor.newInstance(receiverID, deviceName, index);
            Sensor temp = (Sensor) instance;

            return temp;
        } catch (ClassNotFoundException e) {
            Logger.error("Error trying to get class " + deviceName);
            return null;
        } catch (Exception e) {
            Logger.error(e.toString());
        }
        return null;
    }

    /**
     * Creates and returns a new digital sensor
     * @param sensorName the name of the sensor to create
     * @param index the index of the sensor on the receiver
     * @return the new DigitalSensor
     */
    public static DigitalSensor createNewDigitalSensor(String sensorName, int index) {
        try {
            Class<?> tempClass = Class.forName("com.javascape.sensors.digital." + sensorName);

            Object instance = tempClass.getConstructor(String.class, Integer.TYPE).newInstance("", index);
            DigitalSensor temp = (DigitalSensor) instance;
            System.out.println(temp);
            return temp;
        } catch (ClassNotFoundException e) {
            Logger.error("Error trying to get class " + sensorName);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Logger.error(e.toString());
        }
        return null;
    }
}
