package com.javascape.user;

import com.javascape.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserHandler {
    public ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Adds the new user to the users list if there is not already one with that
     * email
     * 
     * @return True if the user was added, false otherwise
     */
    public boolean addUser(User newUser) {
        if (getUser(newUser.getEmail()) != null) {
            Logger.print("User with email " + newUser.getEmail() + " already exists");
            return false;
        }
        users.add(newUser);
        return true;
    }

    /** Removes the specified user from the users list */
    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public boolean removeUser(String email) {
        return users.remove(getUser(email));
    }

    /**
     * Finds the user with the specified email and returns it. Returns null
     * otherwise
     */
    public User getUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        Logger.print("User with email " + email + " not found");
        return null;
    }

    public boolean updateUser(User user, String username, String password, int permissionsLevel, String email) {
        if (user != null && (user.getEmail().equals(email) || getUser(email) == null)) {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPermissions(permissionsLevel);
            return true;
        }
        return false;

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
