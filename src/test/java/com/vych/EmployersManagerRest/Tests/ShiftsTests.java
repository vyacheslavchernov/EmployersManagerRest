package com.vych.EmployersManagerRest.Tests;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Controllers.ShiftsController;
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
import com.vych.EmployersManagerRest.Steps.ShiftsSteps;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static io.qameta.allure.Allure.step;

@DisplayName("Тесты контроллера смен")
public class ShiftsTests extends BaseTest {

    private final ShiftsSteps SHIFT_STEPS;
    private final ShiftsController SHIFTS_CONTROLLER;

    @Autowired
    public ShiftsTests(
            UserRepo userRepo, RoleRepo roleRepo, ShiftRepo shiftRepo,
            ShiftPlanRepo shiftPlanRepo, FineRepo fineRepo, RightSchemeRepo rightSchemeRepo,
            RightRepo rightRepo, LogsRepo logsRepo, OperationRepo operationRepo, AccountRepo accountRepo
    ) {
        super(
                userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo
        );

        SHIFT_STEPS = new ShiftsSteps(
                userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo);

        SHIFTS_CONTROLLER = SHIFT_STEPS.getShiftsController();
    }

    /**
     * Добавить второго тестового пользователя.
     * Добавить в план по N смен для разных пользователей на дни идущие друг за другом.
     * Получить смену на определённый день для пользователя.
     * Попытка получения смены для пользователя на день в который нет смены.
     * Получить смены для пользователя с определённого числа.
     * Получить смены для пользователя с и по определённые числа.
     * Провести аналогичные шаги, но для всех пользователей сразу.
     * Удалить добавленные смены.
     */
    @Test
    @DisplayName("Проверка планирования смен")
    @Description(useJavaDoc = true)
    public void shiftsPlanningTest() {
        var ref = new Object() {
            final User secondTestUser = STEPS.createUserWithCommit("ROLE_ADMIN");
            final int n = 5;
            final LocalDateTime dt = LocalDateTime.now();
        };

        step("Добавление смен в план", () -> {
            for (int i = 0; i < ref.n; i++) {
                LocalDateTime date = ref.dt.plusDays(i);

                ApiResponse response = SHIFT_STEPS.createAndCommitShiftPlan(testUser, date);
                STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);

                response = SHIFT_STEPS.createAndCommitShiftPlan(ref.secondTestUser, date);
                STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
            }
        });

        step("Получение смены на определённый день для пользователя", () -> {
            ApiResponse response = SHIFTS_CONTROLLER.getUserShiftFromPlan(
                    testUser.getUsername(),
                    ref.dt,
                    ref.dt
            );

            STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        STEPS.deleteUserFromBase(ref.secondTestUser);
    }

    /**
     * Добавить второго тестового пользователя.
     * Добавить в факт по несколько смен для разных пользователей на дни идущие друг за другом.
     * Получить смену на определённый день для пользователя.
     * Попытка получения смены для пользователя на день в который нет смены.
     * Получить смены для пользователя с определённого числа.
     * Получить смены для пользователя с и по определённые числа.
     * Провести аналогичные шаги, но для всех пользователей сразу.
     * Изменить одну из смен. Сохранить. Получить и проверить, что изменения были внесены в БД.
     * Удалить добавленные смены.
     */
    @Test
    @DisplayName("Проверка работы с фактически состоявшимися сменами")
    @Description(useJavaDoc = true)
    public void shiftsFactTest() {
        var ref = new Object() {
            final User secondTestUser = STEPS.createUserWithCommit("ROLE_ADMIN");
            final int n = 5;
            final LocalDateTime dt = LocalDateTime.now();
        };

        step("Добавление смен в факт", () -> {
            for (int i = 0; i < ref.n; i++) {
                LocalDateTime date = ref.dt.plusDays(i);

                ApiResponse response = SHIFT_STEPS.createAndCommitShiftFact(testUser, date);
                STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);

                response = SHIFT_STEPS.createAndCommitShiftFact(ref.secondTestUser, date);
                STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
            }
        });

        step("Получение смены на определённый день для пользователя", () -> {
            ApiResponse response = SHIFTS_CONTROLLER.getUserShiftFromFact(
                    testUser.getUsername(),
                    ref.dt,
                    ref.dt
            );

            STEPS.checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        STEPS.deleteUserFromBase(ref.secondTestUser);
    }

    /**
     * Добавить состоявшующя смену для пользователя.
     * Добавить к ней несколько штрафов.
     * Получить штрафы за смену. Проверить корректность данных.
     * Изменить один из штрафов. Проверить, что изменения были внесены.
     * Удалить штрафы и смену пользователя.
     */
    @Test
    @DisplayName("Проверка работы с штрафами")
    @Description(useJavaDoc = true)
    public void finesTest() {
    }
}
