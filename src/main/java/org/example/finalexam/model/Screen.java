package org.example.finalexam.model;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class Screen {
    private int id;
    private int timing;
    private String movie_name;
    private int seat_available;
    private Theater theater;

    public int getId() {
        return id;
    }
    public int getTiming() {
        return timing;
    }
    public String getMovie_name() {
        return movie_name;
    }
    public int getSeat_available() {
        return seat_available;
    }
    public Theater getTheater() {
        return theater;
    }

    private Screen(Screen.Builder builder) {
        id = builder.id;
        timing = builder.timing;
        movie_name = builder.movie_name;
        seat_available = builder.seat_available;
        theater = builder.theater;
    }

    public static class Builder {
        private int id;
        private int timing;
        private String movie_name;
        private int seat_available;
        private Theater theater;

        public Builder() {}

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Builder setTiming(int timing) {
            this.timing = timing;
            return this;
        }
        public Builder setMovieName(String movie_name) {
            this.movie_name = movie_name;
            return this;
        }
        public Builder setSeatAvailable(int seat_available) {
            this.seat_available = seat_available;
            return this;
        }
        public Builder setTheater(Theater theater) {
            this.theater = theater;
            return this;
        }
        public Screen build() {
            return new Screen(this);
        }
    }
}
