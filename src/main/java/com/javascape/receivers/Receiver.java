package com.javascape.receivers;

import java.util.ArrayList;

import com.javascape.ServerThread;
import com.javascape.sensors.analog.Sensor;
import com.javascape.sensors.digital.DigitalSensor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Receiver {

    /**
     * Unique ID of the Receiver.
     */
    protected String uid;

    /**
     * Name of the Receiver
     */
    protected String name;

    /**
     * Specifies the type of Receiver
     */
    protected String type;

    /**
     * The Household that this Receiver is a part of
     */
    protected int householdID = -1;

    /**
     * Array that stores all the GPIO pins for the receiver
     */
    protected GPIO gpio[];

    /**
     * Sensor array lol
     */
    protected Sensor[] sensors;

    /**
     * Boolean to tell whether or not the receiver is connected
     */
    protected boolean connected = false;

    /**
     * The current thread that the Receiver is running
     */
    transient protected ServerThread currentThread;

    /**
     * Label to display the internal temperatuer of the Receiver
     */
    transient protected Label tempLabel;

    /**
     * List of the last 10 internal temperatures or the Reciever
     */
    transient protected ObservableList<Double> internalTemps;

    /**
     * Constructor that declares a Receiver with a default name of "Receiver"
     *
     * @param ID the UID of the Receiver
     */
    public Receiver(String ID) {
        internalTemps = FXCollections.<Double>observableArrayList();
        this.uid = ID;
        name = "Receiver";
    }

    /**
     * Constructor for declaring a Receiver with more parameters.
     *
     * @param ID the UID of the Receiver
     * @param name the name of the Receiver
     * @param type the type of the Receiver
     */
    public Receiver(String ID, String name, String type) {
        internalTemps = FXCollections.<Double>observableArrayList();
        this.uid = ID;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the UID of the Receiver
     *
     * @return the UID of the Receiver
     */
    public String getUID() {
        return uid;
    }

    /**
     * Returns the name of the Reciever
     *
     * @return the name of the Receiver
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Receiver
     * @param name the new name of the Receiver
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the array of GPIO pins
     * @return the array of GPIO pins
     */
    public GPIO[] getGPIO() {
        return gpio;
    }

    /**
     * Return the array of Sensors
     * @return the array of Sensors
     */
    public Sensor[] getSensors() {
        return sensors;
    }

    /**
     * Returns the last recorded internal temperature.
     */
    public double getInternalTemperatureValue() {
        if (internalTemps.isEmpty()) {
            return 0;
        }
        return internalTemps.get(0);
    }

    /**
     * Returns true if the UIDs are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Receiver) {
            Receiver r = (Receiver) o;
            return r.uid.equals(uid);
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns the connection thread of the Receiver
     *
     * @return ServerThread of the Receiver
     */
    public ServerThread getCurrentThread() {
        return currentThread;
    }

    /**
     * Sets the thead info of this Receiver.
     * <strong>Should also set the connected boolean to true.</strong>
     *
     * @param thread The thread to the Receiver to use
     * @param id The ID of the thread
     */
    public abstract void setThreadInfo(ServerThread thread, long id);

    /**
     * Returns the GUI element for the Receiver
     *
     * @return
     */
    public abstract Node getReceiverPane();

    public abstract int[] getValues();

    public abstract boolean setValue(int pin, int value);

    public abstract void addInternalTemperatureValue(double temperature);

    public abstract void addSensor(Sensor sensor, int pin);

    public abstract void removeSensor(Sensor sensor);

    public abstract Sensor getSensor(int pin);

    public abstract ArrayList<GPIO> getDigitalSensors();

    public abstract DigitalSensor getDigitalSensor(int pin);

    public abstract void sendCommand(String command);

}
