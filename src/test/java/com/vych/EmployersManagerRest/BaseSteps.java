package com.vych.EmployersManagerRest;

import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Domain.Rights.Right;
import com.vych.EmployersManagerRest.Domain.Rights.RightScheme;
import com.vych.EmployersManagerRest.Domain.Users.Role;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseSteps {
    protected final UserRepo USER_REPO;
    protected final RoleRepo ROLE_REPO;
    protected final ShiftRepo SHIFT_REPO;
    protected final ShiftPlanRepo SHIFT_PLAN_REPO;
    protected final FineRepo FINE_REPO;
    protected final RightSchemeRepo RIGHT_SCHEME_REPO;
    protected final RightRepo RIGHT_REPO;
    protected final LogsRepo LOGS_REPO;
    protected final OperationRepo OPERATION_REPO;
    protected final AccountRepo ACCOUNT_REPO;

    protected final Random RANDOM = new Random();

    @Autowired
    public BaseSteps(
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

        RANDOM.setSeed(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    @Step("Создание нового пользователя без записи в БД")
    protected User createUserWithoutCommit() {
        String username = Utils.getRandomLettersStringWithLength(10);
        return new User()
                .setUsername(username)
                .setPassword(Utils.getRandomLettersStringWithLength(5) + Utils.getRandomNumbersStringWithLength(5))
                .setEnabled(true)
                .setRole(new Role().setName(username).setAuthority("ROLE_ADMIN"));
    }

    @Step("Создание нового пользователя с записью в БД")
    protected User createUserWithCommit() {
        User user = createUserWithoutCommit();
        USER_REPO.save(user);
        return user;
    }

    @Step("Удаление пользователя из БД")
    protected void deleteUserFromBase(User user) {
        Optional<Right> rightOp = RIGHT_REPO.findByUser(user);
        rightOp.ifPresent(right -> {
            if (right.isUseRightsScheme()) {
                Optional<RightScheme> scheme = RIGHT_SCHEME_REPO.findById(right.getRightScheme().getName());
                scheme.ifPresent(RIGHT_SCHEME_REPO::delete);
            }
            RIGHT_REPO.delete(right);
        });
        USER_REPO.delete(user);
    }

    @Step("Проверка статуса ответа от API")
    protected void checkApiResponseStatus(StatusCode response, StatusCode expected) {
        assertEquals(
                expected,
                response,
                "Статус ответа отличается от ожидаемого"
        );
    }

    @Step("Проверка на not null объекта {object}")
    protected void checkNotNull(Object object) {
        assertNotNull(object, "Объект равен null");
    }

    @Step("Проверка размера коллекции {collection} на соответствие размеру в {expectedSize} элементов")
    protected <T> void checkCollectionSize(Collection<T> collection, int expectedSize) {
        assertEquals(
                expectedSize,
                collection.size(),
                "Размер коллекции не совпадает с ожидаемым"
        );
    }
}
