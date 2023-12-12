package com.javascape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Household {
    int householdID;
    String householdName;

    ObservableList<User> members = FXCollections.observableArrayList();

    /** Creates a new household with specified ID and Name */
    public Household(int id, String name) {
        householdID = id;
        householdName = name;
    }

    /**
     * Adds the user to the household
     * 
     * @return returns true if the user was added,
     *         returns false if the user was not
     */
    public boolean addUser(User user) {
        if (!members.contains(user)) {
            members.add(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the specified User from the household
     * 
     * @return returns true if the user was removed, and false if not
     */
    public boolean removeUser(User user) {
        if (members.contains(user)) {
            members.remove(user);
            return true;
        } else {
            return false;
        }
    }

    public int getID() {
        return householdID;
    }
}
