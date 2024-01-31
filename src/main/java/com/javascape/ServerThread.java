package com.javascape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;

import com.javascape.chronjob.Chronjob;
import com.javascape.chronjob.ConditionalJob;
import com.javascape.receivers.Receiver;
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

    private Receiver currentReceiver;

    /** An enum to store the possible types */
    private enum DeviceType {
        Pending,
        Client,
        Receiver
    };

    /** The commands to send the receiver */
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

            if (args[1].equals("receiver")) {
                deviceType = DeviceType.Receiver;
                if (Server.getDataHandler().getReceiverHandler().getReceiver(args[3]) == null) {
                    Logger.print("Receiver Created");
                    Server.getDataHandler().getReceiverHandler().addReceiver(args[2], args[3], args[4]);
                    Server.getDataHandler().save();
                }
                Server.getDataHandler().getReceiverHandler().getReceiver(args[3]).setThreadInfo(this, this.threadId());
                currentReceiver = Server.getDataHandler().getReceiverHandler().getReceiver(args[3]);
            } else if (args[1].equals("client")) {
                Logger.print("Client connection");
                deviceType = DeviceType.Client;
                User tempUser = Server.getDataHandler().getUserHandler().getUser(args[2]);
                if (tempUser != null && tempUser.getPassword().equals(args[3])) {
                    Logger.print("User logged in");
                    printStream.println("loginStatus true");
                    printStream.flush();
                } else {
                    Logger.print("User not logged in");
                    printStream.println("loginStatus false");
                    printStream.flush();
                    quit();
                }

            }
        } catch (IOException e) {
            Logger.print("Exception getting info");
            quit();
        }

        run = true;

        if (deviceType == DeviceType.Receiver) {
            Platform.runLater(new Runnable() {
                public void run() {
                    ServerGUI.receiverView.update();
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
                                    currentReceiver.addInternalTemperatureValue(Double.parseDouble(inArgs[1]));
                                    break;
                                case "sensorValues":
                                    for (int i = 1; i < inArgs.length; i += 2) {
                                        int tempI = i;
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                currentReceiver.getSensor(Integer.parseInt(inArgs[tempI]))
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
        Logger.debug("Starting recieveFirstProtocol on " + this.getName());

        String in = null;
        String out = null;

        while (run) {
            try {
                // give us the data coming in and print if not null
                in = socketReader.readLine();
                Logger.print("Recieved: " + in);

                if (in == null) {
                    Logger.print("'in' is null");
                    return;
                }

                String[] args = in.split(" ");

                if (args[0].equals("getUserInfo")) {
                    User tempUser = Server.getDataHandler().getUserHandler().getUser(args[1]);
                    out = Server.getDataHandler().serialize(tempUser, true);
                    System.out.println(out);
                } else if (args[0].equals("createUser")) {
                    out = "" + Server.getDataHandler().getUserHandler()
                            .addUser(new User(args[1], args[2], Integer.parseInt(args[3]), args[4]));
                } else if (args[0].equals("deleteUser")) {
                    out = "" + Server.getDataHandler().getUserHandler().removeUser(args[1]);
                } else if (args[0].equals("editUser")) {
                    out = "" + Server.getDataHandler().getUserHandler().updateUser(
                            Server.getDataHandler().getUserHandler().getUser(args[1]), args[2], args[3],
                            Integer.parseInt(args[4]), args[5]);
                } else if (args[0].equals("getUserList")) {
                    out = Server.getDataHandler().serialize(Server.getDataHandler().getUserHandler().getAllUsers(),
                            true);
                } else if (args[0].equals("getReceiverList")) {
                    out = Server.getDataHandler().serialize(
                            Server.getDataHandler().getReceiverHandler().getReceiverList(), true);
                } else if (args[0].equals("createHousehold")) {
                    // TODO: Createhousehold for clients
                } else if (args[0].equals("newRepeating")) {
                    out = "" + Server.getDataHandler().getChronManager().newRepeating(in.substring(12));
                } else if (args[0].equals("newConditional")) {
                    //TODO: newConditional for clients
                } else if (args[0].equals("getChronjobList")) {
                    out = Server.getDataHandler().serialize(Server.getDataHandler().getChronManager().getAllJobs(), true);
                } else if (args[0].equals("getChronjobListItems")) {
                    out = Server.getDataHandler().serialize(
                            Server.getDataHandler().getChronManager().getAllItems(), true);
                } else if (args[0].equals("deleteChronjob")) {
                    out = "" + Server.getDataHandler().getChronManager().remove(!in.contains("conditions") ? (Chronjob)Server.getDataHandler().deserialize(in.substring(14), Chronjob.class) : (ConditionalJob)Server.getDataHandler().deserialize(in.substring(14), ConditionalJob.class));
                } else if (args[0].equals("setPin")) {
                    out = "" + Server.getDataHandler().getReceiverHandler().getReceiver(args[1]).setValue(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                } else {
                    out = "ok";
                }

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
        if (currentReceiver != null)
            currentReceiver.setThreadInfo(null, 0);
        ServerGUI.receiverView.update();

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
