package com.it193.dogadoptionapp.model;

import java.util.Objects;

public class Account {

    private long id;
    private String username;
    private String email;
    private String password;
    private String sessionAuthString;

    public Account() {
    }

    public Account(long id, String username, String email, String password, String sessionAuthString) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.sessionAuthString = sessionAuthString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionAuthString() {
        return sessionAuthString;
    }

    public void setSessionAuthString(String sessionAuthString) {
        this.sessionAuthString = sessionAuthString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(username, account.username) && Objects.equals(email, account.email) && Objects.equals(password, account.password) && Objects.equals(sessionAuthString, account.sessionAuthString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, sessionAuthString);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sessionAuthString='" + sessionAuthString + '\'' +
                '}';
    }
}
