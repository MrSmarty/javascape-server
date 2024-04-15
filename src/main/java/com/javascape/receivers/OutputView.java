package com.javascape.receivers;

import com.javascape.Server;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public final class OutputView {

    /**
     * List that contains the Nodes to represent each output.
     */
    public static final ObservableList<Node> outputs = FXCollections.observableArrayList();

    public OutputView() {
        update();

        Server.getDataHandler().getReceiverHandler().getReceiverList()
                .addListener((ListChangeListener.Change<? extends Receiver> c) -> {
                    update();
                });
    }

    public static ListView<Node> getOutputsListView() {
        update();
        ListView<Node> view = new ListView<>(outputs);

        return view;
    }

    /**
     * Updates the outputs list.
     */
    public static void update() {
        outputs.clear();
        for (Receiver r : Server.getDataHandler().getReceiverHandler().getReceiverList()) {
            for (GPIO g : r.getGPIO()) {
                if (g.value >= 0 && g.getName() != null && !g.getName().equals("")) {
                    outputs.add(g.getUI());
                }
            }
        }
    }

}
