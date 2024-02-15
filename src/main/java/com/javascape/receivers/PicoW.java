package com.javascape.receivers;

import com.javascape.Logger;
import com.javascape.ServerThread;
import com.javascape.menuPopups.AddSensorPopup;
import com.javascape.sensors.Sensor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PicoW extends Receiver {

    transient private CheckBox[] checkBoxes;

    private boolean gpioExpanded = false, sensorExpanded = false;

    public PicoW(String uid) {
        super(uid, "Pico W", "PiPicoW");
        values = new int[26];
        sensors = new Sensor[3];
    }

    public PicoW(String uid, String name, String type) {
        super(uid, name, type);
        values = new int[26];
        sensors = new Sensor[3];
    }

    public GridPane getReceiverPane() {
        if (checkBoxes == null)
            checkBoxes = new CheckBox[26];
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

        for (int i = 0; i < 26; i++) {
            int tempI = i;
            Label l = new Label("GPIO: " + i);
            checkBoxes[i] = new CheckBox();

            checkBoxes[i].selectedProperty().set(values[i] == 1 ? true : false);
            if (i < 13) {
                buttonPane.add(l, 0, i + 1);
                buttonPane.add(checkBoxes[i], 1, i + 1);
            } else {
                buttonPane.add(l, 2, i + 1 - 13);
                buttonPane.add(checkBoxes[i], 3, i + 1 - 13);
            }

            checkBoxes[i].disableProperty().set(!connected);

            checkBoxes[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    Logger.print(
                            String.format("setPin %d %d", tempI, checkBoxes[tempI].selectedProperty().get() ? 1 : 0));
                    sendCommand(
                            String.format("setPin %d %d", tempI, checkBoxes[tempI].selectedProperty().get() ? 1 : 0));
                    values[tempI] = checkBoxes[tempI].selectedProperty().get() ? 1 : 0;
                }
            });
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
        for (int i = 0; i < checkBoxes.length; i++) {
            values[i] = checkBoxes[i].selectedProperty().getValue() == true ? 1 : 0;
        }
        return values;
    }

    public boolean setValue(int pin, int value) {
        checkBoxes[pin].selectedProperty().set(value == 1);
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

    private void sendCommand(String message) {
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

}
