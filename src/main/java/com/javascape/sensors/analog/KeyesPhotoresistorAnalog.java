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

    public int maxCal = 65565;
    public int minCal = 0;

    public KeyesPhotoresistorAnalog(String receiverID, int index) {
        super(receiverID, "Analog photoresistor", index);
        className = "KeyesPhotoresistorAnalog";
    }

    public KeyesPhotoresistorAnalog(String receiverID, String name, int index) {
        super(receiverID, name, index);
        className = "KeyesPhotoresistorAnalog";
    }

    public void addValue(String value) {
        valueList.add(0, Double.parseDouble(value));
        if (valueList.size() > Settings.maxSensorData)
            valueList.remove(Settings.maxSensorData);
    }

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

        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        valueList.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    valueLabel.setText(String.format("Light Level: %s%%", getCurrentValue()));
                }
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

    private String getCurrentValue() {
        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        if (valueList.size() > 0) {
            //double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    public Double getCurrentValueAsDouble() {
        if (valueList.size() > 0) {
            //double percent = (valueList.get(0) - minCal) / (maxCal - minCal) * 100;
            double percent = Helper.convertToPercentage(valueList.get(0), minCal, maxCal);
            return percent;
        }
        return null;
    }

}