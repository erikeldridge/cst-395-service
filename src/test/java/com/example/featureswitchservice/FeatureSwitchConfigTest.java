package com.example.featureswitchservice;

import javax.ws.rs.core.Application;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FeatureSwitchConfigTest extends JerseyTest {

    @Override
    protected Application configure() {
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      return new ResourceConfig(FeatureSwitchConfig.class);
    }

    @Test
    public void testGet() throws IOException {
      final String json = target()
          .path("feature_switch_config")
          .queryParam("id", "123")
          .queryParam("os", "android")
          .queryParam("version", "2.3")
          .request()
          .get(String.class);
      HashMap<String, Boolean> expected = new HashMap<>();
      expected.put("feature_c", true);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Boolean> actual = mapper.readValue(json, Map.class);
      assertEquals(expected, actual);
    }
}
