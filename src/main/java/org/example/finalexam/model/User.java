package org.example.finalexam.model;

import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class User {
    private int id;
    private String name;
    private String contact_info;
    private String booking_history;
    private List<Booking> bookings;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getContact_info() {
        return contact_info;
    }
    public String getBooking_history() {
        return booking_history;
    }
    public List<Booking> getBookings() {
        return bookings;
    }

    private User (User.Builder builder) {
        id = builder.id;
        name = builder.name;
        contact_info = builder.contact_info;
        booking_history = builder.booking_history;
        bookings = builder.bookings;
    }

    public static class Builder {
        private int id;
        private String name;
        private String contact_info;
        private String booking_history;
        private List<Booking> bookings;

        public Builder() {}

        public User.Builder setID(int id) {
            this.id = id;
            return this;
        }
        public User.Builder setName(String name) {
            this.name = name;
            return this;
        }
        public User.Builder setContactInfo(String contact_info) {
            this.contact_info = contact_info;
            return this;
        }
        public User.Builder setBookingHistory(String booking_history) {
            this.booking_history = booking_history;
            return this;
        }
        public User.Builder setBookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }
        public User build() {
            return new User(this);
        }
    }
}
