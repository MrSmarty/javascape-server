package com.javascape;

import javafx.application.Platform;

/**
 * A simple class for printing messages to console and log files.
 */
public class Logger {

    /**
     * Prints the specified string. Relies on the variables found in the
     * {@link Java.Server.Settings} class to determine to where the message is
     * printed.
     * 
     * @param message The message to be printed
     */
    public static void print(String message) {
        if (!Settings.loggerEnabled)
            return;

        if (Settings.loggerConsole)
            System.out.println(message);

        try {
            if (Settings.loggerTerminalOutput) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        ServerGUI.writeToTerminal(message);
                    }
                });

            }
        } catch (NullPointerException e) {
            // Handles if there is no terminal
        }

    }

    /**
     * Prints the specified string if debug mode is enabled. Relies on the variables
     * found in the {@link Java.Server.Settings} class to determine to where the
     * message is printed.
     * 
     * @param message The message to be printed
     */
    public static void debug(String message) {
        if (!Settings.debugEnabled)
            return;

        if (Settings.debugConsole)
            System.out.println(message);

        try {
            if (Settings.debugTerminalOutput)
                Platform.runLater(new Runnable() {
                    public void run() {
                        ServerGUI.writeToTerminal(message);
                    }
                });
        } catch (NullPointerException e) {
            // Handles if there is no terminal
        }
    }

    /**
     * Prints the message wherever needed and highlights it red
     * Always enabled
     */
    public static void error(String message) {

        System.out.println(message);

        try {

            Platform.runLater(new Runnable() {
                public void run() {
                    ServerGUI.writeToTerminal(message, "#FF2222");
                }
            });
        } catch (NullPointerException e) {
            // Handles if there is no terminal
        }
    }
}
