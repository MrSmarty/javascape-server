package com.javascape.user;

import java.util.ArrayList;

public class Permissions {
    /** All permissions */
    public static final int ADMIN = 0;

    /** All permissions in their household */
    public static final int HOUSEHOLD_HEAD = 1;

    /** Some permissions within their household */
    public static final int USER = 2;

    /** Can view data */
    public static final int GUEST = 3;

    /**
     * Converts the given permissions string to an integer
     * 
     * @param permissions The permissions string
     * @return The integer representation of the permissions
     */
    public static int toInt(String permissions) {
        switch (permissions) {
            case "Admin":
                return ADMIN;
            case "Household Head":
                return HOUSEHOLD_HEAD;
            case "User":
                return USER;
            default:
                return GUEST;
        }
    }

    /**
     * Converts the given permissions integer to a string
     * 
     * @param permissions The permissions integer
     * @return The string representation of the permissions
     */
    public static String toString(int permissions) {
        switch (permissions) {
            case ADMIN:
                return "Admin";
            case HOUSEHOLD_HEAD:
                return "Household Head";
            case USER:
                return "User";
            default:
                return "Guest";
        }
    }

    public static ArrayList<String> getPermissionsList() {
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add(toString(ADMIN));
        permissions.add(toString(HOUSEHOLD_HEAD));
        permissions.add(toString(USER));
        permissions.add(toString(GUEST));
        return permissions;
    }
}
