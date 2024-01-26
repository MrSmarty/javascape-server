package com.javascape.gsonDeserializers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.javascape.Settings;
import com.javascape.receivers.Receiver;

public class ReceiverDeserializer implements JsonDeserializer<Receiver> {

    transient HashMap<String, String> classMap = new HashMap<String, String>();

    @Override
    public Receiver deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        getClasses();
        try {
            String s = json.getAsJsonObject().get("type").toString();
            Class<?> tempClass = Class
                    .forName(classMap.get(s.substring(1, s.length() - 1)));
            return context.deserialize(json, tempClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Pull the classes from the receivers file */
    private void getClasses() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "receivers.map"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                classMap.put(item[0], "com.javascape.receivers." + item[1]);
            }
        } catch (IOException e) {
            // Logger.error("Error trying to fetch receivers from receiver map");
        }

    }

}
