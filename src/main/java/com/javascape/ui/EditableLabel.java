package com.javascape.ui;

import java.lang.reflect.Method;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class EditableLabel extends StackPane {

    Label label;
    TextField field;

    public EditableLabel(String text, Object parent, Method method) {
        label = new Label(text);
        field = new TextField(text);
        field.visibleProperty().set(false);
        field.cursorProperty().setValue(Cursor.HAND);

        field.setOnAction(e -> {
            label.setText(field.getText());
            field.visibleProperty().set(false);
            label.visibleProperty().set(true);

            // Set the new name of the object
            try {
                method.invoke(parent, field.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    field.visibleProperty().set(true);
                    label.visibleProperty().set(false);
                }
            }
        });

        getChildren().addAll(label, field);
    }
}
