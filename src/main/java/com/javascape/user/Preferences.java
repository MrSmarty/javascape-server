package com.javascape.user;

public class Preferences {
    /** The timezone in which the user resides */
    public String timezone;

    /** Initializes a preferences object with the US/Central timezone */
    public Preferences() {
        timezone = "US/Central";
    }

    /** Returns the timezone */
    public String getTimezone() {
        return timezone;
    }

    /** Sets the timezone to the value provided */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
