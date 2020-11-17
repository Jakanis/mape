package com.mape.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Conference extends Entity {

  private final String name;
  private final String address;
  private final LocalDateTime time;
  private final int maxVisitors;
  private final int registeredVisitors;
  private final List<Lecture> lectures;

  protected Conference(ConferenceBuilder builder) {
    super(builder.id);
    this.name = builder.name;
    this.address = builder.address;
    this.time = builder.time;
    this.maxVisitors = builder.maxVisitors;
    this.registeredVisitors = builder.registeredVisitors;
    this.lectures = builder.lectures;

  }

  public static ConferenceBuilder builder() {
    return new ConferenceBuilder();
  }

  public List<Lecture> getLectures() {
    return lectures;
  }

  public String getName() {
    return name;
  }

  public int getRegisteredVisitors() {
    return registeredVisitors;
  }

  public String getAddress() {
    return address;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public int getMaxVisitors() {
    return maxVisitors;
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
    Conference that = (Conference) o;
    return maxVisitors == that.maxVisitors &&
        registeredVisitors == that.registeredVisitors &&
        name.equals(that.name) &&
        address.equals(that.address) &&
        time.equals(that.time) &&
        Objects.equals(lectures, that.lectures);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, address, time, maxVisitors, registeredVisitors, lectures);
  }

  public static class ConferenceBuilder extends Entity.Builder<ConferenceBuilder> {


    private long id = -1;
    private String name;
    private String address;
    private LocalDateTime time;
    private int maxVisitors;
    private int registeredVisitors;
    private List<Lecture> lectures;

    @Override
    public Conference build() {
      return new Conference(this);
    }

    @Override
    public ConferenceBuilder withId(long id) {
      this.id = id;
      return this;
    }

    public ConferenceBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public ConferenceBuilder withAddress(String address) {
      this.address = address;
      return this;
    }

    public ConferenceBuilder withTime(LocalDateTime time) {
      this.time = time;
      return this;
    }

    public ConferenceBuilder withMaxVisitors(int maxVisitors) {
      this.maxVisitors = maxVisitors;
      return this;
    }

    public ConferenceBuilder withRegisteredVisitors(int registeredVisitors) {
      this.registeredVisitors = registeredVisitors;
      return this;
    }

    public ConferenceBuilder withLectures(List<Lecture> lectures) {
      this.lectures = lectures;
      return this;
    }

    @Override
    protected ConferenceBuilder self() {
      return this;
    }
  }
}
