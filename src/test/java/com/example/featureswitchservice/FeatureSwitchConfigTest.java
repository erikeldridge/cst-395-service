package com.example.featureswitchservice;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FeatureSwitchConfigTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(FeatureSwitchConfig.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("feature_switch_config").request().get(String.class);

        assertEquals("Hello, Heroku!", responseMsg);
    }
}
