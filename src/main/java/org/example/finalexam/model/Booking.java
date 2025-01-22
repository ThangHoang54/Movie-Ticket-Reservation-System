package org.example.finalexam.model;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class Booking {
    private int id;
    private int reserved_seat;
    private Screen screen;
    private User user;


    public int getId() {
        return id;
    }
    public int getReserved_seat() {
        return reserved_seat;
    }
    public Screen getScreen() {
        return screen;
    }
    public User getUser() {
        return user;
    }

    private Booking(Booking.Builder builder) {
        id = builder.id;
        reserved_seat = builder.reserved_seat;
        user = builder.user;
        screen = builder.screen;
    }

    public static class Builder {
        private int id;
        private int reserved_seat;
        private Screen screen;
        private User user;

        public Builder() {}

        public Booking.Builder setID(int id) {
            this.id = id;
            return this;
        }
        public Booking.Builder setReserved_seat(int reserved_seat) {
            this.reserved_seat = reserved_seat;
            return this;
        }
        public Booking.Builder setScreen(Screen screen) {
            this.screen = screen;
            return this;
        }
        public Booking.Builder setUser(User user) {
            this.user = user;
            return this;
        }
        public Booking build() {
            return new Booking(this);
        }
    }
}
