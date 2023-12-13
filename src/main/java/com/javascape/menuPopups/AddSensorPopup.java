package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.recievers.Reciever;
import com.javascape.sensors.SensorManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddSensorPopup {
    public AddSensorPopup(Reciever reciever, int index) {
        Stage popupStage = new Stage();
        

        GridPane g = new GridPane();

        SensorManager.getClassMap();
        ChoiceBox<String> dropdown = new ChoiceBox<String>(SensorManager.getSensorList());

        g.add(dropdown, 0, 0, 1, 1);

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");

        save.setOnAction(e -> {
            reciever.addSensor(SensorManager.createNewSensor(dropdown.getValue(), reciever.getUID(), index), index);
            Server.getGUI().getRecieverView().update();
            popupStage.close();
        });

        cancel.setOnAction(e -> {
            popupStage.close();
        });

        g.add(save, 0, 1);
        g.add(cancel, 1, 1);

        Scene scene = new Scene(g);
        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        popupStage.setScene(scene);

        popupStage.show();
    }
}
