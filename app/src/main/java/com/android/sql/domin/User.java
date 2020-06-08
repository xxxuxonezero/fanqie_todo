package com.android.sql.domin;

public class User {
    Integer id;
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}
