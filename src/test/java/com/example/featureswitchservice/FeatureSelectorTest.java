package com.example.featureswitchservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.esotericsoftware.yamlbeans.YamlException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeatureSelectorTest {

  @Test
  public void requireInputsShouldDefineInputs() {
    Input actual = FeatureSelector.requireInput(new String[]{"1", "b", "2.3"});
    Input expected = new Input(1, "b", 2.3);
    assertEquals(expected, actual);
  }

  @Test
  public void createFeatureMapShouldCreateMap() throws FileNotFoundException, YamlException {
    Map<String, Map<String, String>> actual = FeatureSelector.createFeatureMap();
    Map<String, Map<String, String>> expected = new HashMap<>();
    HashMap<String, String> feature = new HashMap<>();
    feature.put("os", "android");
    feature.put("version", "2.3");
    feature.put("percentage", "50");
    expected.put("feature_c", feature);
    assertEquals(expected, actual);
  }

  @Test
  public void formatOutputShouldFormatOutput() throws FileNotFoundException, YamlException {
    HashMap<String, Boolean> selected = new HashMap<>();
    selected.put("feature_c", true);
    String expected = "feature_c:true\n";
    String actual = FeatureSelector.formatOutput(selected);
    assertEquals(expected, actual);
  }

  @Test
  public void isEnabledShouldSetEnabled() {
    Map<String, Map<String, String>> features = new HashMap<>();
    HashMap<String, String> feature = new HashMap<>();
    feature.put("os", "android");
    features.put("feature_c", feature);
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isEnabled(new Input(1357246, "android", 2.3), feature);
    assertEquals(true, actual);
  }

  @Test
  public void isEnabledShouldSetDisabled() {
    Map<String, Map<String, String>> features = new HashMap<>();
    HashMap<String, String> feature = new HashMap<>();
    feature.put("os", "android");
    features.put("feature_c", feature);
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isEnabled(new Input(1357246, "ios", 2.3), feature);
    assertEquals(false, actual);
  }

  @Test
  public void isOsEnabledShouldBeTrueIfDefinedAndEqual() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isOsEnabled("a", "a");
    assertEquals(true, actual);
  }

  @Test
  public void isOsEnabledShouldBeTrueIfUndefined() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isOsEnabled(null, "a");
    assertEquals(true, actual);
  }

  @Test
  public void isOsEnabledShouldBeFalseIfDefinedAndUnequal() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isOsEnabled("a", "b");
    assertEquals(false, actual);
  }

  @Test
  public void isVersionEnabledShouldBeTrueIfDefinedAndEqual() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isVersionEnabled("2.3", 2.3);
    assertEquals(true, actual);
  }

  @Test
  public void isVersionEnabledShouldBeTrueIfDefinedAndLessThan() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isVersionEnabled("2", 2.3);
    assertEquals(true, actual);
  }

  @Test
  public void isVersionEnabledShouldBeTrueIfUndefined() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isVersionEnabled(null, 2.3);
    assertEquals(true, actual);
  }

  @Test
  public void isVersionEnabledShouldBeFalseIfDefinedAndGreaterThan() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isVersionEnabled("2.3", 2.0);
    assertEquals(false, actual);
  }

  @Test
  public void isPercentageEnabledShouldBeTrueIfDefinedAndLessThan() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isPercentageEnabled("10", 1);
    assertEquals(true, actual);
  }

  @Test
  public void isPercentageEnabledShouldBeFalseIfDefinedAndGreaterThan() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isPercentageEnabled("10", 11);
    assertEquals(false, actual);
  }

  @Test
  public void isPercentageEnabledShouldBeTrueIfUndefined() {
    Map<String, Map<String, String>> features = new HashMap<>();
    FeatureSelector selector = new FeatureSelector(features);
    boolean actual = selector.isPercentageEnabled(null, 11);
    assertEquals(true, actual);
  }

  @Test
  public void selectShouldEnable() throws FileNotFoundException, YamlException {
    Map<String, Map<String, String>> features = mock(Map.class);
    Map<String, String> feature = new HashMap<>();
    feature.put("os", "android");
    when(features.get("feature_a")).thenReturn(feature);
    Set<String> set = new HashSet<>();
    set.add("feature_a");
    when(features.keySet()).thenReturn(set);
    FeatureSelector selector = new FeatureSelector(features);
    Map<String, Boolean> actual = selector.select(new Input(1357246, "android", 2.3));
    verify(features).get("feature_a");
    Map<String, Boolean> expected = new HashMap<>();
    expected.put("feature_a", true);
    assertEquals(expected, actual);
  }

  @Test
  public void selectShouldDisable() throws FileNotFoundException, YamlException {
    Map<String, Map<String, String>> features = mock(Map.class);
    Map<String, String> feature = new HashMap<>();
    feature.put("os", "android");
    when(features.get("feature_a")).thenReturn(feature);
    Set<String> set = new HashSet<>();
    set.add("feature_a");
    when(features.keySet()).thenReturn(set);
    FeatureSelector selector = new FeatureSelector(features);
    Map<String, Boolean> actual = selector.select(new Input(1357246, "ios", 2.3));
    verify(features).get("feature_a");
    Map<String, Boolean> expected = new HashMap<>();
    expected.put("feature_a", false);
    assertEquals(expected, actual);
  }
}