package com.javascape.sensors.analog;

import java.util.Arrays;

import com.javascape.Server;
import com.javascape.ServerGUI;
import com.javascape.receivers.GPIO;
import com.javascape.receivers.Receiver;
import com.javascape.sensors.SensorBase;
import com.javascape.sensors.digital.DigitalSensor;
import com.javascape.ui.EditableLabel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TDS extends Sensor {

    /**
     * The temperature to use to help compensate. Defaults to 25 degrees
     * Celsius.
     */
    public double temperature = 25.0;

    // The default temperature to use to help compensate. Defaults to 25 degrees Celsius.
    public double defaultTemperature = 25.0;

    public String tempSensorID = null;
    public int tempSensorIndex = -1;
    public int tempSensorValueIndex = -1;

    // The reference voltage to use to help compensate. Defaults to 3.3 volts.
    public double referenceVoltage = 3.3;

    public TDS(String receiverID, String name, int index) {
        super(receiverID, name, index);

        className = "TDS";
    }

    @Override
    public Node getSensorPane() {
        GridPane g = new GridPane();

        try {
            EditableLabel nameLabel = new EditableLabel(super.getName(), this,
                    super.getClass().getMethod("setName", String.class));
            g.add(nameLabel, 0, 0);
        } catch (NoSuchMethodException e) {
        }

        Label tempLabel = new Label("Temperature Sensor: ");
        ChoiceBox<Receiver> receiverBox = new ChoiceBox<>();
        ChoiceBox<SensorBase> sensorBox = new ChoiceBox<>();
        ChoiceBox<String> valueBox = new ChoiceBox<>();
        receiverBox.getItems().add(null);
        receiverBox.getItems().addAll(Server.getDataHandler().getReceiverHandler().getReceiverList());

        if (tempSensorID != null) {
            receiverBox.setValue(Server.getDataHandler().getReceiverHandler().getReceiver(receiverID));
        }

        if (tempSensorIndex != -1) {
            sensorBox.setValue(Server.getDataHandler().getReceiverHandler().getReceiver(tempSensorID).getSensor(tempSensorIndex));
        }

        if (tempSensorValueIndex != -1) {
            //valueBox.setValue(Server.getDataHandler().getReceiverHandler().getReceiver(tempSensorID).getSensor(tempSensorIndex).getValueNames()[0]);
        }

        receiverBox.setOnAction(e -> {
            if (receiverBox.getValue() != null) {
                tempSensorID = receiverBox.getValue().getUID();

                sensorBox.getItems().clear();
                for (GPIO gpio : receiverBox.getValue().getDigitalSensors()) {
                    sensorBox.getItems().add(gpio.getSensor());
                }
                sensorBox.getItems().addAll(Arrays.asList(receiverBox.getValue().getSensors()));
                sensorBox.setVisible(true);
            } else {
                tempSensorID = null;
                sensorBox.getItems().clear();
                sensorBox.setVisible(false);
                valueBox.getItems().clear();
                valueBox.setVisible(false);
            }

        });

        sensorBox.setOnAction(e -> {
            tempSensorIndex = sensorBox.getValue().getIndex();

            if (sensorBox.getValue() instanceof DigitalSensor digitalSensor) {
                valueBox.getItems().clear();
                for (int i = 0; i < digitalSensor.getNumValues(); i++) {
                    valueBox.getItems().add(digitalSensor.getValueNames()[i]);
                }
                valueBox.setVisible(true);
            } else {
                tempSensorIndex = -1;
                valueBox.setVisible(false);
            }

        });

        valueBox.setOnAction(e -> {
            System.out.println("Testing: " + ((DigitalSensor) sensorBox.getValue()).getNameOfValueAtIndex(valueBox.getValue()));
            tempSensorValueIndex = ((DigitalSensor) sensorBox.getValue()).getNameOfValueAtIndex(valueBox.getValue());
        });

        g.add(tempLabel, 0, 1);
        g.add(receiverBox, 1, 1);
        g.add(sensorBox, 2, 1);
        g.add(valueBox, 3, 1);

        g.add(new Label("Voltage: " + referenceVoltage), 0, 2);

        Label valueLabel = new Label(String.format("PPM: %f", calculatePPM()));

        if (valueList == null) {
            valueList = FXCollections.<String>observableArrayList();
        }

        valueList.addListener((ListChangeListener.Change<? extends String> change) -> {
            Platform.runLater(() -> {
                valueLabel.setText(String.format("PPM: %f", calculatePPM()));
            });
        });

        Button delete = new Button("Delete");

        delete.setOnAction(e -> {
            Server.getDataHandler().getReceiverHandler().getReceiver(receiverID).removeSensor(this);
            ServerGUI.getReceiverView().update();
        });

        g.add(valueLabel, 1, 0);

        g.add(delete, 2, 0);

        return g;
    }

//   /** Calculates the PPM of the sensor */
    public double calculatePPM() {

        if (getCurrentRawAsDouble() == null) {
            return -1.0;
        }

        temperature = defaultTemperature;

        if (receiverID != null && tempSensorIndex != -1) {
            Receiver receiver = Server.getDataHandler().getReceiverHandler().getReceiver(receiverID);
            if (receiver != null) {
                if (tempSensorIndex >= receiver.getGPIO().length) {
                    SensorBase sensor = receiver.getSensor(tempSensorIndex);
                    //System.out.println("Sensor is " + sensor.getClass().getName());
                    temperature = Double.parseDouble(sensor.getValue());
                    //System.out.println("Using temperature: " + temperature);
                } else {
                    SensorBase sensor = receiver.getDigitalSensor(tempSensorIndex);
                    //System.out.println("Sensor is digital & " + sensor.getClass().getName());
                    if (sensor instanceof DigitalSensor digitalSensor) {
                        if (tempSensorValueIndex != -1) {
                            temperature = Double.parseDouble(digitalSensor.getValue(tempSensorValueIndex));
                            //System.out.println("Using temperature: " + temperature);
                        }
                    }
                }

            }
        }

        //System.out.println("Calculating PPM");
        double voltage = (getCurrentRawAsDouble() * referenceVoltage / maxCal);// * (double) referenceVoltage / 1024.0;
        // temperature compensation formula: fFinalResult(25^C) =
        // fFinalResult(current)/(1.0+0.02*(fTP-25.0));
        double compensationCoefficient = 1.0 + 0.02 * (temperature - 25.0);
        // temperature compensation
        double compensationVoltage = voltage / compensationCoefficient;

        double ppm = (133.42 * compensationVoltage * compensationVoltage * compensationVoltage
                - 255.86 * compensationVoltage * compensationVoltage + 857.39 * compensationVoltage) * 0.5;

        //System.out.println("PPM: " + ppm);
        return ppm;
    }

    /**
     * Sets the voltage used for the calculations
     *
     * @param newVoltage The new voltage to use
     */
    public void setVoltage(double newVoltage) {
        referenceVoltage = newVoltage;

    }

}
