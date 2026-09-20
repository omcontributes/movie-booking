package com.example.moviebooking.repository;

import com.example.moviebooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCode(String bookingCode);

    boolean existsByBookingCode(String bookingCode);
}