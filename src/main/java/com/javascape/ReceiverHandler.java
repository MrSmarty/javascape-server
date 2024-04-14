package com.javascape;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

import com.javascape.receivers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceiverHandler {
    private final ObservableList<Receiver> receivers = FXCollections.observableArrayList();

    /**
     * Maps the the name of the device to the name of the Class.
     */
    transient ArrayList<String> classList = new ArrayList<String>();

    /** Generic constructor that creates and initializes the ReceiverHandler. */
    public ReceiverHandler() {
        getClasses();

    }

    /** Pull the classes from the receivers file */
    private void getClasses() {
        try {
            try (Scanner scan = new Scanner(new File(Settings.storageLocation + "receivers.txt"))) {
                while (scan.hasNextLine()) {
                    String[] item = scan.nextLine().split(" ");
                    
                    classList.add("com.javascape.receivers." + item[0]);
                }
            }
        } catch (IOException e) {
            Logger.error("Error trying to fetch receivers from receiver list");
        }

    }

    /** 
     * Get specified receiver by ID
     * @param ID The UID of the Receiver you would like to retrieve
     * @return The Receiver with the specified ID
     */
    public Receiver getReceiver(String ID) {
        if (!receivers.isEmpty())
            for (Receiver r : receivers) {
                if (r.getUID().equals(ID)) {
                    return r;
                }
            }
        return null;
    }

    /**
     * Returns the list of Receivers
     * @return The list of Receivers
     */
    public ObservableList<Receiver> getReceiverList() {
        System.out.println("There are " + receivers.size() + " receivers");
        return receivers;
    }

    /**
     * Returns the list of active Receivers. A Receiver is considered active if
     * there is current a socket connection.
     * @return The list of active Receivers
     */
    public ObservableList<Receiver> getActiveReceiverList() {
        ObservableList<Receiver> active = FXCollections.observableArrayList();
        for (Receiver r : receivers)
            if (r.getCurrentThread() != null)
                active.add(r);
        System.out.println("There are " + active.size() + " active receivers");
        return active;
    }

    /** Add a receiver to the receivers list 
     * @param deviceType The type of the device
     * @param ID The UID of the device
    */
    public void addReceiver(String deviceType, String ID) {
        try {
            Class<?> tempClass = Class.forName("com.javascape.receivers." + deviceType);
            Constructor<?> constructor = tempClass.getConstructor(String.class);

            Object instance = constructor.newInstance(ID);
            Receiver temp = (Receiver) instance;

            receivers.add(temp);
            Logger.print(temp.getName() + " | " + temp.getUID());
        } catch (Exception e) {
            Logger.print("Error adding receiver: " + e.toString());
        }
    }

    /** Add a receiver to the receivers list
     * @param deviceType The type of the device
     * @param ID The UID of the device
     * @param name The name of the device
    */
    public void addReceiver(String deviceType, String ID, String name) {
        try {
            Class<?> tempClass = Class.forName("com.javascape.receivers." + deviceType);
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
