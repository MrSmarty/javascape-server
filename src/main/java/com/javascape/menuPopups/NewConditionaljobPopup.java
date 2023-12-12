package com.javascape.menuPopups;

import java.util.ArrayList;
import java.util.Arrays;

import com.javascape.Server;
import com.javascape.chronjob.ChronManager;
import com.javascape.chronjob.ChronjobItem;
import com.javascape.chronjob.ConditionalJob;
import com.javascape.recievers.Reciever;
import com.javascape.sensors.Sensor;
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

public class NewConditionaljobPopup {
    private ObservableList<String> observableCommands = FXCollections.observableArrayList();

    private ObservableList<String> observableElseCommands = FXCollections.observableArrayList();

    private ObservableList<String> observableConditions = FXCollections.observableArrayList();

    private ChronManager manager = Server.getDataHandler().getChronManager();

    public NewConditionaljobPopup() {
        Stage stage = new Stage();
        stage.setTitle("Create New Conditional Job");

        GridPane g = new GridPane();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        // #region ConditionBox
        VBox conditionBox = new VBox();
        HBox conditionBoxHeader = new HBox();

        Button newCondition = new Button("New Condition");
        newCondition.setOnAction(e -> {
            addCondition();
        });
        conditionBoxHeader.getChildren().addAll(new Label("Conditions:"), newCondition);

        conditionBox.getChildren().addAll(new Label("IF:"), conditionBoxHeader);

        observableConditions.addListener((javafx.beans.Observable observable) -> {
            conditionBox.getChildren().clear();
            conditionBox.getChildren().addAll(new Label("IF:"), conditionBoxHeader);
            for (String condition : observableConditions) {
                HBox temp = new HBox();

                Button delete = new Button();
                delete.getStyleClass().add("deleteButton");

                delete.setOnAction(e -> {
                    observableConditions.remove(condition);
                });
                temp.getChildren().addAll(new Label(condition), delete);
                conditionBox.getChildren().addAll(temp);
            }
            stage.sizeToScene();
        });
        // #endregion

        // #region Commandbox
        VBox commandBox = new VBox();
        HBox commandBoxHeader = new HBox();

        Button newCommand = new Button("New Command");
        newCommand.setOnAction(e -> {
            addCommand();
        });
        commandBoxHeader.getChildren().addAll(new Label("Commands:"), newCommand);

        commandBox.getChildren().addAll(new Label("DO:"), commandBoxHeader);

        observableCommands.addListener((javafx.beans.Observable observable) -> {
            commandBox.getChildren().clear();
            commandBox.getChildren().addAll(new Label("DO:"), commandBoxHeader);
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
        // #endregion

        // #region elseCommandbox
        VBox elseCommandBox = new VBox();
        HBox elseCommandBoxHeader = new HBox();

        Button newElseCommand = new Button("New Command");
        newElseCommand.setOnAction(e -> {
            addCommand();
        });
        elseCommandBoxHeader.getChildren().addAll(new Label("Commands:"), newElseCommand);

        elseCommandBox.getChildren().addAll(new Label("ELSE DO:"), elseCommandBoxHeader);

        observableElseCommands.addListener((javafx.beans.Observable observable) -> {
            elseCommandBox.getChildren().clear();
            elseCommandBox.getChildren().addAll(new Label("ELSE DO:"), elseCommandBoxHeader);
            for (String command : observableElseCommands) {
                HBox temp = new HBox();

                Button delete = new Button();
                delete.getStyleClass().add("deleteButton");

                delete.setOnAction(e -> {
                    observableElseCommands.remove(command);
                });
                temp.getChildren().addAll(new Label(command), delete);
                elseCommandBox.getChildren().addAll(temp);
            }
            stage.sizeToScene();
        });
        // #endregion

        g.add(nameLabel, 0, 0);
        g.add(nameField, 1, 0);
        g.add(conditionBox, 0, 1);
        g.add(commandBox, 0, 2);
        g.add(elseCommandBox, 0, 3);

        Button create = new Button("Create Conditional Job");
        Button cancel = new Button("Cancel");

        g.add(create, 0, 5);
        g.add(cancel, 1, 5);

        cancel.setOnAction(e -> {
            stage.close();
        });

        create.setOnAction(e -> {
            ArrayList<String> commands = new ArrayList<String>(observableCommands);
            ArrayList<String> conditions = new ArrayList<String>(observableConditions);

            ConditionalJob job = new ConditionalJob(nameField.textProperty().getValue(), commands, conditions);
            manager.newConditional(job);
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
        setType.setValue(manager.getAllItems().get(0));

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
        recieverBox.setValue(Server.getDataHandler().getRecieverHandler().getRecieverList().get(0));

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

    public void addCondition() {
        Stage stage = new Stage();

        GridPane g = new GridPane();

        ChoiceBox<Reciever> setReciever = new ChoiceBox<Reciever>(
                Server.getDataHandler().getRecieverHandler().getRecieverList());
        setReciever.setValue(Server.getDataHandler().getRecieverHandler().getRecieverList().get(0));

        ChoiceBox<Sensor> setSensor = new ChoiceBox<Sensor>();
        setSensor.getItems().addAll(Arrays.asList(setReciever.getValue().getSensors()));
        setSensor.setValue(setReciever.getValue().getSensors()[0]);

        setReciever.setOnAction(e -> {
            setSensor.getItems().clear();
            setSensor.getItems().addAll(Arrays.asList(setReciever.getValue().getSensors()));
            setSensor.setValue(setReciever.getValue().getSensors()[0]);
        });

        ChoiceBox<String> setOperator = new ChoiceBox<String>();
        setOperator.getItems().addAll("==", ">", "<", ">=", "<=");
        setOperator.setValue("==");

        TextField valueField = new TextField();

        g.add(setReciever, 0, 0);
        g.add(setSensor, 1, 0);
        g.add(setOperator, 2, 0);
        g.add(valueField, 3, 0);

        Button add = new Button("Add");
        add.setOnAction(e -> {
            String condition = setReciever.getValue().getUID() + ":" + setSensor.getValue().getIndex() + " "
                    + setOperator.getValue() + " " + valueField.textProperty().get();
            observableConditions.add(condition);
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            stage.close();
        });

        g.add(add, 0, 1);
        g.add(cancel, 1, 1);

        Scene s = new Scene(g);

        stage.setScene(s);

        stage.show();
    }

    public void addElseCommand() {
        Stage stage = new Stage();

        GridPane g = new GridPane();

        ChoiceBox<ChronjobItem> setType = new ChoiceBox<ChronjobItem>();
        setType.getItems().addAll(manager.getAllItems());
        setType.setValue(manager.getAllItems().get(0));

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
        recieverBox.setValue(Server.getDataHandler().getRecieverHandler().getRecieverList().get(0));

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
            observableElseCommands.add(command);
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
