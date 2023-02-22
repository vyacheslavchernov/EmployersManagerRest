package com.vych.EmployersManagerRest.Repo.Shifts;

import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
import com.vych.EmployersManagerRest.Domain.Shifts.Shift;
import com.vych.EmployersManagerRest.Domain.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ShiftRepo extends JpaRepository<Shift, Long> {
    List<Shift> findAllByUser(User user);

    @Query(value = "SELECT * FROM shifts_planer WHERE user_id = :user and shift_date >= :from", nativeQuery = true)
    List<Shift> findAllByUserIdAndFromDate(@Param("user") Long userId, @Param("from") LocalDateTime from);

    @Query(
            value = "SELECT * FROM shifts_planer WHERE user_id = :user AND shift_date >= :from AND shift_date <= :to",
            nativeQuery = true
    )
    List<Shift> findAllByUserIdAndFromDateAndToDate(
            @Param("user") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query(value = "SELECT * FROM shifts_planer WHERE shift_date >= :from", nativeQuery = true)
    List<Shift> findAllByFromDate(@Param("from") LocalDateTime from);

    @Query(
            value = "SELECT * FROM shifts_planer WHERE shift_date >= :from AND shift_date <= :to",
            nativeQuery = true
    )
    List<Shift> findAllByFromDateAndToDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
