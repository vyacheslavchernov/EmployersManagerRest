package com.vych.EmployersManagerRest.Tests;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ListPayload;
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
import com.vych.EmployersManagerRest.Steps.RightsSteps;
import com.vych.EmployersManagerRest.TestUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static io.qameta.allure.Allure.step;

@DisplayName("Тесты контроллера прав пользователей")
public class RightsTests extends BaseTest {

    private final RightsSteps RIGHT_STEPS;
    private final RightsController RIGHTS_CONTROLLER;

    @Autowired
    public RightsTests(
            UserRepo userRepo, RoleRepo roleRepo, ShiftRepo shiftRepo,
            ShiftPlanRepo shiftPlanRepo, FineRepo fineRepo, RightSchemeRepo rightSchemeRepo,
            RightRepo rightRepo, LogsRepo logsRepo, OperationRepo operationRepo, AccountRepo accountRepo
    ) {
        super(
                userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo
        );

        RIGHT_STEPS = new RightsSteps(userRepo, roleRepo, shiftRepo, shiftPlanRepo, fineRepo,
                rightSchemeRepo, rightRepo, logsRepo, operationRepo, accountRepo);

        RIGHTS_CONTROLLER = RIGHT_STEPS.getRightsController();
    }

    /**
     * Добавляем права для пользователя.
     * Проверяем успешность добавления прав.
     * Получаем только добавленные права и проверяем, что данные были добавленны корректно.
     * Вносим изменения в права, сохраняем изменения.
     * Получаем права, проверяем, что изменения внесены.
     * Удаляем права пользователя.
     * Проверяем, что права успешно удалены.
     */
    @Test
    @DisplayName("Добавление прав для пользователя (набор битов), их изменение и удаление")
    @Description(useJavaDoc = true)
    public void rightsTest() {
        var ref = new Object() {
            ApiResponse response;
            String rightBits = TestUtils.getRandomBitsWithLength(10);
            Right right;
        };

        step("Установка прав для пользователя", () -> {
            ref.response = RIGHTS_CONTROLLER.setUserRights(
                    new Right()
                            .setRightsBits(ref.rightBits)
                            .setUseRightsScheme(false)
                            .setUser(testUser)
            );
            STEPS.checkApiResponseStatus(ref.response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        step("Получение прав пользователя и их проверка", () -> {
            ref.right = RIGHT_STEPS.getUserRight(testUser.getUsername());
            RIGHT_STEPS.checkEqualsForRightBits(ref.rightBits, ref.right.getRightsBits());
        });

        step("Изменение прав пользователя", () -> {
            ref.rightBits = TestUtils.getRandomBitsWithLength(5);
            ref.right.setRightsBits(ref.rightBits);
            ref.response = RIGHTS_CONTROLLER.setUserRights(ref.right);
            STEPS.checkApiResponseStatus(ref.response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        step("Проверка успешности изменения прав пользователя", () -> {
            ref.right = RIGHT_STEPS.getUserRight(testUser.getUsername());
            RIGHT_STEPS.checkEqualsForRightBits(ref.rightBits, ref.right.getRightsBits());
        });

        RIGHT_STEPS.deleteUserRights(ref.right);

        step("Проверка успешности удаления прав пользователя", () -> {
            RIGHT_STEPS.getUserRight(testUser.getUsername(), StatusCode.ERROR);
        });
    }

    /**
     * Добавляем схему прав.
     * Проверяем успешность добавления схемы прав.
     * Получаем только добавленую схему прав и проверяем, что данные были добавленны корректно.
     * Вносим изменения в схему прав, сохраняем изменения.
     * Получаем схему прав, проверяем, что изменения внесены.
     * Удаляем схему прав.
     * Проверяем, что схема прав успешно удалена.
     */
    @Test
    @DisplayName("Добавление, изменение, удаление схемы прав")
    @Description(useJavaDoc = true)
    public void rightsSchemeTest() {
        var ref = new Object() {
            final String schemeName = TestUtils.getRandomLettersStringWithLength(10);
            String rightBits = TestUtils.getRandomBitsWithLength(10);
            ApiResponse response;
            RightScheme scheme;
        };

        RIGHT_STEPS.addNewRightScheme(ref.schemeName, ref.rightBits);

        step("Проверка успешности добавления схемы прав", () -> {
            ref.scheme = RIGHT_STEPS.getRightScheme(ref.schemeName);
            RIGHT_STEPS.checkEqualsForRightBits(ref.rightBits, ref.scheme.getRightsBits());
        });

        step("Изменение схемы прав", () -> {
            ref.rightBits = TestUtils.getRandomBitsWithLength(5);
            ref.scheme.setRightsBits(ref.rightBits);
            ref.response = RIGHTS_CONTROLLER.updateRightScheme(ref.scheme);
            STEPS.checkApiResponseStatus(ref.response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        step("Проверка успешности изменения схемы прав", () -> {
            ref.scheme = RIGHT_STEPS.getRightScheme(ref.schemeName);
            RIGHT_STEPS.checkEqualsForRightBits(ref.rightBits, ref.scheme.getRightsBits());
        });

        step("Удаление схемы прав", () -> RIGHT_STEPS.deleteRightsScheme(ref.schemeName));

        step("Проверка успешности удаления схемы прав", () -> {
            RIGHT_STEPS.getRightScheme(ref.schemeName, StatusCode.ERROR);
        });
    }

    /**
     * Создаём схему прав. Проверяем успешность её создания.
     * Создаём права для пользователя с использованием схемы прав.
     * Проверяем успешность создания.
     * Пытаемся получить схему прав применённую для пользователя.
     * Удаляем права для пользователя.
     * Пытаемся получить схему прав применённую для пользователя.
     * Пытаемся отдельно получить только схему по имени. Проверяем её наличие.
     * Удаляем схему.
     */
    @Test
    @DisplayName("Добавление прав для пользователя по схеме прав")
    @Description(useJavaDoc = true)
    public void addRightSchemeToUser() {
        var ref = new Object() {
            final String schemeName = TestUtils.getRandomLettersStringWithLength(10);
            final String rightBits = TestUtils.getRandomBitsWithLength(10);
            ApiResponse response;
            RightScheme scheme;
            Right right;
        };

        RIGHT_STEPS.addNewRightScheme(ref.schemeName, ref.rightBits);
        ref.scheme = RIGHT_STEPS.getRightScheme(ref.schemeName);

        step("Установка прав для пользователя", () -> {
            ref.response = RIGHTS_CONTROLLER.setUserRights(
                    new Right()
                            .setUseRightsScheme(true)
                            .setRightScheme(ref.scheme)
                            .setUser(testUser)
            );
            STEPS.checkApiResponseStatus(ref.response.getStatus().getCode(), StatusCode.SUCCESS);
        });

        step("Получение схемы прав установленной для пользователя", () -> {
            ref.right = RIGHT_STEPS.getUserRight(testUser.getUsername());
            STEPS.checkNotNull(ref.right.getRightScheme());
            RIGHT_STEPS.checkEqualsForRightBits(ref.rightBits, ref.right.getRightScheme().getRightsBits());
        });

        RIGHT_STEPS.deleteUserRights(ref.right);
        RIGHT_STEPS.getUserRight(testUser.getUsername(), StatusCode.ERROR);

        RIGHT_STEPS.getRightScheme(ref.schemeName);
        RIGHT_STEPS.deleteRightsScheme(ref.schemeName);
    }

    /**
     * Создаём n количество схем прав.
     * Пытаемся получить все эти схемы за раз. Сверяем количество полученых схем с n.
     * Удаляем созданные схемы.
     */
    @Test
    @DisplayName("Получение всех схем прав списком")
    @Description(useJavaDoc = true)
    public void getAllRightsScheme() {
        int n = 5 + STEPS.getRandom().nextInt(6);
        var ref = new Object() {
            ApiResponse response;
            ListPayload payload;
        };

        step("Создание n схем правил в БД", () -> {
            for (int i = 0; i < n; i++) {
                RIGHT_STEPS.addNewRightScheme(
                        TestUtils.getRandomLettersStringWithLength(10),
                        TestUtils.getRandomBitsWithLength(10)
                );
            }
        });

        step("Получение всех схем прав", () -> {
            ref.response = RIGHTS_CONTROLLER.getAllRightSchemes();
            STEPS.checkApiResponseStatus(ref.response.getStatus().getCode(), StatusCode.SUCCESS);
            ref.payload = (ListPayload) ref.response.getPayload();
            STEPS.checkCollectionSize((Collection<?>) ref.payload.getListOfPayload().get(0), n);
        });

        step("Удаление созданных схем прав", () -> {
            for (Object scheme : (Collection<?>) ref.payload.getListOfPayload().get(0)) {
                String schemeName = ((RightScheme) scheme).getName();
                RIGHT_STEPS.deleteRightsScheme(schemeName);
                RIGHT_STEPS.getRightScheme(schemeName, StatusCode.ERROR);
            }
        });
    }
}
