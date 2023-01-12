package com.vych.EmployersManagerRest;

import com.vych.EmployersManagerRest.Domain.Users.Role;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Repo.Users.RoleRepo;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class EmployersManagerRestApplicationTests {

	private final UserRepo USER_REPO;
	//private final RoleRepo ROLE_REPO;

	@Autowired
	public EmployersManagerRestApplicationTests(UserRepo userRepo, RoleRepo roleRepo) {
		this.USER_REPO = userRepo;
		//this.ROLE_REPO = roleRepo;
	}

	@Test
	void createNewUser() {
		USER_REPO.save(
				new User()
						.setEnabled(true)
						.setFirstName("user")
						.setLastName("user")
						.setPassword("0000")
						.setUsername("vy")
						.setRole(new Role().setAuthority("ROLE_ADMIN").setName("vy"))
		);
	}

}
