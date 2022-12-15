package com.vych.EmployersManagerRest.Repo.Users;

import com.vych.EmployersManagerRest.Domain.Users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
