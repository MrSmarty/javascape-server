package com.javascape.menuPopups;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.javascape.Server;
import com.javascape.chronjob.Chronjob;
import com.javascape.chronjob.ChronManager;
import com.javascape.chronjob.ChronjobItem;
import com.javascape.recievers.Reciever;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewChronjobPopup {
    private ObservableList<String> observableCommands = FXCollections.observableArrayList();

    private ChronManager manager = Server.getDataHandler().getChronManager();

    public NewChronjobPopup() {
        Stage stage = new Stage();
        stage.setTitle("Create New Chronjob");

        GridPane g = new GridPane();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label periodLabel = new Label("Period:");
        TextField periodField = new TextField();

        ObservableList<TimeUnit> timeUnitList = FXCollections.observableArrayList();
        timeUnitList.addAll(TimeUnit.values());

        ChoiceBox<TimeUnit> timeUnits = new ChoiceBox<TimeUnit>(timeUnitList);
        timeUnits.valueProperty().set(TimeUnit.MINUTES);

        VBox commandBox = new VBox();
        HBox commandBoxHeader = new HBox();

        Button newCommand = new Button("New Command");
        newCommand.setOnAction(e -> {
            addCommand();
        });
        commandBoxHeader.getChildren().addAll(new Label("Commands:"), newCommand);

        commandBox.getChildren().add(commandBoxHeader);

        observableCommands.addListener((javafx.beans.Observable observable) -> {
            commandBox.getChildren().clear();
            commandBox.getChildren().add(commandBoxHeader);
            for (String command : observableCommands) {
                HBox temp = new HBox();

                Button delete = new Button();
                delete.getStyleClass().add("deleteButton");

                delete.setOnAction(e -> {
                    observableCommands.remove(command);
                });
                temp.getChildren().addAll(new Label(command), delete);
                commandBox.getChildren().addAll(temp);
            }
            stage.sizeToScene();
        });

        g.add(nameLabel, 0, 0);
        g.add(nameField, 1, 0);
        g.add(periodLabel, 0, 1);
        g.add(periodField, 1, 1);
        g.add(timeUnits, 2, 1);
        g.add(commandBox, 0, 2);

        Button create = new Button("Create Chronjob");
        Button cancel = new Button("Cancel");

        g.add(create, 0, 5);
        g.add(cancel, 1, 5);

        cancel.setOnAction(e -> {
            stage.close();
        });

        create.setOnAction(e -> {
            ArrayList<String> commands = new ArrayList<String>(observableCommands);

            Chronjob job = new Chronjob(nameField.textProperty().getValue(), commands,
                    Integer.parseInt(periodField.textProperty().getValue()), timeUnits.getValue());
            manager.newRepeating(job);
            stage.close();
        });

        Scene scene = new Scene(g);
        scene.getStylesheets().add("resources/Stylesheets/buttonStyles.css");

        stage.setScene(scene);

        stage.show();
    }

    public void addCommand() {
        Stage stage = new Stage();

        GridPane g = new GridPane();

        ChoiceBox<ChronjobItem> setType = new ChoiceBox<ChronjobItem>();
        setType.getItems().addAll(manager.getAllItems());
        setType.valueProperty().set(setType.getItems().get(0));

        Label infoLabel = new Label();
        TextField valueField = new TextField();
        Label infoLabelAfter = new Label();

        setType.setOnAction(e -> {
            infoLabel.textProperty().setValue(setType.getValue().getLabel());
            infoLabelAfter.textProperty().setValue(setType.getValue().getAfterLabel());
        });

        Label recieverLabel = new Label("Reciever");
        ChoiceBox<Reciever> recieverBox = new ChoiceBox<Reciever>();

        recieverBox.getItems().addAll(Server.getDataHandler().getRecieverHandler().getRecieverList());
        recieverBox.valueProperty().set(recieverBox.getItems().get(0));

        g.add(setType, 0, 0);
        g.add(infoLabel, 0, 1);
        g.add(valueField, 1, 1);
        g.add(infoLabelAfter, 2, 1);
        g.add(recieverLabel, 0, 2);
        g.add(recieverBox, 1, 2);

        Button add = new Button("Add");
        add.setOnAction(e -> {
            String command = setType.getValue().getCommand(valueField.textProperty().getValue()) + " - "
                    + recieverBox.valueProperty().get().getUID();
            observableCommands.add(command);
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            stage.close();
        });

        g.add(add, 0, 3);
        g.add(cancel, 1, 3);

        Scene s = new Scene(g);

        stage.setScene(s);

        stage.show();

    }
}
