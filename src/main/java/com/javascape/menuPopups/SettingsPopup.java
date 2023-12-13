package com.javascape.menuPopups;

import java.util.concurrent.TimeUnit;

import com.javascape.Settings;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingsPopup {

    public SettingsPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Settings");

        GridPane g = new GridPane();

        Label dataFileLabel = new Label("Data File Location:");
        TextField dataFileField = new TextField(Settings.storageLocation);

        g.add(dataFileLabel, 0, 0);
        g.add(dataFileField, 1, 0);

        Label timeoutLabel = new Label("Timeout Duration:");
        TextField timeoutField = new TextField("" + Settings.timeOut);

        g.add(timeoutLabel, 0, 1);
        g.add(timeoutField, 1, 1);

        Label autoLoginLabel = new Label("Autologin:");
        CheckBox autoLoginCheckbox = new CheckBox();
        autoLoginCheckbox.selectedProperty().set(Settings.autoLogin);

        g.add(autoLoginLabel, 0, 2);
        g.add(autoLoginCheckbox, 1, 2);

        Label sensorDataPollingLabel = new Label("Sensor Data Polling Rate:");
        TextField sensorDataPollingField = new TextField("" + Settings.getSensorDataJobInterval);
        ChoiceBox<TimeUnit> sensorDataPollingUnits = new ChoiceBox<TimeUnit>(
                FXCollections.observableArrayList(TimeUnit.values()));

        g.add(sensorDataPollingLabel, 0, 3);
        g.add(sensorDataPollingField, 1, 3);
        g.add(sensorDataPollingUnits, 2, 3);

        Button save = new Button("Save");

        save.setOnAction(e -> {
            Settings.storageLocation = dataFileField.textProperty().getValue();
            Settings.timeOut = Integer.parseInt(timeoutField.textProperty().getValue());
            Settings.autoLogin = autoLoginCheckbox.selectedProperty().getValue();
            Settings.getSensorDataJobInterval = Integer.parseInt(sensorDataPollingField.textProperty().getValue());
            Settings.getSensorDataJobIntervalUnit = sensorDataPollingUnits.getValue();
            popupStage.close();
        });

        Button cancel = new Button("Cancel");

        cancel.setOnAction(e -> {
            popupStage.close();
        });

        g.add(save, 0, 4);
        g.add(cancel, 1, 4);

        Scene scene = new Scene(g);

        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        popupStage.setScene(scene);
        popupStage.show();
    }

}
