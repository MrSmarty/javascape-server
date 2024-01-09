package com.javascape;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.*;

import com.javascape.chronjob.ChronManager;
import com.javascape.chronjob.Chronjob;
import com.javascape.sensors.Sensor;
import com.javascape.gsonDeserializers.*;
import javafx.collections.ObservableList;

/**
 * Handles all the data of the server.
 */
public class DataHandler {

    /** The GSON object to be used for saving and loading the JSON */
    private Gson gson = new GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
            .registerTypeAdapter(ObservableList.class, new ObservableListDeserializer())
            .registerTypeAdapter(Sensor.class, new SensorDeserializer())
            .disableHtmlEscaping().create();

    /** The path for the settings file */
    Path settingsFilePath = Paths.get("/settings.json");
    /** The file for the settings file */
    File settingsFile = new File(settingsFilePath.toString());
    /** The local settings object */
    Settings settings;

    // #region Handlers
    private UserHandler userHandler;
    private HouseholdHandler householdHandler;
    private RecieverHandler recieverHandler;

    private ChronManager chronManager;
    // #endregion

    /** Initializes the datahandler object with no constructors */
    public DataHandler() {
        initialize();
    }

    /**
     * Initializes all the variables of the data handler. Deserializes the Settings,
     * Users, Households, and Devices.
     */
    private void initialize() {
        Logger.print("Initializing data handler");
        createDataFolder();
        loadSettings();
        loadHandlers();
        save();
    }

    /** Ensures that there is a data folder in the filesystem */
    private void createDataFolder() {
        Logger.print("Checking for data folder");
        File dataFolder = new File(Settings.storageLocation);
        if (!dataFolder.exists()) {
            Logger.print("Data folder does not exist, creating new one");
            dataFolder.mkdir();
        }
        Logger.print(dataFolder.getAbsolutePath());
    }

    /** Saves all the data in the DataHandler to their respective files */
    public void save() {
        Logger.print("Saving data");
        saveSettings();
        saveUsers();
        saveHouseholds();
        saveRecievers();
        saveChronManager();
    }

    /** Deserializes and loads the settings */
    private void loadSettings() {
        try {
            if (!settingsFile.exists() || settingsFile.equals(null)) {
                Logger.print("Settings file does not exist, creating new one");
                settings = new Settings();
            } else {
                Logger.print("Settings file exists, loading");
                settings = gson.fromJson(
                        Files.lines(settingsFilePath, StandardCharsets.UTF_8).collect(Collectors.joining("\n")),
                        Settings.class);
            }
        } catch (Exception e) {
            Logger.print("Failed to load settings");
            Logger.print(e.toString());
        }
    }

    /** Loads all the handlers */
    private void loadHandlers() {
        Logger.print("Loading handlers");
        loadUserHandler();
        loadHouseholdHandler();
        loadRecieverHandler();
        loadChronManager();
    }

    /** Loads the UserHandler */
    private void loadUserHandler() {
        Logger.print("Loading user handler");
        Path usersFilePath = Paths.get(Settings.storageLocation + "users.json");
        File usersFile = new File(usersFilePath.toString());
        try {
            if (!usersFile.exists() || usersFile.equals(null)) {
                Logger.print("Users file does not exist, creating new one");
                userHandler = new UserHandler();
                Logger.print("Initializing an admin user...");
                userHandler.addUser(new User("admin", "admin", true, "admin"));
            } else {
                Logger.print("Users file exists, loading");
                userHandler = gson.fromJson(
                        Files.lines(usersFilePath, StandardCharsets.UTF_8).collect(Collectors.joining("\n")),
                        UserHandler.class);
            }
        } catch (Exception e) {
            Logger.print("Failed to load userHandler");
            Logger.error(e.toString());
        }
    }

    /** Loads the HouseholdHandler */
    private void loadHouseholdHandler() {
        Logger.print("Loading Household handler");
        Path filePath = Paths.get(Settings.storageLocation + "households.json");
        File file = new File(filePath.toString());
        try {
            if (!file.exists() || file.equals(null)) {
                Logger.print("Household file does not exist, creating new one");
                householdHandler = new HouseholdHandler();
            } else {
                Logger.print("Household file exists, loading");
                householdHandler = gson.fromJson(
                        Files.lines(filePath, StandardCharsets.UTF_8).collect(Collectors.joining("\n")),
                        HouseholdHandler.class);
            }
        } catch (Exception e) {
            Logger.print("Failed to load Household Handler");
            Logger.error(e.toString());
        }
    }

