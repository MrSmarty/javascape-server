package com.javascape.sensors.digital;

import com.javascape.ui.EditableLabel;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * A simple class for defining on and off states digitally
 */
public class Input extends DigitalSensor {

    public Input(String receiverID, int index) {
        super(receiverID, index);

        name = "Input";
        className = "Input";

        numValues = 1;
        valueNames = new String[1];
        valueNames[0] = "State";
    }

    @Override
    public String getCommand() {
        return "input";
    }

    @Override
    public Node getSensorPane() {
        GridPane pane = new GridPane();

        if (valueList == null) {
            valueList = FXCollections.observableArrayList();
        }

        try {
            EditableLabel label = new EditableLabel(getName(), this,
                    super.getClass().getMethod("setName", String.class));
            pane.add(label, 0, 0);
        } catch (NoSuchMethodException e) {
        }

        String currentValue;

        try {
            currentValue = getValue();
        } catch (NullPointerException e) {
            currentValue = "N/A";
        }

        Label value = new Label("Value: " + currentValue);

        pane.add(value, 0, 1);

        valueList.addListener((ListChangeListener<? super String>) new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                value.setText("Value: " + getValue());
            }
        });

        return pane;
    }

}
