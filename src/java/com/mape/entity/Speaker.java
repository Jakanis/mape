package com.mape.entity;

import java.util.Objects;

public class Speaker extends User {

  private final double averageRate;

  private Speaker(SpeakerBuilder builder) {
    super(builder);
    this.averageRate = builder.rate;
  }

  public static SpeakerBuilder speakerBuilder() {
    return new Speaker.SpeakerBuilder();
  }

  public double getAverageRate() {
    return averageRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Speaker speaker = (Speaker) o;
    return Double.compare(speaker.averageRate, averageRate) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), averageRate);
  }

  public static class SpeakerBuilder extends UserBuilder {

    private double rate;

    public SpeakerBuilder withRate(double rate) {
      this.rate = rate;
      return this;
    }

    @Override
    public Speaker build() {
      return new Speaker(this);
    }

    @Override
    protected SpeakerBuilder self() {
      return this;
    }
  }
}
