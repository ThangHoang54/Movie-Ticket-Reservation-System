package org.example.finalexam.model;

import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class Theater {
    private int id;
    private String name;
    private String address;
    private List<Screen> screens;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public List<Screen> getScreens() {
        return screens;
    }
    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }
    private Theater(Theater.Builder builder) {
        id = builder.id;
        name = builder.name;
        address = builder.address;
        screens = builder.screens;
    }

    public static class Builder {
        private int id;
        private String name;
        private String address;
        private List<Screen> screens;

        public Builder() {}

        public Theater.Builder setID(int id) {
            this.id = id;
            return this;
        }
        public Theater.Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Theater.Builder setAddress(String address) {
            this.address = address;
            return this;
        }
        public Theater.Builder setScreens(List<Screen> screens) {
            this.screens = screens;
            return this;
        }
        public Theater build() {
            return new Theater(this);
        }
    }
}
