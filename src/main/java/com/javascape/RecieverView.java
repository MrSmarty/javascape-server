package com.javascape;

import com.javascape.recievers.Reciever;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class RecieverView {
    protected ServerThread currentThread;

    ObservableList<GridPane> recieverList = FXCollections.observableArrayList();

    public RecieverView() {
        update();

        Server.getDataHandler().getRecieverHandler().getRecieverList()
                .addListener((ListChangeListener.Change<? extends Reciever> c) -> {
                    update();
                });
    }

    public ListView<GridPane> getRecieverView() {
        ListView<GridPane> view = new ListView<GridPane>(recieverList);

        return view;
    }

    public void update() {
        Logger.print("Updating RecieverView");
        Platform.runLater(new Runnable() {
            public void run() {
                recieverList.clear();
                for (Reciever r : Server.getDataHandler().getRecieverHandler().getRecieverList()) {
                    if (r.getCurrentThread() != null) {
                        recieverList.add(r.getRecieverPane());
                    } else {
                        Logger.debug("Null thread");
                        recieverList.add(r.getRecieverPane());
                    }
                }
            }
        });

    }
}
