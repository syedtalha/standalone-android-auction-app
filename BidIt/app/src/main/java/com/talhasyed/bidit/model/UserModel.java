package com.talhasyed.bidit.model;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class UserModel extends BaseModel {
    private String name;
    private String userName;
    private String password;

    public UserModel() {
    }

    private UserModel(Builder builder) {
        setName(builder.name);
        setUserName(builder.userName);
        setPassword(builder.password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final class Builder {
        private String name;
        private String userName;
        private String password;

        public Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withUserName(String val) {
            userName = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
