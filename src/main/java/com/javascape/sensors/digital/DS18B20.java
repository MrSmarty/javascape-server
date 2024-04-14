package com.javascape.sensors.digital;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DS18B20 extends DigitalSensor {

    public DS18B20(String receiverID, int index) {
        super(receiverID, index);
        name = "DS18B20";
        className = "DS18B20";

        // This sets up the values of the sensor
        numValues = 1;
        valueNames = new String[1];
        valueNames[0] = "Temperature";
    }

    @Override
    public String getCommand() {
        return "DS18B20";
    }

    @Override
    public Node getSensorPane() {

        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }

        GridPane pane = new GridPane();

        String currentValue;

        try {
            currentValue = getValue();
        } catch (NullPointerException e) {
            currentValue = "N/A";
        }

        Label temperature = new Label("Temperature: " + currentValue);

        pane.add(temperature, 0, 0);

        valueList.addListener((ListChangeListener<? super String>) new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                temperature.setText("Temperature: " + getValue());
            }
        });

        return pane;
    }

}
