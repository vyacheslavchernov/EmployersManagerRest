package com.vych.EmployersManagerRest.Repo.Rights;

import com.vych.EmployersManagerRest.Domain.Rights.Right;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RightRepo extends JpaRepository<Right, Long> {
}
