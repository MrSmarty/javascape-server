package com.javascape.sensors.analog;

import com.javascape.Helper;
import com.javascape.Server;
import com.javascape.ServerGUI;
import com.javascape.Settings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CapacitiveV2 extends Sensor {

    transient ObservableList<Double> valueList = FXCollections.observableArrayList();

    public int maxCal = 22000;
    public int minCal = 10500;


    public CapacitiveV2(String receiverID, int index) {
        super(receiverID, "Capacitive V2", index);
        this.className = "CapacitiveV2";
    }

    public CapacitiveV2(String receiverID, String name, int index) {
        super(receiverID, name, index);
        this.className = "CapacitiveV2";
    }

    public void addValue(String value) {
        valueList.add(0, Double.parseDouble(value));
        if (valueList.size() > Settings.maxSensorData)
            valueList.remove(Settings.maxSensorData);
    }

    public GridPane getSensorPane() {
        GridPane g = new GridPane();

        Label nameLabel = new Label(super.getName());
        TextField nameField = new TextField(super.getName());
        nameField.visibleProperty().set(false);
        nameLabel.cursorProperty().setValue(Cursor.HAND);

        nameField.setOnAction(e -> {
            nameLabel.setText(nameField.getText());
            nameField.visibleProperty().set(false);
            nameLabel.visibleProperty().set(true);
            super.setName(nameField.getText());
        });

        nameLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    nameField.visibleProperty().set(true);
                    nameLabel.visibleProperty().set(false);
                }
            }
        });

        g.add(nameLabel, 0, 0);
        g.add(nameField, 0, 0);

        Label valueLabel = new Label(String.format("Moisture: %s%%", getCurrentValue()));

        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        valueList.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    valueLabel.setText(String.format("Moisture: %s%%", getCurrentValue()));
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
            double percent = Helper.convertToPercentage(valueList.get(0), maxCal, minCal);
            // double percent = (valueList.get(0) - maxCal) / (minCal - maxCal) * 100;
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    public Double getCurrentValueAsDouble() {
        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        if (valueList.size() > 0) {
            double percent = Helper.convertToPercentage(valueList.get(0), maxCal, minCal);
            return percent;
        }
        return null;
    }

}
