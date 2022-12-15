package com.vych.EmployersManagerRest.Repo.Aspects;

import com.vych.EmployersManagerRest.Domain.Aspects.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepo extends JpaRepository<LogEntity, Long> {
}
