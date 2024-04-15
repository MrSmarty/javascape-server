package com.javascape.sensors;

import com.javascape.Server;
import com.javascape.receivers.GPIO;
import com.javascape.receivers.Receiver;
import com.javascape.sensors.analog.Sensor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public final class SensorView {

    /**
     * List of all the sensors
     */
    public ObservableList<Node> sensorViewList = FXCollections.observableArrayList();

    /**
     * Creates a sensorView. This will update every time the method
     * getSensorView() is called. This method with also update when a new Sensor is made.
     */
    public SensorView() {
        update();
    }

    /**
     * Returns a ListView with all of the SensorPanes. Also updates the list before returning.
     * @return The ListView with all of the SensorPanes
     */
    public ListView<Node> getSensorView() {
        update();
        ListView<Node> view = new ListView<>(sensorViewList);
        return view;
    }

    /**
     * Updates the sensorView
     */
    public void update() {
        sensorViewList.clear();
        for (Receiver r : Server.getDataHandler().getReceiverHandler().getReceiverList()) {
            if (r != null) {
                for (Sensor s : r.getSensors()) {
                    if (s != null) {
                        sensorViewList.add(s.getSensorPane());
                    }
                }
                for (GPIO g : r.getDigitalSensors()) {
                    if (g.sensor != null) {
                        sensorViewList.add(g.sensor.getSensorPane());
                    }
                }
            }
        }

    }
}
