package com.vych.EmployersManagerRest.Repo.Shifts;

import com.vych.EmployersManagerRest.Domain.Shifts.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepo extends JpaRepository <Fine, Long> {
}
