package com.javascape.ui;

import java.lang.reflect.Method;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A class for creating editable labels. The labels can be double clicked to
 * enter an editing menu. nce the editing is done, hit the enter key to save the
 * value.
 * <p>
 * TODO: Save the value on loss of focus
 */
public class EditableLabel extends StackPane {

    /**
     * The Label element for the EditableLabel
     */
    Label label;
    /**
     * The TextField element for the EditableLabel
     */
    TextField field;

    /**
     * The parent object that will be used to call the method
     */
    Object parent;

    /**
     * The method to call for saving the new value
     */
    Method method;

    /**
     * Constructor for the EditableLabel class. This constructor creates a new
     * editable label using the given text. The parent object is the object that
     * will be used to "call" the method (generally just 'this'). The method is
     * the method to call for saving the new value.
     *
     * <p>
     * Example: EditableLabel nameLabel = new EditableLabel(getName(), this,
     * this.getClass().getMethod("setName", String.class));
     *
     * @param text The text to display on the label when intialized.
     * @param parent The object that will be used to call the method. Generally
     * 'this'.
     * @param method The method to call for saving the new value. Must contain
     * only a single String parameter.
     */
    public EditableLabel(String text, Object parent, Method method) {
        this.parent = parent;
        this.method = method;

        label = new Label(text);
        field = new TextField(text);
        field.visibleProperty().set(false);
        field.cursorProperty().setValue(Cursor.HAND);

        field.setOnAction(e -> {
            saveValue();
        });

        label.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                label.visibleProperty().set(false);
                field.visibleProperty().set(true);
                
                Platform.runLater(() -> field.requestFocus());
                field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        saveValue();
                    }
                });
            }
        });

        getChildren().addAll(label, field);
    }

    private void saveValue() {
        label.setText(field.getText());
        field.visibleProperty().set(false);
        label.visibleProperty().set(true);

        // Set the new name of the object
        try {
            method.invoke(parent, field.getText());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
