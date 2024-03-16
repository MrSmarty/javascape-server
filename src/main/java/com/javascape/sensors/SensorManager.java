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

    private static ObservableList<String> sensors = FXCollections.observableArrayList();
    private static ObservableList<String> digitalSensors = FXCollections.observableArrayList();

    public static ObservableList<String> getSensorList() {
        return sensors;
    }

    public static ObservableList<String> getDigitalSensorList() {
        return digitalSensors;
    }

    public static void initializeSensorLists() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "sensors.txt"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                sensors.add(item[0]);
            }
            scan.close();
        } catch (IOException e) {
            Logger.error("Error trying to fetch sensors from sensor list");
        }

        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "digitalsensors.txt"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                digitalSensors.add(item[0]);
            }
            scan.close();
        } catch (IOException e) {
            Logger.error("Error trying to fetch digital sensors from digital sensor list");
        }
    }

    public static Sensor createNewAnalogSensor(String deviceName, String receiverID, int index) {
        try {
            Class<?> tempClass = Class.forName("com.javascape.sensors.analog."+deviceName);
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
