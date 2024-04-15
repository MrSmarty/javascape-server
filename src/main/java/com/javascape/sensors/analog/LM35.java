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

public class LM35 extends Sensor {

    transient ObservableList<Double> valueList = FXCollections.observableArrayList();


    public LM35(String receiverID, int index) {
        super(receiverID, "LM35", index);
        this.className = "LM35";
    }

    public LM35(String receiverID, String name, int index) {
        super(receiverID, name, index);
        this.className = "LM35";
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

        Label valueLabel = new Label(String.format("Temperature: %sC", getCurrentValue()));

        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        valueList.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(() -> {
                valueLabel.setText(String.format("Temperature: %sC", getCurrentValue()));
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
            double value = valueList.get(0) * .00005 * 100;
            // double percent = (valueList.get(0) - maxCal) / (minCal - maxCal) * 100;
            return String.format("%.2f", value);
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
            double value = valueList.get(0) * .00005 * 100;
            return Math.round(value * 100.0) / 100.0;
        }
        return null;
    }

}
