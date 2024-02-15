package com.javascape;

public class Helper {
    public static double convertToPercentage(double value, double min, double max) {
        return (value - min) / (max - min) * 100;
    }
}
