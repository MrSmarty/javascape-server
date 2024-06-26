package com.javascape.receivers;

import java.util.ArrayList;

import com.javascape.Logger;
import com.javascape.ServerThread;
import com.javascape.menuPopups.AddSensorPopup;
import com.javascape.sensors.analog.Sensor;
import com.javascape.sensors.digital.DigitalSensor;
import com.javascape.ui.EditableLabel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PiPicoW extends Receiver {

    transient private boolean gpioExpanded = false, sensorExpanded = false;

    public PiPicoW(String uid) {
        super(uid, "Pico W", "PiPicoW");
        gpio = new GPIO[23];
        sensors = new Sensor[3];
    }

    public PiPicoW(String uid, String name, String type) {
        super(uid, name, type);
        gpio = new GPIO[23];
        sensors = new Sensor[3];
    }

    @Override
    public GridPane getReceiverPane() {
        GridPane g = new GridPane();

        try {
            EditableLabel nameLabel = new EditableLabel(super.getName(), this,
                    this.getClass().getMethod("setName", String.class));
            g.add(nameLabel, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.add(new Label("UID: " + super.getUID()), 1, 0);
        g.add(new Label("Temperature: "), 2, 0);
        tempLabel = new Label();
        if (internalTemps == null) {
            internalTemps = FXCollections.<Double>observableArrayList();
        }
        internalTemps.addListener((ListChangeListener.Change<? extends Double> change) -> {
            Platform.runLater(() -> {
                tempLabel.setText(String.format("%.2f˚ Celcius", getInternalTemperatureValue()));
            });
        });

        g.add(tempLabel, 3, 0);

        GridPane buttonPane = new GridPane();

        for (int i = 0; i < gpio.length; i++) {
            if (gpio[i] == null) {
                gpio[i] = new GPIO(uid, i);
            }

            if (i < gpio.length / 2) {
                buttonPane.add(gpio[i].getUI(), 0, i);
            } else {
                buttonPane.add(gpio[i].getUI(), 1, i - gpio.length / 2);
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

    /**
     * @return An int array with the values of all the GPIO pins.
     */
    @Override
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
        if (currentThread == null) {
            return;
        }
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
        ArrayList<GPIO> digitalSensors = new ArrayList<>();
        System.out.println("Getting digital sensors");
        for (GPIO g : gpio) {
            if (g.sensor != null) {
                digitalSensors.add(g);
            }
        }
        System.out.println(digitalSensors.size());

        return digitalSensors;
    }

    /**
     * @param pin The pin of the Digital Sensor to return
     * @return The Digital Sensor at the specified pin
     */
    @Override
    public DigitalSensor getDigitalSensor(int pin) {
        return gpio[pin].sensor;
    }

}
