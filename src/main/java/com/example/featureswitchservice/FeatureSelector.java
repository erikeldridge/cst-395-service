package com.example.featureswitchservice;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FeatureSelector {
  Map<String, Map<String, String>> features;

  public FeatureSelector(Map<String, Map<String, String>> features) {
    this.features = features;
  }

  /**
   * Main method
   * @param args selection args
   * @throws IOException when feature file cannot be read
   */
  public static void main( String[] args ) throws IOException {
    Input input = requireInput(args);
    Map<String, Map<String, String>> features = createFeatureMap();
    FeatureSelector selector = new FeatureSelector(features);
    Map<String, Boolean> selected = selector.select(input);
    System.out.println(formatOutput(selected));
  }

  public static Input requireInput(String[] args) {
    Integer id = Integer.valueOf(args[0]);
    String os = args[1];
    Double version = Double.valueOf(args[2]);
    return new Input(id, os, version);
  }

  public static Map<String, Map<String, String>> createFeatureMap() throws FileNotFoundException, YamlException {
    String path = FeatureSelector.class.getClassLoader().getResource("features.yml").getFile();
    YamlReader features = new YamlReader(new FileReader(path));
    return features.read(Map.class);
  }

  public Map<String, Boolean> select(Input input) {
    Map<String, Boolean> selected = new HashMap<>();
    for (String name : features.keySet()) {
      Map<String, String> feature = features.get(name);
      selected.put(name, isEnabled(input, feature));
    }
    return selected;
  }

  public boolean isEnabled(Input input, Map<String, String> feature) {
    return isOsEnabled(feature.get("os"), input.os)
        && isVersionEnabled(feature.get("version"), input.version)
        && isPercentageEnabled(feature.get("percentage"), input.id);
  }

  public boolean isOsEnabled(String featureOs, String inputOs) {
    return featureOs == null || featureOs.equals(inputOs);
  }

  public boolean isVersionEnabled(String featureVersion, Double inputVersion) {
    return featureVersion == null || Double.valueOf(featureVersion).compareTo(inputVersion) <= 0;
  }

  public boolean isPercentageEnabled(String featurePercentage, Integer inputId) {
    return featurePercentage == null || inputId % 100 < Integer.valueOf(featurePercentage);
  }

  public static String formatOutput(Map<String, Boolean> selected) throws FileNotFoundException, YamlException {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, Boolean> feature : selected.entrySet()) {
      builder
          .append(feature.getKey())
          .append(":")
          .append(feature.getValue())
          .append("\n");
    }
    return builder.toString();
  }
}