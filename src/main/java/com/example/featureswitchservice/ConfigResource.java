package com.example.featureswitchservice;

import com.esotericsoftware.yamlbeans.YamlException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.util.Map;

@Path("feature_switch_config")
public class ConfigResource {
  @QueryParam("id") String id;
  @QueryParam("os") String os;
  @QueryParam("version") String version;

  Map<String, Map<String, String>> config;

  @Inject
  public ConfigResource(@Named("config") Map<String, Map<String, String>> config) {
    this.config = config;
  }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Boolean> get() throws FileNotFoundException, YamlException {
      Input input = FeatureSelector.requireInput(new String[]{id, os, version});
      FeatureSelector selector = new FeatureSelector(this.config);
      Map<String, Boolean> selected = selector.select(input);
      return selected;
    }
}
