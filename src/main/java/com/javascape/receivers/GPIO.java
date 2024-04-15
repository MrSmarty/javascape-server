package com.javascape.receivers;

import com.javascape.Logger;
import com.javascape.Server;
import com.javascape.ServerGUI;
import com.javascape.sensors.SensorManager;
import com.javascape.sensors.digital.DigitalSensor;
import com.javascape.ui.EditableLabel;

import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GPIO {

    public int value = 0;

    public int index;

    private transient CheckBox checkBox;
    private transient Label checkBoxLabel;

    String uid;

    public DigitalSensor sensor = null;

    public GPIO(String uid, int index) {
        this.uid = uid;

        checkBox = new CheckBox();

        this.index = index;
        checkBoxLabel = new Label("GPIO " + index + ":");

        checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Logger.print(String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
            Server.getDataHandler().getReceiverHandler().getReceiver(uid)
                    .sendCommand(String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
        });
    }

    public void setValue(int value) {
        System.out.println("Setting value: " + value);
        this.value = value;
        if (value >= 0) {
            checkBox.selectedProperty().set(value == 1);
            checkBox.setDisable(false);
        } else {
            checkBox.setDisable(true);
        }

    }

    public void setConnectionStatus(boolean connected) {
        if (checkBox == null) {
            checkBox = new CheckBox();
        }
        if (connected && value >= 0) {
            checkBox.disableProperty().set(false);
        } else {
            checkBox.disableProperty().set(true);
        }
    }

    public HBox getUI() {
        HBox hbox = new HBox();

        if (value >= 0 || sensor == null) {
            if (checkBoxLabel == null) {
                checkBoxLabel = new Label("GPIO " + index + ":");
            }
            if (checkBox == null) {
                checkBox = new CheckBox();
                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    Logger.print(String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
                    Server.getDataHandler().getReceiverHandler().getReceiver(uid).sendCommand(
                            String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
                });
            }
            hbox.getChildren().addAll(checkBoxLabel, checkBox);
        } else {
            hbox.getChildren().addAll(sensor.getSensorPane());
        }

        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                editWindow();
            }
        });

        return hbox;
    }

    public void editWindow() {
        Stage popup = new Stage();
        popup.setTitle("Edit GPIO " + index);
        GridPane g = new GridPane();

        Label gpioLabel = new Label("GPIO " + index);
        g.add(gpioLabel, 0, 0);

        try {
            EditableLabel nameLabel = new EditableLabel(sensor.getName(), sensor,
                    sensor.getClass().getMethod("setName", String.class));
            g.add(nameLabel, 0, 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        ComboBox<String> mode = new ComboBox<>();
        mode.getItems().addAll("Output", "Input");

        ComboBox<String> device = new ComboBox<>();
        device.getItems().add("None");
        device.getItems().addAll(SensorManager.getDigitalSensorList());
        device.visibleProperty().set(false);

        mode.setOnAction(e -> {
            if (mode.getSelectionModel().getSelectedItem().equals("Input")) {
                device.visibleProperty().set(true);
                device.getSelectionModel().select("None");
            } else {
                device.visibleProperty().set(false);
            }
        });

        if (value >= 0 || sensor == null) {
            mode.getSelectionModel().select("Output");
        } else {
            mode.getSelectionModel().select("Input");
            device.visibleProperty().set(true);
            device.getSelectionModel().select(sensor.className);

        }

        g.add(mode, 0, 2);
        g.add(device, 0, 3);

        Button saveButton = new Button("Save");

        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            if (mode.getSelectionModel().getSelectedItem().equals("Output")) {
                setValue(0);
                sensor = null;
            } else {
                setValue(-1);
                if (sensor != null && sensor.className.equals(device.getSelectionModel().getSelectedItem())) {
                } else {
                    setSensor(SensorManager.createNewDigitalSensor(device.getSelectionModel().getSelectedItem(), index));
                }
            }
            System.out.println("Sensor: " + sensor);
            ServerGUI.getReceiverView().update();
            popup.close();
        });

        cancelButton.setOnAction(e -> {
            popup.close();
        });

        g.add(saveButton, 0, 4);
        g.add(cancelButton, 1, 4);

        Scene scene = new Scene(g);

        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        popup.setScene(scene);
        popup.show();

    }

    public void setSensor(DigitalSensor s) {
        this.sensor = s;
        System.out.println("Set the sensor: " + s.name);
    }

    public DigitalSensor getSensor() {
        return sensor;
    }

    public void removeSensor() {
        sensor = null;
        setValue(0);
    }

    /**
     * Get the command to set the GPIO.
     *
     * @return
     */
    public String getCommand() {
        if (sensor != null) {
            return index + " " + sensor.getCommand();
        }
        return null;
    }

}
