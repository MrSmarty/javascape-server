package com.javascape.sensors.analog;

import com.javascape.Helper;
import com.javascape.Server;
import com.javascape.ServerGUI;
import com.javascape.Settings;
import com.javascape.ui.EditableLabel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CapacitiveV2 extends Sensor {

    transient ObservableList<Double> valueList = FXCollections.observableArrayList();


    public CapacitiveV2(String receiverID, int index) {
        super(receiverID, "Capacitive V2", index);
        this.className = "CapacitiveV2";

        maxCal = 22000;
        minCal = 10500;
    }

    public CapacitiveV2(String receiverID, String name, int index) {
        super(receiverID, name, index);
        this.className = "CapacitiveV2";

        maxCal = 22000;
        minCal = 10500;
    }

    @Override
    public void addValue(String value) {
        valueList.add(0, Double.valueOf(value));
        if (valueList.size() > Settings.maxSensorData)
            valueList.remove(Settings.maxSensorData);
    }

    @Override
    public GridPane getSensorPane() {
        GridPane g = new GridPane();

        try {
            EditableLabel nameLabel = new EditableLabel(super.getName(), this,
                    super.getClass().getMethod("setName", String.class));
            g.add(nameLabel, 0, 0);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Label valueLabel = new Label(String.format("Moisture: %s%%", getCurrentValue()));

        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        valueList.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(() -> {
                valueLabel.setText(String.format("Moisture: %s%%", getCurrentValue()));
            });
        });

        Button delete = new Button("Delete");

        delete.setOnAction(e -> {
            Server.getDataHandler().getReceiverHandler().getReceiver(receiverID).removeSensor(this);
            ServerGUI.getReceiverView().update();
        });

        g.add(valueLabel, 1, 0);

        g.add(delete, 1, 1);

        return g;
    }

    /**
     * Returns the current value as a string.
     * 
     * @return the current value as a string
     */
    @Override
    public String getCurrentValue() {
        if (valueList == null)
            valueList = FXCollections.observableArrayList();
        if (!valueList.isEmpty()) {
            double percent = Helper.convertToPercentage(valueList.get(0), maxCal, minCal);
            // double percent = (valueList.get(0) - maxCal) / (minCal - maxCal) * 100;
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    /**
     * Returns the current value as a double instead of a String.
     * @return the current value as a double
     */
    @Override
    public Double getCurrentValueAsDouble() {
        if (valueList == null)
            valueList = FXCollections.observableArrayList();
        if (!valueList.isEmpty()) {
            double percent = Helper.convertToPercentage(valueList.get(0), maxCal, minCal);
            return percent;
        }
        return null;
    }

}
