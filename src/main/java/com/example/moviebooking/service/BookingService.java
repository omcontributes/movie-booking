package com.example.moviebooking.service;

import com.example.moviebooking.dto.BookingRequest;
import com.example.moviebooking.dto.BookingResponse;
import com.example.moviebooking.exception.BadRequestException;
import com.example.moviebooking.exception.ConflictException;
import com.example.moviebooking.exception.NotFoundException;
import com.example.moviebooking.model.BookedSeat;
import com.example.moviebooking.model.Booking;
import com.example.moviebooking.model.BookingStatus;
import com.example.moviebooking.model.MovieShow;
import com.example.moviebooking.repository.BookedSeatRepository;
import com.example.moviebooking.repository.BookingRepository;
import com.example.moviebooking.repository.MovieShowRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class BookingService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;

    private final SecureRandom random = new SecureRandom();
    private final BookingRepository bookingRepository;
    private final MovieShowRepository showRepository;
    private final BookedSeatRepository bookedSeatRepository;

    public BookingService(BookingRepository bookingRepository,
                          MovieShowRepository showRepository,
                          BookedSeatRepository bookedSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.bookedSeatRepository = bookedSeatRepository;
    }

    @Transactional
    public BookingResponse book(BookingRequest request) {
        MovieShow show = showRepository.findById(request.showId())
                .orElseThrow(() -> new NotFoundException("Show not found"));

        if (show.getShowTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("This show has already started");
        }

        Set<String> seats = normalizeAndValidate(show, request.seats());

        // Friendly pre-check. The unique constraint below is the real safeguard.
        List<BookedSeat> alreadyTaken =
                bookedSeatRepository.findByMovieShowIdAndSeatNumberIn(show.getId(), seats);
        if (!alreadyTaken.isEmpty()) {
            String taken = String.join(", ",
                    alreadyTaken.stream().map(BookedSeat::getSeatNumber).toList());
            throw new ConflictException("Seats already booked: " + taken);
        }

        BigDecimal total = show.getPricePerSeat().multiply(BigDecimal.valueOf(seats.size()));
        Booking booking = new Booking(generateCode(), show,
                request.customerName().trim(), request.customerEmail().trim(), total);
        for (String seat : seats) {
            booking.addSeat(new BookedSeat(show, seat));
        }

        try {
            bookingRepository.saveAndFlush(booking);
        } catch (DataIntegrityViolationException e) {
            // Two people tried to book the same seat at the same moment.
            throw new ConflictException("Some seats were just booked by someone else. Please pick different seats.");
        }

        return toResponse(booking, List.copyOf(seats));
    }

    @Transactional(readOnly = true)
    public BookingResponse get(String code) {
        Booking booking = find(code);
        List<String> seats = booking.getSeats().stream().map(BookedSeat::getSeatNumber).sorted().toList();
        return toResponse(booking, seats);
    }

    @Transactional
    public BookingResponse cancel(String code) {
        Booking booking = find(code);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }
        if (booking.getMovieShow().getShowTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot cancel a booking for a show that has already started");
        }
        List<String> seats = booking.getSeats().stream().map(BookedSeat::getSeatNumber).sorted().toList();
        booking.cancel();
        bookingRepository.saveAndFlush(booking);
        return toResponse(booking, seats);
    }

    private Booking find(String code) {
        return bookingRepository.findByBookingCode(code.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    /** Seat format: row letter + number, e.g. A1, B10. Must fit inside the screen's grid. */
    private Set<String> normalizeAndValidate(MovieShow show, List<String> requested) {
        Set<String> seats = new LinkedHashSet<>();
        for (String raw : requested) {
            if (raw == null) {
                throw new BadRequestException("Seat number cannot be empty");
            }
            String seat = raw.trim().toUpperCase(Locale.ROOT);
            if (!seat.matches("^[A-Z][0-9]{1,2}$")) {
                throw new BadRequestException("Invalid seat '" + raw + "'. Use a format like A1");
            }
            int rowIndex = seat.charAt(0) - 'A';
            int number = Integer.parseInt(seat.substring(1));
            if (rowIndex >= show.getTotalRows() || number < 1 || number > show.getSeatsPerRow()) {
                throw new BadRequestException("Seat " + seat + " does not exist in this screen");
            }
            seats.add(seat);
        }
        return seats;
    }

    private String generateCode() {
        while (true) {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
            }
            String code = sb.toString();
            if (!bookingRepository.existsByBookingCode(code)) {
                return code;
            }
        }
    }

    private BookingResponse toResponse(Booking b, List<String> seats) {
        MovieShow show = b.getMovieShow();
        return new BookingResponse(b.getBookingCode(), b.getStatus().name(),
                show.getMovie().getTitle(), show.getScreenName(), show.getShowTime(),
                seats, b.getCustomerName(), b.getCustomerEmail(),
                b.getTotalAmount(), b.getCreatedAt());
    }
}
