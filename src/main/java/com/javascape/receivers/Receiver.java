package com.javascape.receivers;

import com.javascape.ServerThread;
import com.javascape.sensors.Sensor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class Receiver {
    /** Unique ID of the Receiver. */
    protected String uid;

    /** Name of the Receiver */
    protected String name;

    /** Specifies the type of Receiver */
    protected String type;

    /** The Household that this Receiver is a part of */
    protected int householdID;

    protected int[] values;

    /** Sensor array lol */
    protected Sensor[] sensors;

    /** Boolean to tell whether or not the receiver is connected */
    transient protected boolean connected;
    transient protected ServerThread currentThread;

    transient protected Label tempLabel;
    transient protected ObservableList<Double> internalTemps;

    public Receiver(String ID) {
        internalTemps = FXCollections.<Double>observableArrayList();
        this.uid = ID;
        name = "Receiver";
    };

    public Receiver(String ID, String name, String type) {
        internalTemps = FXCollections.<Double>observableArrayList();
        this.uid = ID;
        this.name = name;
        this.type = type;
    }

    public String getUID() {
        return uid;
    };

    public String getName() {
        return name;
    };

    public void setName(String name) {
        this.name = name;
    };

    @Override
    public String toString() {
        return getName();
    }
    
    /**
     * Returns the connection thread of the Receiver
     * @return ServerThread of the Receiver
     */
    public abstract ServerThread getCurrentThread();

    /** Sets the thead info of this Receiver.
     *  <strong>Should also set the connected boolean to true.</strong>
     * @param thread The thread to the Receiver to use
     * @param id The ID of the thread
     */
    public abstract void setThreadInfo(ServerThread thread, long id);

    public abstract GridPane getReceiverPane();

    public abstract int[] getValues();

    public abstract boolean setValue(int pin, int value);

    public abstract void addInternalTemperatureValue(double temperature);

    public abstract void addSensor(Sensor sensor, int pin);

    public abstract void removeSensor(Sensor sensor);

    public abstract Sensor getSensor(int pin);

    public abstract Sensor[] getSensors();

    public double getInternalTemperatureValue() {
        if (internalTemps.size() == 0) {
            return 0;
        }
        return internalTemps.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Receiver) {
            Receiver r = (Receiver) o;
            return r.uid.equals(uid);
        }
        return false;
    }
}
