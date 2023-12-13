package com.javascape.gsonDeserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.javascape.sensors.Sensor;

public class SensorDeserializer implements JsonDeserializer<Sensor> {

    @Override
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            String className = json.getAsJsonObject().get("className").getAsString();
            System.out.println(className);
            Class<?> tempClass = Class.forName("com.javascape.sensors." + className);
            return context.deserialize(json, tempClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
