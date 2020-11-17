package com.mape.entity;

import java.util.Objects;

public abstract class Entity {

  private static final long NO_ID = -1;

  protected Long id;

  protected Entity(Long id) {
    this.id = id;
  }

  protected Entity() {
    id = NO_ID;
  }

  protected Entity(Builder<?> builder) {
    id = builder.id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isSaved() {
    return id != NO_ID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity entity = (Entity) o;
    return id.equals(entity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  abstract static class Builder<T extends Builder> {

    private long id;

    abstract Entity build();

    public T withId(long id) {
      this.id = id;
      return self();
    }

    protected abstract T self();
  }

}
