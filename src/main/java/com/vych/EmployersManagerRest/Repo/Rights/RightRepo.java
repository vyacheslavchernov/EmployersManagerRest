package com.vych.EmployersManagerRest.Repo.Rights;

import com.vych.EmployersManagerRest.Domain.Rights.Right;
import com.vych.EmployersManagerRest.Domain.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RightRepo extends JpaRepository<Right, Long> {
    Optional<Right> findByUser(User user);
}
