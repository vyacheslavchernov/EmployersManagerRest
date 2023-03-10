package com.vych.EmployersManagerRest.Repo.Shifts;

import com.vych.EmployersManagerRest.Domain.Shifts.ShiftPlan;
import com.vych.EmployersManagerRest.Domain.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShiftPlanRepo extends JpaRepository<ShiftPlan, Long> {
    Optional<ShiftPlan> findByUser(User user);
    List<ShiftPlan> findAllByUser(User user);

    @Query(value = "SELECT * FROM shifts_planer WHERE user_id = :user", nativeQuery = true)
    List<ShiftPlan> findAllByUserId(@Param("user") Long userId);

    @Query(value = "SELECT * FROM shifts_planer WHERE user_id = :user and shift_date >= :from", nativeQuery = true)
    List<ShiftPlan> findAllByUserIdAndFromDate(@Param("user") Long userId, @Param("from") LocalDateTime from);

    @Query(
            value = "SELECT * FROM shifts_planer WHERE user_id = :user AND shift_date >= :from AND shift_date <= :to",
            nativeQuery = true
    )
    List<ShiftPlan> findAllByUserIdAndFromDateAndToDate(
            @Param("user") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query(value = "SELECT * FROM shifts_planer WHERE shift_date >= :from", nativeQuery = true)
    List<ShiftPlan> findAllByFromDate(@Param("from") LocalDateTime from);

    @Query(
            value = "SELECT * FROM shifts_planer WHERE shift_date >= :from AND shift_date <= :to",
            nativeQuery = true
    )
    List<ShiftPlan> findAllByFromDateAndToDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