    /** Loads the recieverHandler */
    private void loadRecieverHandler() {
        Logger.print("Loading Reciever handler");
        Path filePath = Paths.get(Settings.storageLocation + "recievers.json");
        File file = new File(filePath.toString());

        try {
            if (!file.exists() || file.equals(null)) {
                Logger.print("Reciever file does not exist, creating new one");
                recieverHandler = new RecieverHandler();
            } else {
                Logger.print("Reciever file exists, loading");
                recieverHandler = gson.fromJson(
                        Files.lines(filePath, StandardCharsets.UTF_8).collect(Collectors.joining("\n")),
                        RecieverHandler.class);
            }
        } catch (Exception e) {
            Logger.print("Failed to load Reciever Handler");
            Logger.error(e.toString());
        }
    }

    /** Loads the recieverHandler */
    private void loadChronManager() {
        Logger.print("Loading Chronjob Manager");
        Path filePath = Paths.get(Settings.storageLocation + "chronManager.json");
        File file = new File(filePath.toString());

        try {
            if (!file.exists() || file.equals(null)) {
                Logger.print("Chronjob Manager file does not exist, creating new one");
                chronManager = new ChronManager();

                chronManager.loadData();
            } else {
                Logger.print("Chronjob Manager file exists, loading");
                chronManager = gson.fromJson(
                        Files.lines(filePath, StandardCharsets.UTF_8).collect(Collectors.joining("\n")),
                        ChronManager.class);
                chronManager.loadData();
            }
            createTempJob();
            createSensorDataJob();
        } catch (Exception e) {
            Logger.print("Failed to load Chronjob Manager");
            Logger.error(e.toString());
        }
    }

    /** Serializes and saves the settigns */
    private void saveSettings() {
        try {
            Logger.print("Saving settings");
            Files.write(settingsFilePath, gson.toJson(settings).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.print("Failed to save settings");
            Logger.error(e.toString());
        }
    }

    private void saveUsers() {
        Path usersFilePath = Paths.get(Settings.storageLocation + "users.json");
        try {
            Logger.print("Saving users");
            Files.write(usersFilePath, gson.toJson(userHandler).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.print("Failed to save users");
            Logger.error(e.toString());
        }
    }

    private void saveHouseholds() {
        Path filePath = Paths.get(Settings.storageLocation + "households.json");
        try {
            Logger.print("Saving households");
            Files.write(filePath, gson.toJson(householdHandler).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.print("Failed to save households");
            Logger.error(e.toString());
        }
    }

    private void saveRecievers() {
        Path filePath = Paths.get(Settings.storageLocation + "recievers.json");
        try {
            Logger.print("Saving Recievers");
            Files.write(filePath, gson.toJson(recieverHandler).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.print("Failed to save recievers");
            Logger.error(e.toString());
        }
    }

    private void saveChronManager() {
        Path filePath = Paths.get(Settings.storageLocation + "chronManager.json");
        try {
            Logger.print("Saving Chronjob Manager");
            Files.write(filePath, gson.toJson(chronManager).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.print("Failed to save Chronjob Manager");
            Logger.error(e.toString());
        }
    }

    /** Login for the Server side (Checks admin rights too) */
    public boolean serverLogin(String email, String password) {
        if (userHandler.login(email, password)) {
            if (userHandler.getUser(email).isAdmin()) {
                return true;
            }
        }
        return false;
    }

    public void createTempJob() {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("getTemp - all");

        Chronjob getTempJob = new Chronjob("GetTempJob", commands, Settings.getTempInterval,
                Settings.getTempIntervalUnit);
        chronManager.newRepeating(getTempJob, false);
    }

    public void createSensorDataJob() {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("getSensors - all");

        Chronjob getTempJob = new Chronjob("getSensorDataJob", commands, Settings.getSensorDataJobInterval,
                Settings.getSensorDataJobIntervalUnit);
        chronManager.newRepeating(getTempJob, false);
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public HouseholdHandler getHouseholdHandler() {
        return householdHandler;
    }

    public RecieverHandler getRecieverHandler() {
        return recieverHandler;
    }

    public ChronManager getChronManager() {
        return chronManager;
    }

    public String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public String serialize(Object obj, boolean simplify) {
        if (simplify) {
            String tempString = gson.toJson(obj);
            return tempString.replaceAll("\n", "");
        } else {
            return gson.toJson(obj);
        }
    }
}
