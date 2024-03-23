package ru.vladushik.vkusvillstoremanagement.entity;

public class User {
    private Integer id;
    private String username;
    private String login;
    private String password;
    private String secret;

    public User(Integer id, String username, String login, String password, String secret) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.password = password;
        this.secret = secret;
    }

    public User() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return username;
    }
}
