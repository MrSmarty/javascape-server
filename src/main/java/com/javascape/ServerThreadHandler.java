package com.javascape;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The class that handles the socket connections & threads. The actual server.
 */
public class ServerThreadHandler extends Thread {

    /**
     * A refernce to the server
     */
    Server server;

    /**
     * List of all the threads (connections)
     */
    ObservableList<ServerThread> threads;

    /**
     * The server socket connection
     */
    ServerSocket serverSocket;


    /**
     * Basic constructor
     *
     * @param server The server
     *
     */
    public ServerThreadHandler(Server server) {
        this.server = server;
    }

    /**
     * Starts the actual server
     */
    @Override
    public void run() {
        threads = FXCollections.observableArrayList();

        try {
            serverSocket = new ServerSocket(Settings.port);
            Logger.print("Server started with port: " + Settings.port);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

        while (true) {
            // May have to put this in a completable future:
            try {
                // Accepts connection
                Logger.print("Waiting for incoming connection...");
                Socket incomingSocket = serverSocket.accept();
                Logger.print("Socket connected");

                // Creates the new thread
                ServerThread t = new ServerThread(incomingSocket, server);
                threads.add(t);
                Thread.ofVirtual().start(t);
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    public void closeThreads() {
        for (ServerThread t : threads) {
            t.quit();
        }
    }
}
