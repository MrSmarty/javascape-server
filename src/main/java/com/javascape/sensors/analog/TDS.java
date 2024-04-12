package com.javascape.sensors.analog;

import com.javascape.Server;
import com.javascape.ServerGUI;
import com.javascape.ui.EditableLabel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TDS extends Sensor {

    /**
     * The temperature to use to help compensate. Defaults to 25 degrees Celsius.
     */
    public double temperature = 25.0;

    private double referenceVoltage = 3.3;

    public TDS(String receiverID, String name, int index) {
        super(receiverID, name, index);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Node getSensorPane() {
        GridPane g = new GridPane();

        try {
            EditableLabel nameLabel = new EditableLabel(super.getName(), this,
                    super.getClass().getMethod("setName", String.class));
            g.add(nameLabel, 0, 0);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Label valueLabel = new Label(String.format("PPM: %f", calculatePPM()));

        if (valueList == null)
            valueList = FXCollections.<String>observableArrayList();
            
        valueList.addListener((ListChangeListener.Change<? extends String> change) -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    valueLabel.setText(String.format("PPM: %f", calculatePPM()));
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

    public double calculatePPM() {

        if (getCurrentRawAsDouble() == null)
            return -1.0;

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

}
