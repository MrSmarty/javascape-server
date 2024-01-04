package com.javascape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;

import com.javascape.recievers.Reciever;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A connection thread for the Server.
 */
public class ServerThread extends Thread {
    /** The socket that the ServerThread communicates on */
    protected Socket socket;
    /** A reference to the server */
    protected Server server;

    private Reciever currentReciever;

    /** An enum to store the possible types */
    private enum DeviceType {
        Pending,
        Client,
        Reciever
    };

    /** The commands to send the pico */
    public ObservableList<String> commands = FXCollections.observableArrayList();

    /** Stores the type of device on the thread */
    private DeviceType deviceType = DeviceType.Pending;

    /** BufferedReader to read in the input from the socket */
    private BufferedReader socketReader;

    /** Printstream to ouput to the other device */
    private PrintStream printStream;

    /** Boolean that runs the while loop */
    private boolean run;

    // #region Constructors

    /**
     * Creates a new ServerThread.
     * 
     * @param socket The socket that the ServerThread communicates on.
     * @param server A reference to the server.
     */
    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    // #endregion

    // #region Methods

    /**
     * The main method of the ServerThread.
     */
    public void run() {
        Logger.print("New thread created");

        try {
            // to send data to the socket
            printStream = new PrintStream(socket.getOutputStream());

            

            // to read data coming from the socket
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        printStream.println("getInfo" + " ");
        printStream.flush();

        try {
            String info = socketReader.readLine();
            String[] args = info.split(" ");

            System.out.println(Arrays.toString(args));

            if (args[1].equals("reciever")) {
                deviceType = DeviceType.Reciever;
                if (Server.getDataHandler().getRecieverHandler().getReciever(args[3]) == null) {
                    Logger.print("Reciever Created");
                    Server.getDataHandler().getRecieverHandler().addReciever(args[2], args[3], args[4]);
                    Server.getDataHandler().save();
                }
                Server.getDataHandler().getRecieverHandler().getReciever(args[3]).setThreadInfo(this, this.threadId());
                currentReciever = Server.getDataHandler().getRecieverHandler().getReciever(args[3]);
            }
        } catch (IOException e) {
            Logger.print("Exception getting info");
            quit();
        }

        run = true;

        if (deviceType == DeviceType.Reciever) {
            Platform.runLater(new Runnable() {
                public void run() {
                    ServerGUI.recieverView.update();
                }
            });
            sendFirstProtocol();
        } else
            recieveFirstProtocol();

    }

    /** Used for connection with reciecver clients */
    private void sendFirstProtocol() {
        Logger.debug("Starting sendFirstProtocol on " + this.getName());
        addCommand("ping");
        addCommand("getTemp");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        while (run) {
            // Block the thread for a second
            try {
                Thread.sleep(1);
            } catch (Exception e) {

            }

            if (commands.size() > 0) {
                Logger.debug("New command executing...");

                Future<String> future = executor.submit(new Callable<String>() {
                    @Override
                    public String call() {
                        try {

                            String out = commands.remove(0) + " ";

                            printStream.println(out);
                            printStream.flush();

                            Logger.print("Sent: " + out);

                            // give us the data coming in and print if not null
                            String in = socketReader.readLine();
                            Logger.print("Recieved: " + in);
                            String[] inArgs = in.split(" ");
                            switch (inArgs[0]) {
                                case "internalTemp":
                                    currentReciever.addInternalTemperatureValue(Double.parseDouble(inArgs[1]));
                                    break;
                                case "sensorValues":
                                    for (int i = 1; i < inArgs.length; i += 2) {
                                        int tempI = i;
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                currentReciever.getSensor(Integer.parseInt(inArgs[tempI]))
                                                        .addValue(inArgs[tempI + 1]);
                                            }
                                        });
                                    }
                                    break;
                            }
                        } catch (IOException e) {
                            Logger.print("Exception in send-first protocol: " + e.toString());
                        }
                        return "done";
                    }
                });

                try {
                    Logger.print("Started..");
                    Logger.print(future.get(Settings.timeOut, TimeUnit.SECONDS));
                    Logger.print("Finished!");
                } catch (Exception e) {
                    future.cancel(true);
                    Logger.print("Terminated!\n" + e.toString());
                    quit();
                }

            }
        }
        executor.shutdownNow();

    }

    /** Used for connections with user client */
    private void recieveFirstProtocol() {

        String in = null;
        String out = null;

        while (run) {
            try {
                // give us the data coming in and print if not null
                in = socketReader.readLine();
                Logger.print("Recieved: " + in);

                out = "ok";

                printStream.println(out);
                printStream.flush();

                Logger.print("Sent: " + out);

            } catch (IOException e) {
                Logger.print(e.toString());
            }
            in = null;
            out = null;
        }
    }

    public void quit() {
        Logger.print("Closing thread with name:" + this.getName());
        run = false;
        currentReciever.setThreadInfo(null, 0);
        ServerGUI.recieverView.update();

        // close connection
        try {
            // keyboardReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // #endregion

    public String getType() {
        return deviceType.toString();
    }

    public void addCommand(String command) {
        Logger.debug("Adding command...");
        commands.add(command);
    }

    public void addCommand(String command, int index) {
        Logger.debug("Adding command to index " + index + "...");
        commands.add(index, command);
    }

}
