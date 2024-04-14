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

public class KeyesPhotoresistorAnalog extends Sensor {

    transient ObservableList<Double> valueList = FXCollections.observableArrayList();

    public KeyesPhotoresistorAnalog(String receiverID, int index) {
        super(receiverID, "Analog photoresistor", index);
        className = "KeyesPhotoresistorAnalog";

        maxCal = 65565;
        minCal = 0;
    }

    public KeyesPhotoresistorAnalog(String receiverID, String name, int index) {
        super(receiverID, name, index);
        className = "KeyesPhotoresistorAnalog";

        maxCal = 65565;
        minCal = 0;
    }

    @Override
    public void addValue(String value) {
        valueList.add(0, Double.valueOf(value));
        if (valueList.size() > Settings.maxSensorData) {
            valueList.remove(Settings.maxSensorData);
        }
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

        Label valueLabel = new Label(String.format("Light Level: %s%%", getCurrentValue()));

        if (valueList == null) {
            valueList = FXCollections.<Double>observableArrayList();
        }
        valueList.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(() -> {
                valueLabel.setText(String.format("Light Level: %s%%", getCurrentValue()));
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
     * This method will return the current value as a String.
     *
     * @return the current value as a String
     */
    private String getCurrentValue() {
        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }
        if (!valueList.isEmpty()) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    /**
     * This method will return the current value as a double instead of a
     * String.
     * @return the current value as a double
     */
    @Override
    public Double getCurrentValueAsDouble() {
        if (!valueList.isEmpty()) {
            // double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return percent;
        }
        return null;
    }

}
