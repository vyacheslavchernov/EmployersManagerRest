package com.vych.EmployersManagerRest.Repo.Accounts;

import com.vych.EmployersManagerRest.Domain.Accounts.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepo extends JpaRepository<Operation, Long> {
}
