package com.amazonaws.tvm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An application user.
 */
public class User {

    private String id;
    private String name;
    private String hashedPassword;
    private boolean enabled;

    public User() {
        // for Jackson
    }

    public User(String id, String name, String hashedPassword, boolean enabled) {
        this.id = id;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
