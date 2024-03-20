package com.javascape;

import java.util.concurrent.TimeUnit;

/** A class containing all of the variables that are used for the settings */
public class Settings {

    public static String version = "a0.0.1";

    /** Boolean that determines whether or not to display the terminal */
    public static boolean showTerminal = true;

    /** Boolean that determines if the Logger is enabled */
    public static boolean loggerEnabled = true;

    /** Determines if the Logger prints to the terminal in app */
    public static boolean loggerTerminalOutput = true;
    /** Determines if the Logger prints to the debug console */
    public static boolean loggerConsole = true;
    /** Determines if the Logger prints to a log file */
    public static boolean loggerFile = true;

    /** Boolean that determines if the Logger is enabled */
    public static boolean debugEnabled = true;

    /** Determines if the Logger prints the debug to the terminal in app */
    public static boolean debugTerminalOutput = true;
    /** Determines if the Logger prints the debug to the debug console */
    public static boolean debugConsole = true;
    /** Determines if the Logger prints the debug to a log file */
    public static boolean debugFile = true;

    /** The location of the data files */
    public static String storageLocation = "./data/";

    /** The port that the server runs on */
    public static int port = 19;

    /** The interval for retrieving the temperature of the boards */
    public static int getTempInterval = 5;

    /** The unit for the interval of retreiving the temperature of the boards */
    public static TimeUnit getTempIntervalUnit = TimeUnit.MINUTES;

    /** The amount of units for getting the sensor data */
    public static int getSensorDataJobInterval = 15;

    /** The unit for getting the sensor data */
    public static TimeUnit getSensorDataJobIntervalUnit = TimeUnit.SECONDS;

    /** The default interval for checking the conditionals */
    public static int conditionalCheckInterval = 15;

    /** The default unit for checking the conditionals */
    public static TimeUnit conditionalCheckUnit = TimeUnit.SECONDS;

    /**
     * How long the serverThread will wait for a response.
     */
    public static int timeOut = 15;

    /** Whether or not to automagically login to the application */
    public static boolean autoLogin = true;

    /** The email that is to be used for the autologin. */
    public static String loggedInEmailAuto = "admin";
}
