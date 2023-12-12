package com.javascape.recievers;

import com.javascape.ServerThread;
import com.javascape.sensors.Sensor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class Reciever {
    protected String uid;
    protected String name;
    protected String type;
    protected int householdID;
    protected Sensor[] sensors;
    transient protected Label tempLabel;
    transient protected ObservableList<Double> internalTemps;

    public Reciever(String ID) {
        internalTemps = FXCollections.<Double>observableArrayList();
        this.uid = ID;
        name = "Reciever";
    };

    public Reciever(String ID, String name, String type) {
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

    public abstract ServerThread getCurrentThread();

    public abstract void setThreadInfo(ServerThread thread, long id);

    public abstract GridPane getRecieverPane();

    public abstract int[] getValues();

    public abstract void setValue(int pin, int value);

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
}
