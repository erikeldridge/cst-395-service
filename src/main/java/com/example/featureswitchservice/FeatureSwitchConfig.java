package com.example.featureswitchservice;

import com.esotericsoftware.yamlbeans.YamlException;
import com.example.featureselector.FeatureSelector;
import com.example.featureselector.Input;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.util.Map;

@Path("feature_switch_config")
public class FeatureSwitchConfig {
    @QueryParam("id") String id;
    @QueryParam("os") String os;
    @QueryParam("version") String version;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Boolean> get() throws FileNotFoundException, YamlException {
      Input input = FeatureSelector.requireInput(new String[]{id, os, version});
      Map<String, Map<String, String>> features = FeatureSelector.createFeatureMap();
      FeatureSelector selector = new FeatureSelector(features);
      Map<String, Boolean> selected = selector.select(input);
      return selected;
    }
}
