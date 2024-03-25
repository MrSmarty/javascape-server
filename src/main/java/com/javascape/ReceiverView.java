package com.javascape;

import com.javascape.receivers.Receiver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public class ReceiverView {

    /** List that contains the Nodes to represent each receiver */
    ObservableList<Node> receiverList = FXCollections.observableArrayList();

    /**
     * Creates a receiverView and sets it to update every time the
     * {@link #receiverList} changes.
     */
    public ReceiverView() {
        update();

        Server.getDataHandler().getReceiverHandler().getReceiverList()
                .addListener((ListChangeListener.Change<? extends Receiver> c) -> {
                    update();
                });
    }

    /** Returns the list with the Receiver panes */
    public ListView<Node> getReceiverView() {
        ListView<Node> view = new ListView<Node>(receiverList);

        return view;
    }

    /** Updates the ReceiverView with any new data */
    public void update() {
        Logger.print("Updating ReceiverView");
        Platform.runLater(new Runnable() {
            public void run() {
                receiverList.clear();
                for (Receiver r : Server.getDataHandler().getReceiverHandler().getReceiverList()) {
                    if (r != null) {
                        if (r.getCurrentThread() != null) {
                            receiverList.add(r.getReceiverPane());
                        } else {
                            Logger.debug("Null thread");
                            receiverList.add(r.getReceiverPane());
                        }
                    }

                }
            }
        });

    }
}
