package com.javascape.gsonDeserializers;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.javascape.User;
import com.javascape.chronjob.Chronjob;
import com.javascape.chronjob.ConditionalJob;
import com.javascape.receivers.Receiver;
import com.javascape.Household;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The deserializer for the ObservableLists */
public class ObservableListDeserializer implements JsonDeserializer<ObservableList<?>> {

    @Override
    public ObservableList<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ObservableList<?> list = FXCollections.observableArrayList();

        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            // If the element has an Email, then it's a user
            if (jsonElement.getAsJsonObject().has("email")) {
                list.add(context.deserialize(jsonElement, User.class));
            } else if (jsonElement.getAsJsonObject().has("householdName")) {
                list.add(context.deserialize(jsonElement, Household.class));
            } else if (jsonElement.getAsJsonObject().has("type")) {
                list.add(context.deserialize(jsonElement, Receiver.class));
            } else if (jsonElement.getAsJsonObject().has("conditions")) {
                list.add(context.deserialize(jsonElement, ConditionalJob.class));
            } else if (jsonElement.getAsJsonObject().has("commands")) {
                list.add(context.deserialize(jsonElement, Chronjob.class));
            }
        }

        return list;
    }
}
