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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.tvm.Configuration;
import com.amazonaws.tvm.custom.DeviceAuthentication;
import com.amazonaws.tvm.custom.UserAuthentication;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Helps debugging the SimpleDB state, because AWS doesn't provide a lot of tools to query SDB using a console.
 */
@Path("/debug")
public class DebugResource {

    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public String listUsers() {
        UserAuthentication userAuthentication = new UserAuthentication();
        List<String> users = userAuthentication.listUsers();

        StringBuilder builder = new StringBuilder();
        for (String currUser : users) {
            builder.append(currUser);
            builder.append('\n');
        }

        return builder.toString();
    }

    @GET
    @Path("/device")
    @Produces(MediaType.TEXT_PLAIN)
    public String listDevices() {
        DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
        List<String> devices = deviceAuthentication.listDevices();

        StringBuilder builder = new StringBuilder();
        for (String currDevice : devices) {
            builder.append(currDevice);
            builder.append('\n');
        }

        return builder.toString();
    }

    @GET
    @Path("/domain")
    @Produces(MediaType.TEXT_PLAIN)
    public String listDomains() {
        AmazonSimpleDBClient sdb = new AmazonSimpleDBClient(new BasicAWSCredentials(Configuration.AWS_ACCESS_KEY_ID, Configuration.AWS_SECRET_KEY));
        sdb.setEndpoint(Configuration.SIMPLEDB_ENDPOINT);

        StringBuilder builder = new StringBuilder();
        String nextToken = null;
        do {
            ListDomainsRequest ldr = new ListDomainsRequest();
            ldr.setNextToken(nextToken);

            ListDomainsResult result = sdb.listDomains(ldr);
            builder.append(result.getDomainNames());
            builder.append('\n');

            nextToken = result.getNextToken();
        } while (nextToken != null);

        return builder.toString();
    }

}
