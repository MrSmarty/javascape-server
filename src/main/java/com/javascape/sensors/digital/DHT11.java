package com.javascape.sensors.digital;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DHT11 extends DigitalSensor {

    public DHT11(String receiverID, int index) {
        super(receiverID, index);
        name = "DHT11";
        className = "DHT11";

        // This sets up the values of the sensor
        numValues = 2;
        valueNames = new String[2];
        valueNames[0] = "Humidity";
        valueNames[1] = "Temperature";
    }

    @Override
    public GridPane getSensorPane() {

        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }

        String[] currentValue;

        try {
            currentValue = getValue().split("\\|");
        } catch (NullPointerException e) {
            currentValue = new String[2];
            currentValue[0] = "N/A";
            currentValue[1] = "N/A";
        }

        GridPane pane = new GridPane();

        Label nameLabel = new Label(super.getName());
        pane.add(nameLabel, 0, 0);

        Label tempLabel = new Label("Temperature: ");
        Label tempValue = new Label(currentValue[1]);

        pane.add(tempLabel, 0, 1);
        pane.add(tempValue, 1, 1);

        Label humidityLabel = new Label("Humidity: ");
        Label humidityValue = new Label(currentValue[0]);

        pane.add(humidityLabel, 2, 1);
        pane.add(humidityValue, 3, 1);

        valueList.addListener((Change<? extends String> c) -> {
            String[] currentValue1 = getValue().split(DELIMITER);
            tempValue.setText(currentValue1[1]);
            humidityValue.setText(currentValue1[0]);
        });

        return pane;
    }

    @Override
    public String getCommand() {
        return "DHT11";
    }

}
