package com.vych.EmployersManagerRest;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Controllers.RightsController;
import com.vych.EmployersManagerRest.Domain.Rights.Right;
import com.vych.EmployersManagerRest.Domain.Rights.RightScheme;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RightsSteps extends BaseSteps {

    private final RightsController RIGHTS_CONTROLLER;

    public RightsSteps(UserRepo userRepo, RoleRepo roleRepo, ShiftRepo shiftRepo, ShiftPlanRepo shiftPlanRepo, FineRepo fineRepo, RightSchemeRepo rightSchemeRepo, RightRepo rightRepo, LogsRepo logsRepo, OperationRepo operationRepo, AccountRepo accountRepo) {
        super(userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo, rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo);
        RIGHTS_CONTROLLER = new RightsController(userRepo, rightRepo, rightSchemeRepo);
    }

    public RightsController getRightsController() {
        return RIGHTS_CONTROLLER;
    }

    @Step("Создаём новой схемы прав")
    void addNewRightScheme(String schemeName, String rightBits) {
        ApiResponse response = RIGHTS_CONTROLLER.addRightScheme(
                new RightScheme()
                        .setName(schemeName)
                        .setRightsBits(rightBits)
        );

        checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
    }

    @Step("Получение схемы прав")
    RightScheme getRightScheme(String schemeName, StatusCode... expectedStatusCode) {
        ApiResponse response = RIGHTS_CONTROLLER.getRightScheme(schemeName);
        checkApiResponseStatus(
                response.getStatus().getCode(),
                expectedStatusCode.length > 0 ? expectedStatusCode[0] : StatusCode.SUCCESS
        );
        return response.getStatus().getCode() == StatusCode.ERROR ? null : (RightScheme) response.getPayload();
    }

    @Step("Удаление схемы прав")
    void deleteRightsScheme(String schemeName) {
        ApiResponse response = RIGHTS_CONTROLLER.deleteRightScheme(schemeName);
        checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
    }

    @Step("Получение прав пользователя")
    Right getUserRight(String username, StatusCode... expectedStatusCode) {
        ApiResponse response = RIGHTS_CONTROLLER.getUserRights(username);
        checkApiResponseStatus(
                response.getStatus().getCode(),
                expectedStatusCode.length > 0 ? expectedStatusCode[0] : StatusCode.SUCCESS
        );
        return response.getStatus().getCode() == StatusCode.ERROR ? null : (Right) response.getPayload();
    }

    @Step("Проверка равенства битов правил")
    void checkEqualsForRightBits(String expected, String actual) {
        assertEquals(
                expected,
                actual,
                "Значение битов различаются"
        );
    }

    @Step("Удаление прав пользователя")
    void deleteUserRights(Right right) {
        ApiResponse response = RIGHTS_CONTROLLER.deleteUserRights(right.getId());
        checkApiResponseStatus(response.getStatus().getCode(), StatusCode.SUCCESS);
    }
}
