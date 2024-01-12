package com.javascape;

import com.javascape.menuPopups.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Handles the GUI for the server.
 */
public class ServerGUI {

    /** A reference to the server */
    Server server;

    /** A referenc to the primary stage */
    Stage primaryStage;

    /** The main scene */
    BorderPane mainPane;

    static ReceiverView receiverView;

    /** A reference to the terminal */
    static ListView<Text> terminalListView = new ListView<Text>();;

    public ServerGUI(Stage primaryStage, Server server) {
        this.server = server;
        primaryStage = new Stage();

        primaryStage.setTitle("JavaScape Server - " + Settings.version);

        primaryStage.setScene(getMainScene());

        primaryStage.sizeToScene();

        primaryStage.show();

        primaryStage = this.primaryStage;

    }

    public Stage getStage() {
        return primaryStage;
    }

    /** Returns the main scene */
    private Scene getMainScene() {

        mainPane = new BorderPane();

        MenuBar menuBar = getMenuBar();

        mainPane.setTop(menuBar);

        mainPane.setLeft(sideMenu());

        receiverView = new ReceiverView();

        mainPane.setCenter(receiverView.getReceiverView());

        if (Settings.showTerminal)
            mainPane.setBottom(getTerminalElement());

        Scene scene = new Scene(mainPane);

        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        return scene;
    }

    /** The sidebar menu */
    private VBox sideMenu() {
        VBox menu = new VBox();
        menu.setId("verticalMenu");
        // menu.setStyle("-fx-background-color: #CCCCCC; -fx-padding: 10px;
        // -fx-text-align: center;");

        // FIXME: Fix the profile image not working
        // ImageView profileImage = new ImageView(server.loggedInUser.getUserImage());

        Text name = new Text(server.loggedInUser.getUsername());
        name.setStyle("-fx-color: #222222;");
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            mainPane.setCenter(receiverView.getReceiverView());
        });

        // ADD PROFILE IMAGW BACK TO THE FRONT
        menu.getChildren().addAll(name, homeButton);

        return menu;
    }

    /** Returns the MenuBar for the GUI */
    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        // #region The File menu
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem settings = new MenuItem("Settings");
        Menu terminal = new Menu("Terminal");
        MenuItem close = new MenuItem("Close");
        MenuItem quitProgram = new MenuItem("Quit Program");

        save.setOnAction(e -> {
            Server.getDataHandler().save();
        });

        settings.setOnAction(e -> {
            new SettingsPopup();
        });

        // #region Terminal Menu
        MenuItem clearTerminal = new MenuItem("Clear Terminal");

        clearTerminal.setOnAction(e -> {
            terminalListView.getItems().clear();
        });

        terminal.getItems().addAll(clearTerminal);
        // #endregion

        close.setOnAction(e -> {
            primaryStage.hide();
        });

        quitProgram.setOnAction(e -> {
            server.getServerThreadHandler().closeThreads();
            Server.getDataHandler().getChronManager().quit();
            Logger.print("Exiting Program");
            Platform.exit();
        });

        file.getItems().addAll(save, settings, terminal, close, quitProgram);
        // #endregion

        Menu users = new Menu("Users");

        MenuItem createUser = new MenuItem("Create User");
        MenuItem editUser = new MenuItem("Edit User");
        MenuItem deleteUser = new MenuItem("Delete User");

        createUser.setOnAction(e -> {
            new CreateNewUserPopup();
        });

        editUser.setOnAction(e -> {
            new EditUserPopup();
        });

        deleteUser.setOnAction(e -> {
            new DeleteUserPopup(server);
        });

        users.getItems().addAll(createUser, editUser, deleteUser);

        Menu households = new Menu("Households");

        MenuItem createHousehold = new MenuItem("Create Household");

        createHousehold.setOnAction(e -> {
            new CreateNewHouseholdPopup();
        });

        households.getItems().addAll(createHousehold);

        Menu chronjobs = new Menu("Chronjobs");

        MenuItem createChronjob = new MenuItem("New Chronjob");

        MenuItem createConditional = new MenuItem("New Conditional");

        MenuItem deleteChronjob = new MenuItem("Delete Chronjob");

        createChronjob.setOnAction(e -> {
            new NewChronjobPopup();
        });

        createConditional.setOnAction(e -> {
            new NewConditionaljobPopup();
        });

        deleteChronjob.setOnAction(e -> {
            new DeleteChronjobPopup();
        });

        chronjobs.getItems().addAll(createChronjob, createConditional, deleteChronjob);

        menuBar.getMenus().addAll(file, users, households, chronjobs);

        return menuBar;
    }

    /** Returns the ScrollPane that is the terminal */
    private ListView<Text> getTerminalElement() {
        terminalListView.setId("terminal");
        terminalListView.setPrefHeight(200);
        return terminalListView;
    }

    /** Writes to the terminal */
    public static void writeToTerminal(String message) {
        Text t = new Text(message);
        t.setStyle("-fx-font-family: Arial; -fx-fill: whitesmoke;");
        terminalListView.getItems().add(t);
    }

    /** Writes to the terminal */
    public static void writeToTerminal(String message, String color) {
        Text t = new Text(message);
        t.setStyle("-fx-font-family: Arial; -fx-fill: " + color + ";");
        terminalListView.getItems().add(t);
    }

    /** Clears the terminal */
    public static void clearTerminal() {
        terminalListView.getItems().clear();
    }

    public ReceiverView getReceiverView() {
        return receiverView;
    }

}
