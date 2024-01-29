package com.javascape;

import com.javascape.receivers.Receiver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class ReceiverView {

    ObservableList<GridPane> receiverList = FXCollections.observableArrayList();

    public ReceiverView() {
        update();

        Server.getDataHandler().getReceiverHandler().getReceiverList()
                .addListener((ListChangeListener.Change<? extends Receiver> c) -> {
                    update();
                });
    }

    public ListView<GridPane> getReceiverView() {
        ListView<GridPane> view = new ListView<GridPane>(receiverList);

        return view;
    }

    public void update() {
        Logger.print("Updating ReceiverView");
        Platform.runLater(new Runnable() {
            public void run() {
                receiverList.clear();
                for (Receiver r : Server.getDataHandler().getReceiverHandler().getReceiverList()) {
                    if (r.getCurrentThread() != null) {
                        receiverList.add(r.getReceiverPane());
                    } else {
                        Logger.debug("Null thread");
                        receiverList.add(r.getReceiverPane());
                    }
                }
            }
        });

    }
}
