package com.javascape;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Scanner;

import com.javascape.recievers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecieverHandler {
    private ObservableList<Reciever> recievers = FXCollections.observableArrayList();

    transient HashMap<String, String> classMap = new HashMap<String, String>();

    public RecieverHandler() {
        getClasses();

        // Reciever test =
        // Class.forName(classMap.get("PiPicoW")).getConstructor(int.class).newInstance(0);
    }

    /** Pull the classes from the recievers file */
    private void getClasses() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "recievers.map"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                classMap.put(item[0], "Java.Server.Recievers." + item[1]);
            }
        } catch (IOException e) {
            Logger.error("Error trying to fetch recievers from reciever map");
        }

    }

    /** Get specified reciever by ID */
    public Reciever getReciever(String ID) {
        for (Reciever r : recievers) {
            if (r.getUID().equals(ID)) {
                return r;
            }
        }
        return null;
    }

    public ObservableList<Reciever> getRecieverList() {
        System.out.println("There are " + recievers.size() + " recievers");
        return recievers;
    }

    public ObservableList<Reciever> getActiveRecieverList() {
        ObservableList<Reciever> active = FXCollections.observableArrayList();
        for (Reciever r : recievers)
            if (r.getCurrentThread() != null)
                active.add(r);
        System.out.println("There are " + active.size() + " active recievers");
        return active;
    }

    /** Add a reciever to the recievers list */
    public void addReciever(String deviceType, String ID) {
        try {
            Class<?> tempClass = Class.forName(classMap.get(deviceType));
            Constructor<?> constructor = tempClass.getConstructor(String.class);

            Object instance = constructor.newInstance(ID);
            Reciever temp = (Reciever) instance;

            recievers.add(temp);
            Logger.print(temp.getName() + " | " + temp.getUID());
        } catch (Exception e) {
            Logger.print("Error adding reciever: " + e.toString());
        }
    }

    /** Add a reciever to the recievers list */
    public void addReciever(String deviceType, String ID, String name) {
        try {
            Class<?> tempClass = Class.forName(classMap.get(deviceType));
            Constructor<?> constructor = tempClass.getConstructor(String.class, String.class, String.class);

            Object instance = constructor.newInstance(ID, name, deviceType);
            Reciever temp = (Reciever) instance;

            recievers.add(temp);
            Logger.print(temp.getName() + " | " + temp.getUID());
        } catch (Exception e) {
            Logger.print(e.toString());
        }
    }
}
