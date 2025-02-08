package org.example.finalexam.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateInput {
    /**
     * Returns a random available seat number from the total seats, excluding the booked seats.
     *
     * @param totalSeats   the total number of seats
     * @param bookedSeats  the list of seats that are already booked
     * @return a random available seat number
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
        // Select a random available seat
        Random random = new Random();
        return availableSeats.get(random.nextInt(availableSeats.size()));
    }

    public static String getSimplifyFullName(String fullName) {
        return fullName.toLowerCase().replaceAll(" ", "");
    }
}
