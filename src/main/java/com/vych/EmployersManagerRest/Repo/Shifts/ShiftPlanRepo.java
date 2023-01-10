package com.vych.EmployersManagerRest.Repo.Shifts;

import com.vych.EmployersManagerRest.Domain.Shifts.ShiftPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftPlanRepo extends JpaRepository<ShiftPlan, Long> {
}
