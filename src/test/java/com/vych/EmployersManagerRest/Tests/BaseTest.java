package com.vych.EmployersManagerRest.Tests;

import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Repo.Accounts.AccountRepo;
import com.vych.EmployersManagerRest.Repo.Accounts.OperationRepo;
import com.vych.EmployersManagerRest.Repo.Aspects.LogsRepo;
import com.vych.EmployersManagerRest.Repo.Rights.RightRepo;
import com.vych.EmployersManagerRest.Repo.Rights.RightSchemeRepo;
import com.vych.EmployersManagerRest.Repo.Shifts.FineRepo;
import com.vych.EmployersManagerRest.Repo.Shifts.ShiftPlanRepo;
import com.vych.EmployersManagerRest.Repo.Shifts.ShiftRepo;
import com.vych.EmployersManagerRest.Repo.Users.RoleRepo;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import com.vych.EmployersManagerRest.Steps.BaseSteps;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    final UserRepo USER_REPO;
    final RoleRepo ROLE_REPO;
    final ShiftRepo SHIFT_REPO;
    final ShiftPlanRepo SHIFT_PLAN_REPO;
    final FineRepo FINE_REPO;
    final RightSchemeRepo RIGHT_SCHEME_REPO;
    final RightRepo RIGHT_REPO;
    final LogsRepo LOGS_REPO;
    final OperationRepo OPERATION_REPO;
    final AccountRepo ACCOUNT_REPO;

    User testUser;

    final BaseSteps steps;

    @Autowired
    public BaseTest(
            UserRepo userRepo,
            RoleRepo roleRepo,
            ShiftRepo shiftRepo,
            ShiftPlanRepo shiftPlanRepo,
            FineRepo fineRepo,
            RightSchemeRepo rightSchemeRepo,
            RightRepo rightRepo,
            LogsRepo logsRepo,
            OperationRepo operationRepo,
            AccountRepo accountRepo
    ) {
        this.USER_REPO = userRepo;
        this.ROLE_REPO = roleRepo;
        this.SHIFT_REPO = shiftRepo;
        this.SHIFT_PLAN_REPO = shiftPlanRepo;
        this.FINE_REPO = fineRepo;
        this.RIGHT_SCHEME_REPO = rightSchemeRepo;
        this.RIGHT_REPO = rightRepo;
        this.LOGS_REPO = logsRepo;
        this.OPERATION_REPO = operationRepo;
        this.ACCOUNT_REPO = accountRepo;

        steps = new BaseSteps(userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo);
    }

    @Step("Подготовка тестовых данных")
    @BeforeEach
    public void init() {
        testUser = steps.createUserWithCommit();
    }

    @Step("Очистка после завершения теста")
    @AfterEach
    public void destruct() {
        if (testUser != null) {
            steps.deleteUserFromBase(testUser);
        }

    }
}
