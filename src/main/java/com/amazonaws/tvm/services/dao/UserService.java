package com.amazonaws.tvm.services.dao;

import com.amazonaws.tvm.domain.User;

import java.util.List;

/**
 * Store and manages users.
 */
public interface UserService {

    public List<User> list();

    public User findById(String id);

}
