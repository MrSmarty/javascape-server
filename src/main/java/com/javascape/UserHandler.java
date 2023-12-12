package com.javascape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserHandler {
    private ObservableList<User> users = FXCollections.observableArrayList();

    /** Adds the new user to the users list */
    public void addUser(User newUser) {
        users.add(newUser);
    }

    /** Removes the specified user from the users list */
    public void removeUser(User user) {
        users.remove(user);
    }

    /** Finds the user with the specified email and returns it. Returns null otherwise */
    public User getUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        Logger.print("User with email " + email + " not found");
        return null;
    }

    /** Returns an ObservableList of all the Users */
    public ObservableList<User> getAllUsers() {
        return users;
    }

    /** Verifies that the user is indeed a user */
    public boolean login(String email, String password) {
        User user = getUser(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}
