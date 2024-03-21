package com.javascape.user;

/** The User class */
public class User {

    /** The email associated with the user. The email is used for all login info. */
    public String email;

    /** The username for the user */
    public String username;

    /** The password for the user */
    public String password;

    /** The permissions level granted to the user. */
    public int permissionsLevel;

    /**
     * The ID of the household tied to the User. Used instead of a reference to the
     * household to prevent the storing of a lot of unnecessary data.
     */
    public int householdID = -1;

    /** The Preferences that are tied to this User. */
    public Preferences preferences;

    /** Initialize a user with the given values */
    public User(String username, String password, int permissions, String email) {
        this.username = username;
        this.password = password;
        this.permissionsLevel = permissions;
        this.email = email;
        householdID = -1;
        preferences = new Preferences();
    }

    /** Initialize a user with the given values */
    public User(String username, String password, int permissions, String email, int householdID) {
        this.username = username;
        this.password = password;
        this.permissionsLevel = permissions;
        this.email = email;
        this.householdID = householdID;
        preferences = new Preferences();
    }

    /**
     * 
     * @return The username of the user
     */
    public String getUsername() {
        return username;
    }

    /** Sets the username of the User */
    public void setUsername(String name) {
        username = name;
    }

    /** @return The password of the user */
    public String getPassword() {
        return password;
    }

    /** Sets the password of the User */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return True if the user is an admin */
    public int getPermissionsLevel() {
        return permissionsLevel;
    }

    /** Set the admin status of the User */
    public void setPermissions(int value) {
        permissionsLevel = value;
    }

    /** @return The email of the user */
    public String getEmail() {
        return email;
    }

    /** Set the email of the User */
    public void setEmail(String email) {
        this.email = email;
    }

    public void setHosueholdID(int id) {
        this.householdID = id;
    }

    public int getHouseholdID() {
        return this.householdID;
    }

    // public Image getUserImage() {
    // //System.out.println(getClass().getResourceAsStream("/icons/defaultUser_x64.png").toString());
    // //return new Image("/main/resources/icons/defaultUser_x64.png");
    // return new
    // Image(getClass().getResourceAsStream("/main/resources/icons/defaultUser_x64.png"));
    // }

    /** Returns the email of the user */
    @Override
    public String toString() {
        return email;
    }
}
