package com.amazonaws.tvm.services.dao;

import com.amazonaws.tvm.domain.User;
import com.amazonaws.tvm.domain.UserCreationRequest;

import java.util.List;

/**
 * Store and manages users.
 */
public interface UserService {

    public List<User> list();

    public User findById(String id);

    public User findByName(String name);

    public void deleteById(String id);

    public void add(UserCreationRequest user, String endpoint);

    public Long countDevices();

}
