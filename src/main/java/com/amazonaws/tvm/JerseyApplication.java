package com.amazonaws.tvm;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey bootstrap.
 */
@ApplicationPath("management")
public class JerseyApplication extends ResourceConfig {

    public JerseyApplication() {
        packages(this.getClass().getPackage().getName() + ".services.rest").register(JacksonFeature.class);
    }

}
