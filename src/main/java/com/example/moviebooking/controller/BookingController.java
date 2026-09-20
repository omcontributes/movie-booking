package com.example.moviebooking.controller;

import com.example.moviebooking.dto.BookingRequest;
import com.example.moviebooking.dto.BookingResponse;
import com.example.moviebooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request));
    }

    @GetMapping("/{code}")
    public BookingResponse get(@PathVariable String code) {
        return bookingService.get(code);
    }

    @DeleteMapping("/{code}")
    public BookingResponse cancel(@PathVariable String code) {
        return bookingService.cancel(code);
    }
}