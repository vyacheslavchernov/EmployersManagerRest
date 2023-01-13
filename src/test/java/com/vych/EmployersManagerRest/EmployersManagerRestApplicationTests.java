package com.vych.EmployersManagerRest;

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
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class EmployersManagerRestApplicationTests extends BaseTest {

    @Autowired
    public EmployersManagerRestApplicationTests(
            UserRepo userRepo, RoleRepo roleRepo, ShiftRepo shiftRepo,
            ShiftPlanRepo shiftPlanRepo, FineRepo fineRepo, RightSchemeRepo rightSchemeRepo,
            RightRepo rightRepo, LogsRepo logsRepo, OperationRepo operationRepo, AccountRepo accountRepo
    ) {
        super(
                userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo
        );
    }
}
