package com.javascape.sensors.digital;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Flowmeter extends DigitalSensor {

    public Flowmeter(String receiverID, int index) {
        super(receiverID, index);

        name = "Flowmeter";
        className = "Flowmeter";

        numValues = 1;
        valueNames = new String[1];
        valueNames[0] = "Flowrate";
    }

    @Override
    public String getCommand() {
        return "counter";
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

        Label temperature = new Label("Flow Rate: " + currentValue + " L/min");

        pane.add(temperature, 0, 0);

        valueList.addListener((ListChangeListener<? super String>) new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                temperature.setText("Flow Rate: " + getValue() + " L/min");
            }
        });

        return pane;
    }

    @Override 
    public String getValue() {
        if (valueList == null || valueList.isEmpty()) {
            return "N/A";
        }
        /** Milliliters per pulse */
        double mlPerPulse = 0.44;
        System.out.println(valueList.get(0));
        /** Pulses * mlPerPulse to get ml, * 1000 for Liters*/
        double flowRate = Double.parseDouble(valueList.get(0)) * mlPerPulse / 1000 * 60;
        return String.format("%.3f", flowRate);
    }

}