package com.javascape;

public class Helper {
    public static double convertToPercentage(double value, double min, double max) {
        return (value - min) / (max - min) * 100;
    }

    public static double convertToPercentage(String valueString, double min, double max) {
        double value = Double.parseDouble(valueString);
        return (value - min) / (max - min) * 100;
    }
}
