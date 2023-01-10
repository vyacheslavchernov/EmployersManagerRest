package com.vych.EmployersManagerRest.Repo.Accounts;

import com.vych.EmployersManagerRest.Domain.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
