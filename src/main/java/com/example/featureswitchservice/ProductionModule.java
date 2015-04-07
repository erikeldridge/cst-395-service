package com.example.featureswitchservice;

import com.esotericsoftware.yamlbeans.YamlException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.inject.Named;
import java.io.FileNotFoundException;
import java.util.Map;

public class ProductionModule extends JerseyServletModule {

  @Provides
  @Singleton
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Provides
  @Singleton
  JacksonJsonProvider jacksonJsonProvider(ObjectMapper mapper) {
    return new JacksonJsonProvider(mapper);
  }

  @Provides
  @Named("config")
  Map<String, Map<String, String>> get() throws FileNotFoundException, YamlException {
    return FeatureSelector.createFeatureMap();
  }

  @Override
  protected void configureServlets() {
    bind(ConfigResource.class);
    serve("/*").with(GuiceContainer.class);
  }
}