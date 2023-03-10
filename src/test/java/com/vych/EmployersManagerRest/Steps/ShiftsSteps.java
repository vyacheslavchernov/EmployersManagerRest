package com.vych.EmployersManagerRest.Steps;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.Controllers.ShiftsController;
import com.vych.EmployersManagerRest.Domain.Shifts.Shift;
import com.vych.EmployersManagerRest.Domain.Shifts.ShiftPlan;
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
import io.qameta.allure.Step;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class ShiftsSteps extends BaseSteps{

    private final ShiftsController SHIFTS_CONTROLLER;

    public ShiftsSteps(UserRepo userRepo, RoleRepo roleRepo, ShiftRepo shiftRepo, ShiftPlanRepo shiftPlanRepo, FineRepo fineRepo, RightSchemeRepo rightSchemeRepo, RightRepo rightRepo, LogsRepo logsRepo, OperationRepo operationRepo, AccountRepo accountRepo) {
        super(userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo, rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo);
        SHIFTS_CONTROLLER = new ShiftsController(userRepo, shiftRepo, fineRepo, shiftPlanRepo);
    }

    public ShiftsController getShiftsController() {
        return SHIFTS_CONTROLLER;
    }

    @Step("Создание нового плана смены")
    public ShiftPlan createShiftPlan(User user, LocalDateTime date) {
        return new ShiftPlan().setUser(user).setShiftDate(date);
    }

    @Step("Создание нового плана смены")
    public ApiResponse createAndCommitShiftPlan(User user, LocalDateTime date) {
        return SHIFTS_CONTROLLER
                .addShiftToPlan(
                        createShiftPlan(user, date)
                );
    }

    @Step("Создание нового плана смены")
    public Shift createShiftFact(User user, LocalDateTime date) {
        return new Shift()
                .setShiftStart(date)
                .setShiftEnd(date)
                .setUser(user)
                .setSalary(1000d)
                .setSalaryType("shift");
    }

    @Step("Создание нового плана смены")
    public ApiResponse createAndCommitShiftFact(User user, LocalDateTime date) {
        return SHIFTS_CONTROLLER
                .addShiftToFact(
                        createShiftFact(user, date)
                );
    }

}
