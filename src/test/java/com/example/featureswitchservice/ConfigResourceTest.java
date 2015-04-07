package com.example.featureswitchservice;

import com.esotericsoftware.yamlbeans.YamlException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;
import com.sun.jersey.guice.JerseyServletModule;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConfigResourceTest {
  Server server;
  WebTarget target;

  @Before
  public void setUp() throws Exception {
    GuiceServletContextListener listener = new GuiceServletContextListener() {

      @Override
      protected Injector getInjector() {
        return Guice.createInjector(Modules.override(new ProductionModule())
            .with(new JerseyServletModule() {
              @Provides
              @Named("config")
              Map<String, Map<String, String>> get() throws FileNotFoundException, YamlException {
                return FeatureSelector.createFeatureMap();
              }
            }));
      }
    };
    server = Main.createServer(Main.DEFAULT_PORT, listener);
    server.start();
    Client client = ClientBuilder.newClient();
    target = client.target(String.format("http://localhost:%d/", Main.DEFAULT_PORT));
  }

  @After
  public void tearDown() throws Exception {
    server.stop();
  }

  @Test
  public void testGet() throws IOException {
    final String json = target
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
