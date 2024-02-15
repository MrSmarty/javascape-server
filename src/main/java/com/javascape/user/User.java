package com.javascape.user;


/** The User class */
public class User {
    // TODO: User Preferences?

    public String email;

    public String username;

    public String password;

    public int permissionsLevel;

    public int householdID = -1;

    /** Initialize a user with the given values */
    public User(String username, String password, int permissions, String email) {
        this.username = username;
        this.password = password;
        this.permissionsLevel = permissions;
        this.email = email;
        householdID = -1;
    }

    /** Initialize a user with the given values */
    public User(String username, String password, int permissions, String email, int householdID) {
        this.username = username;
        this.password = password;
        this.permissionsLevel = permissions;
        this.email = email;
        this.householdID = householdID;
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
    //     //System.out.println(getClass().getResourceAsStream("/icons/defaultUser_x64.png").toString());
    //     //return new Image("/main/resources/icons/defaultUser_x64.png");
    //     return new Image(getClass().getResourceAsStream("/main/resources/icons/defaultUser_x64.png"));
    // }

    @Override
    public String toString() {
        return email;
    }
}
