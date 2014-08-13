package com.amazonaws.tvm.domain;

/**
 * A tentative user creation request.
 */
public class UserCreationRequest {

    private String name;
    private String password;
    private boolean enabled;

    public UserCreationRequest() {
        // for Jackson
    }

    public UserCreationRequest(String name, String password, boolean enabled) {
        this.name = name;
        this.password = password;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
