package com.javascape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HouseholdHandler {
    private ObservableList<Household> households = FXCollections.observableArrayList();
    private int currentID = 0;

    public void createHousehold(String name) {
        households.add(new Household(currentID++, name));
    }

    /** Returns the household with the specified id, otherwise returns null */
    public Household getHouseholdById(int id) {
        for (Household h : households) {
            if (h.getID() == id) {
                return h;
            }
        }
        return null;
    }

    public ObservableList<Household> getHouseholdList() {
        return households;
    }

    public int getCurrentID() {
        return currentID;
    }
}
