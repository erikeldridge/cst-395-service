package com.example.featureswitchservice;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class App extends ResourceConfig {
  public App() {
    register(JacksonFeature.class);
  }
}