package com.amazonaws.tvm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An application user.
 */
public class User {

    private String id;
    private String hashedPassword;
    private boolean enabled;

    public User(String id, String hashedPassword, boolean enabled) {
        this.id = id;
        this.hashedPassword = hashedPassword;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
