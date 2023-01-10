package com.vych.EmployersManagerRest.Repo.Shifts;

import com.vych.EmployersManagerRest.Domain.Shifts.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepo extends JpaRepository<Shift, Long> {
}
