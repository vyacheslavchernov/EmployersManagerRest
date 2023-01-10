package com.vych.EmployersManagerRest;

import com.vych.EmployersManagerRest.Controllers.RightsController;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Repo.Rights.RightRepo;
import com.vych.EmployersManagerRest.Repo.Rights.RightSchemeRepo;
import com.vych.EmployersManagerRest.Repo.Users.RoleRepo;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class RightsTests {

    private final RoleRepo ROLE_REPO;
    private final UserRepo USER_REPO;
    private final RightRepo RIGHT_REPO;
    private final RightSchemeRepo RIGHT_SCHEME_REPO;
    private final RightsController rightsController;

    @Autowired
    public RightsTests(RoleRepo roleRepo, UserRepo userRepo, RightRepo rightRepo, RightSchemeRepo rightSchemeRepo) {
        this.ROLE_REPO = roleRepo;
        this.USER_REPO = userRepo;
        this.RIGHT_REPO = rightRepo;
        this.RIGHT_SCHEME_REPO = rightSchemeRepo;

        this.rightsController = new RightsController(this.USER_REPO, this.RIGHT_REPO, this.RIGHT_SCHEME_REPO);
    }

    @AfterEach
    public void cleanupTestData() {
    }

    @Test
    public void getRightsTest() {

    }
}
