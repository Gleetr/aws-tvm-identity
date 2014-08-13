/*
 * Copyright 2014 Gleetr SAS or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Long count() {
        Long count = this.userService.count();
        return count;
    }

}
