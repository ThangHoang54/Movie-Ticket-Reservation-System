package org.example.finalexam.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class GenerateInput {
    /**
     * Returns a suitable available seat number from the total seats, excluding the booked seats.
     *
     * @param totalSeats   the total number of seats
     * @param bookedSeats  the list of seats that are already booked
     * @return a suitable available seat number
     * @throws IllegalArgumentException if there are no available seats
     */
    public static int getRandomAvailableSeatNumber(int totalSeats, List<Integer> bookedSeats) {
        List<Integer> availableSeats = new ArrayList<>();
        // Populate availableSeats with seats that are not booked
        for (int i = 1; i <= totalSeats; i++) {
            if (!bookedSeats.contains(i)) {
                availableSeats.add(i);
            }
        }
        // If there are no available seats
        if (availableSeats.isEmpty()) {
            throw new IllegalArgumentException("No available seats");
        }
        // Select a suitable available seat
        return availableSeats.getFirst();
    }

    public static String getSimplifyFullName(String fullName) {
        return fullName.toLowerCase().replaceAll(" ", "");
    }
}
