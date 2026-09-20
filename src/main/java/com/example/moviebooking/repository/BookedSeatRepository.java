package com.example.moviebooking.repository;

import com.example.moviebooking.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    @Query("select b.seatNumber from BookedSeat b where b.movieShow.id = :showId")
    List<String> findSeatNumbersByShowId(@Param("showId") Long showId);

    List<BookedSeat> findByMovieShowIdAndSeatNumberIn(Long showId, Collection<String> seatNumbers);
}