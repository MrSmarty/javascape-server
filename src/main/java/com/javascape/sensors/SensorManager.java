package com.javascape.sensors;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Scanner;

import com.javascape.Logger;
import com.javascape.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SensorManager {

    private static HashMap<String, String> classMap = new HashMap<String, String>();

    public static ObservableList<String> getSensorList() {
        ObservableList<String> sensors = FXCollections.observableArrayList();

        for (String key : classMap.keySet()) {
            sensors.add(key);
        }

        return sensors;
    }

    public static void getClassMap() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "sensors.map"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                classMap.put(item[0], "com.javascape.sensors." + item[1]);
            }
            scan.close();
        } catch (IOException e) {
            Logger.error("Error trying to fetch sensors from sensor map");
        }
    }

    public static Sensor createNewSensor(String deviceName, String receiverID, int index) {
        try {
            Class<?> tempClass = Class.forName(classMap.get(deviceName));
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
}
