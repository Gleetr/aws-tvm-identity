package com.amazonaws.tvm.services.rest;

import com.amazonaws.tvm.Utilities;
import com.amazonaws.tvm.domain.User;
import com.amazonaws.tvm.domain.UserCreationRequest;
import com.amazonaws.tvm.services.dao.UserService;
import com.amazonaws.tvm.services.dao.UserSimpleDBService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findByName(@PathParam("name") String name) {
        User user = this.userService.findByName(name);
        return user;
    }

    @DELETE
    @Path("/id/{id}")
    public void delete(@PathParam("id") String id) {
        this.userService.deleteById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(UserCreationRequest userRequested, @Context HttpServletRequest request) {
        String endpoint = Utilities.getEndPoint(request);
        this.userService.add(userRequested, endpoint);
    }

}
