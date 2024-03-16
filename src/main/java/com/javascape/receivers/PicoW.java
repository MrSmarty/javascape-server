package com.javascape.receivers;

import java.util.ArrayList;

import com.javascape.Logger;
import com.javascape.ServerThread;
import com.javascape.menuPopups.AddSensorPopup;
import com.javascape.sensors.analog.Sensor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PicoW extends Receiver {

    transient private boolean gpioExpanded = false, sensorExpanded = false;

    public PicoW(String uid) {
        super(uid, "Pico W", "PiPicoW");
        gpio = new GPIO[23];
        sensors = new Sensor[3];
    }

    public PicoW(String uid, String name, String type) {
        super(uid, name, type);
        gpio = new GPIO[23];
        sensors = new Sensor[3];
    }

    public GridPane getReceiverPane() {
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

        g.add(new Label("UID: " + super.getUID()), 1, 0);
        g.add(new Label("Temperature: "), 2, 0);
        tempLabel = new Label();
        if (internalTemps == null)
            internalTemps = FXCollections.<Double>observableArrayList();
        internalTemps.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    tempLabel.setText(String.format("%.2f˚ Celcius", getInternalTemperatureValue()));
                }
            });
        });

        g.add(tempLabel, 3, 0);

        GridPane buttonPane = new GridPane();

        for (int i = 0; i < gpio.length; i++) {
            if (gpio[i] == null)
                gpio[i] = new GPIO(uid, i);

            if (i < gpio.length/2) {
                buttonPane.add(gpio[i].getUI(), 0, i);
            } else {
                buttonPane.add(gpio[i].getUI(), 1, i - gpio.length/2);
            }

            gpio[i].setConnectionStatus(connected);

        }
        TitledPane gpioPane = new TitledPane("GPIO", buttonPane);
        gpioPane.expandedProperty().set(gpioExpanded);

        gpioPane.setOnMouseClicked(e -> {
            gpioExpanded = gpioPane.expandedProperty().get();
        });

        g.add(gpioPane, 0, 1);

        VBox sensorVBox = new VBox();

        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i] != null) {
                sensorVBox.getChildren().add(sensors[i].getSensorPane());
            } else {
                int tempI = i;
                HBox h = new HBox();
                h.getChildren().add(new Label("ADC " + i + " is empty"));
                Button addSensorButton = new Button("Add Sensor");
                h.getChildren().add(addSensorButton);

                addSensorButton.setOnAction(e -> {
                    new AddSensorPopup(this, tempI);
                });

                sensorVBox.getChildren().add(h);
            }
        }

        TitledPane sensorPane = new TitledPane("Sensors", sensorVBox);
        sensorPane.expandedProperty().set(sensorExpanded);

        sensorPane.setOnMouseClicked(e -> {
            sensorExpanded = sensorPane.expandedProperty().get();
        });

        g.add(sensorPane, 0, 2);

        return g;
    }

    public ServerThread getCurrentThread() {
        return currentThread;
    }

    public int[] getValues() {
        int[] values = new int[gpio.length];
        for (int i = 0; i < gpio.length; i++) {
            values[i] = gpio[i].value;
        }
        return values;
    }

    public boolean setValue(int pin, int value) {
        gpio[pin].setValue(value);
        return true;
    }

    public void setThreadInfo(ServerThread thread, long id) {
        this.currentThread = thread;
        if (thread != null) {
            Logger.print("Set the pico threadinfo | " + thread.getName());
            connected = true;
        } else {
            connected = false;
        }

    }

    public void sendCommand(String message) {
        if (currentThread == null)
            return;
        Logger.print("Sending Command to " + currentThread.getName());
        currentThread.addCommand(message);
    }

    public void addInternalTemperatureValue(double temperature) {
        internalTemps.add(0, temperature);
        if (internalTemps.size() > 10) {
            internalTemps.remove(internalTemps.size() - 1);
        }
    }

    public void addSensor(Sensor sensor, int pin) {
        sensors[pin] = sensor;
    }

    public void removeSensor(Sensor sensor) {
        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i] == sensor) {
                sensors[i] = null;
                return;
            }
        }
    }

    public Sensor getSensor(int pin) {
        return sensors[pin];
    }

    public Sensor[] getSensors() {
        return sensors;
    }

    public ArrayList<GPIO> getDigitalSensors() {
       ArrayList<GPIO> digitalSensors = new ArrayList<GPIO>();
       System.out.println("Getting digital sensors");
       for (GPIO g : gpio) {
           if (g.sensor != null) {
               digitalSensors.add(g);
           }
       }
       System.out.println(digitalSensors.size());

       return digitalSensors;
    }

}
