package com.mape.entity;

import java.util.Objects;

public class User extends Entity {

  private final Role role;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String login;
  private String password;

  protected User(UserBuilder builder) {
    super(builder);
    this.role = builder.role;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.email = builder.email;
    this.login = builder.login;
    this.password = builder.password;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
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
    User user = (User) o;
    return role.equals(user.role) &&
        Objects.equals(firstName, user.firstName) &&
        Objects.equals(lastName, user.lastName) &&
        email.equals(user.email) &&
        login.equals(user.login) &&
        password.equals(user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), role, firstName, lastName, email, login, password);
  }

  public Role getRole() {
    return role;
  }

  public String getLogin() {
    return login;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static class UserBuilder extends Entity.Builder<UserBuilder> {

    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;

    public UserBuilder withRole(Role role) {
      this.role = role;
      return self();
    }

    public UserBuilder withFirstName(String firstName) {
      this.firstName = firstName;
      return self();
    }

    public UserBuilder withLastName(String lastName) {
      this.lastName = lastName;
      return self();
    }

    public UserBuilder withEmail(String email) {
      this.email = email;
      return self();
    }

    public UserBuilder withLogin(String login) {
      this.login = login;
      return self();
    }

    public UserBuilder withPassword(String password) {
      this.password = password;
      return self();
    }

    @Override
    public User build() {
      return new User(this);
    }

    @Override
    protected UserBuilder self() {
      return this;
    }
  }
}
