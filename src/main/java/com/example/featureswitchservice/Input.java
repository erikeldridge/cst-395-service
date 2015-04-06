package com.example.featureswitchservice;

public class Input {
  public final Integer id;
  public final String os;
  public final Double version;

  public Input(Integer id, String os, Double version) {
    this.id = id;
    this.os = os;
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Input input = (Input) o;

    if (!id.equals(input.id)) return false;
    if (!os.equals(input.os)) return false;
    if (!version.equals(input.version)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + os.hashCode();
    result = 31 * result + version.hashCode();
    return result;
  }
}