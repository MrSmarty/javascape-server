package com.javascape;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Scanner;

import com.javascape.receivers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceiverHandler {
    private ObservableList<Receiver> receivers = FXCollections.observableArrayList();

    transient HashMap<String, String> classMap = new HashMap<String, String>();

    public ReceiverHandler() {
        getClasses();

        // Receiver test =
        // Class.forName(classMap.get("PiPicoW")).getConstructor(int.class).newInstance(0);
    }

    /** Pull the classes from the receivers file */
    private void getClasses() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "receivers.map"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                classMap.put(item[0], "com.javascape.receivers." + item[1]);
            }
            scan.close();
        } catch (IOException e) {
            Logger.error("Error trying to fetch receivers from receiver map");
        }

    }

    /** Get specified receiver by ID */
    public Receiver getReceiver(String ID) {
        if (!receivers.isEmpty())
            for (Receiver r : receivers) {
                if (r.getUID().equals(ID)) {
                    return r;
                }
            }
        return null;
    }

    public ObservableList<Receiver> getReceiverList() {
        System.out.println("There are " + receivers.size() + " receivers");
        return receivers;
    }

    public ObservableList<Receiver> getActiveReceiverList() {
        ObservableList<Receiver> active = FXCollections.observableArrayList();
        for (Receiver r : receivers)
            if (r.getCurrentThread() != null)
                active.add(r);
        System.out.println("There are " + active.size() + " active receivers");
        return active;
    }

    /** Add a receiver to the receivers list */
    public void addReceiver(String deviceType, String ID) {
        try {
            Class<?> tempClass = Class.forName(classMap.get(deviceType));
            Constructor<?> constructor = tempClass.getConstructor(String.class);

            Object instance = constructor.newInstance(ID);
            Receiver temp = (Receiver) instance;

            receivers.add(temp);
            Logger.print(temp.getName() + " | " + temp.getUID());
        } catch (Exception e) {
            Logger.print("Error adding receiver: " + e.toString());
        }
    }

    /** Add a receiver to the receivers list */
    public void addReceiver(String deviceType, String ID, String name) {
        try {
            Class<?> tempClass = Class.forName(classMap.get(deviceType));
            Constructor<?> constructor = tempClass.getConstructor(String.class, String.class, String.class);

            Object instance = constructor.newInstance(ID, name, deviceType);
            Receiver temp = (Receiver) instance;

            receivers.add(temp);
            Logger.print(temp.getName() + " | " + temp.getUID());
        } catch (Exception e) {
            Logger.print(e.toString());
        }
    }
}
