package com.amazonaws.tvm.services.rest;

import com.amazonaws.tvm.domain.User;
import com.amazonaws.tvm.services.dao.UserService;
import com.amazonaws.tvm.services.dao.UserSimpleDBService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST operations on Users.
 */
@Path("/user")
public class UserResource {

    private UserService userService;

    public UserResource() {
        this.userService = new UserSimpleDBService();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
        List<User> users = this.userService.list();
        return users;
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findById(@PathParam("id") String id) {
        User user = this.userService.findById(id);
        return user;
    }

}
