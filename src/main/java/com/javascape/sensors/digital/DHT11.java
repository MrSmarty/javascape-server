package com.javascape.sensors.digital;
import javafx.scene.layout.GridPane;

public class DHT11 extends DigitalSensor {


    public DHT11(String receiverID, int index) {
        super(receiverID, index);
    }

    @Override
    public GridPane getSensorPane() {
        GridPane pane = new GridPane();

        return pane;
    }

    @Override
    public String getCommand() {
        return "DHT11";
    }

    @Override
    public void addValue(String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addValue'");
    }
    
}
