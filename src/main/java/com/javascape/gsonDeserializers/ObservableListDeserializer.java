package com.javascape.gsonDeserializers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.javascape.User;
import com.javascape.chronjob.Chronjob;
import com.javascape.chronjob.ConditionalJob;
import com.javascape.Household;
import com.javascape.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The deserializer for the ObservableLists */
public class ObservableListDeserializer implements JsonDeserializer<ObservableList<?>> {

    transient HashMap<String, String> classMap = new HashMap<String, String>();

    @Override
    public ObservableList<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        getClasses();

        ObservableList<?> list = FXCollections.observableArrayList();

        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            // If the element has an Email, then it's a user
            if (jsonElement.getAsJsonObject().has("email")) {
                list.add(context.deserialize(jsonElement, User.class));
            } else if (jsonElement.getAsJsonObject().has("householdName")) {
                list.add(context.deserialize(jsonElement, Household.class));
            } else if (jsonElement.getAsJsonObject().has("type")) {
                try {
                    String s = jsonElement.getAsJsonObject().get("type").toString();
                    Class<?> tempClass = Class
                            .forName(classMap.get(s.substring(1, s.length() - 1)));
                    list.add(context.deserialize(jsonElement, tempClass));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (jsonElement.getAsJsonObject().has("conditions")) {
                list.add(context.deserialize(jsonElement, ConditionalJob.class));

            } else if (jsonElement.getAsJsonObject().has("commands")) {
                list.add(context.deserialize(jsonElement, Chronjob.class));
            }
        }

        return list;
    }

    /** Pull the classes from the recievers file */
    private void getClasses() {
        try {
            Scanner scan = new Scanner(new File(Settings.storageLocation + "recievers.map"));
            while (scan.hasNextLine()) {
                String[] item = scan.nextLine().split(" ");

                classMap.put(item[0], "com.javascape.recievers." + item[1]);
            }
        } catch (IOException e) {
            // Logger.error("Error trying to fetch recievers from reciever map");
        }

    }
}
