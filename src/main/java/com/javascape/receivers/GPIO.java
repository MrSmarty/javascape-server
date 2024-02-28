package com.javascape.receivers;

import com.javascape.Logger;
import com.javascape.Server;
import com.javascape.sensors.SensorManager;
import com.javascape.sensors.digital.DigitalSensor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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

    private transient Label sensorValueLabel;

    public DigitalSensor sensor;

    public GPIO(String uid, int index) {

        checkBox = new CheckBox();

        this.index = index;

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Logger.print(String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
                Server.getDataHandler().getReceiverHandler().getReceiver(uid).sendCommand(String.format("setPin %d %d", index, checkBox.selectedProperty().get() ? 1 : 0));
            }
        });
    }

    public void setValue(int value) {
        this.value = value;
        if (value >= 0) {
            checkBox.selectedProperty().set(value==1);
            checkBox.setDisable(false);
        } else {
            checkBox.setDisable(true);
        }

    }

    public void setConnectionStatus(boolean connected) {
        if (connected && value >= 0) {
            checkBox.disableProperty().set(false);
        } else {
            checkBox.disableProperty().set(true);
        }
    }

    public HBox getUI() {
        HBox hbox = new HBox();

        hbox.getChildren().addAll(new Label("GPIO "+index+":"), checkBox);

        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    editWindow();
                }
            }
        });

        return hbox;
    }

    public void editWindow() {
        Stage popup = new Stage();
        popup.setTitle("Edit GPIO "+index);
        GridPane g = new GridPane();

        Label nameLabel = new Label("GPIO "+index);
        g.add(nameLabel, 0, 0);

        ComboBox<String> mode = new ComboBox<String>();
        mode.getItems().addAll("Output", "Input");

        ComboBox<String> device = new ComboBox<String>();
        device.getItems().addAll("None", "DHT11");
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

        if (value >= 0) {
            mode.getSelectionModel().select("Output");
        } else {
            mode.getSelectionModel().select("Input");
        }

        g.add(mode, 0, 1);
        g.add(device, 0, 2);

        Button saveButton = new Button("Save");

        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            if (mode.getSelectionModel().getSelectedItem().equals("Output")) {
                setValue(0);
                sensor=null;
            } else {
                setValue(-1);
                setSensor(SensorManager.createNewDigitalSensor(device.getSelectionModel().getSelectedItem(), index));
            }
            System.out.println("Sensor: "+ sensor);
            popup.close();
        });

        cancelButton.setOnAction(e -> {
            popup.close();
        });

        g.add(saveButton, 0, 3);
        g.add(cancelButton, 1, 3);

        Scene scene = new Scene(g);

        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());


        popup.setScene(scene);
        popup.show();

    }

    public void setSensor(DigitalSensor s) {
        this.sensor = s;
        System.out.println("Set the sensor: " + s);
    }

    public DigitalSensor getSensor() {
        return sensor;
    }

    /**
     * Get the command to set the GPIO.
     * @return
     */
    public String getCommand() {
        if (sensor != null)
            return index + " " + sensor.getCommand();
        return null;
    }

}
