package com.javascape;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The main class for the server.
 */
public class Server extends Application {

    /** The ServerThreadHandler for the server */
    private ServerThreadHandler serverThreadHandler;

    /** The datahandler for the server */
    private static DataHandler dataHandler;
    Stage primaryStage;

    private static ServerGUI gui;

    /** The User currently logged in */
    public User loggedInUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        initialize();

        java.awt.SplashScreen.getSplashScreen().close();

        startServer();

        if (!Settings.autoLogin)
            login();
        else {
            loggedInUser = dataHandler.getUserHandler().getUser(Settings.loggedInEmailAuto);
            loggedIn();
        }
    }

    /**
     * Initializes the server and all the variables.
     */
    private void initialize() {
        Logger.print("Initializing server");

        dataHandler = new DataHandler();
        dataHandler.getChronManager().startJobs();

        serverThreadHandler = new ServerThreadHandler(this);
    }

    /** Called once the user has been logged in */
    public void loggedIn() {
        primaryStage.hide();
        startGui();
    }

    /** Setup the login screen and validate credentials and such */
    private void login() {
        LoginWindow loginWindow = new LoginWindow(primaryStage, dataHandler, this);
        loginWindow.show();
    }

    /** Sets up and runs the GUI for the Server. Called after a successful login */
    public void startGui() {
        gui = new ServerGUI(primaryStage, this);
    }

    /** Starts the acutal server and listens for connections */
    private void startServer() {
        Thread.ofVirtual().start(serverThreadHandler);
    }

    // #region Get and Set function
    public static DataHandler getDataHandler() {
        return dataHandler;
    }

    public ServerThreadHandler getServerThreadHandler() {
        return serverThreadHandler;
    }

    public static ServerGUI getGUI() {
        return gui;
    }

    // #endregion
}
