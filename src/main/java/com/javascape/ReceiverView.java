package com.javascape;

import com.javascape.receivers.Receiver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public final class ReceiverView {

    /**
     * List that contains the Nodes to represent each receiver
     */
    private final ObservableList<Node> receiverViewList = FXCollections.observableArrayList();

    /**
     * Creates a receiverView and sets it to update every time the
     * {@link #receiverViewList} changes.
     */
    public ReceiverView() {
        update();

        Server.getDataHandler().getReceiverHandler().getReceiverList()
                .addListener((ListChangeListener.Change<? extends Receiver> c) -> {
                    update();
                });
    }

    /**
     * Returns the list with the Receiver panes
     *
     * @return The Node with the receiverView
     */
    public ListView<Node> getReceiverView() {
        update();
        ListView<Node> view = new ListView<>(receiverViewList);

        return view;
    }

    /**
     * Updates the ReceiverView with any new data
     */
    public void update() {
        Logger.print("Updating ReceiverView");
        Platform.runLater(() -> {
            receiverViewList.clear();
            for (Receiver r : Server.getDataHandler().getReceiverHandler().getReceiverList()) {
                if (r != null) {
                    receiverViewList.add(r.getReceiverPane());
                }

            }
        });

    }
}
