package com.javascape.sensors;

import com.javascape.Server;
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

    private int maxCal = 48571;
    private int minCal = 18260;

    public CapacitiveV2(String recieverID, int index) {
        super(recieverID, "Capacitive V2", index);
        className = "CapacitiveV2";
    }

    public CapacitiveV2(String recieverID, String name, int index) {
        super(recieverID, name, index);
        className = "CapacitiveV2";
    }

    public void addValue(String value) {
        valueList.add(0, Double.parseDouble(value));
        if (valueList.size() > 10)
            valueList.remove(10);
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
            Server.getDataHandler().getRecieverHandler().getReciever(recieverID).removeSensor(this);
            Server.getGUI().getRecieverView().update();
        });

        g.add(valueLabel, 1, 0);

        g.add(delete, 1, 1);

        return g;
    }

    private String getCurrentValue() {
        if (valueList == null)
            valueList = FXCollections.<Double>observableArrayList();
        if (valueList.size() > 0) {
            double percent = (valueList.get(0) - maxCal) / (minCal - maxCal) * 100;
            return String.format("%.2f", percent);
        }
        return "N/A";
    }

    public Double getCurrentValueAsDouble() {
        if (valueList.size() > 0) {
            double percent = (valueList.get(0) - maxCal) / (minCal - maxCal) * 100;
            return percent;
        }
        return null;
    }

}
