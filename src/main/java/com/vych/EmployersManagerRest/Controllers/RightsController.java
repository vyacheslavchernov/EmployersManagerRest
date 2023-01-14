package com.vych.EmployersManagerRest.Controllers;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.Exceptions.IncorrectData;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ListPayload;
import com.vych.EmployersManagerRest.ApiCore.ResponseUtil;
import com.vych.EmployersManagerRest.ApiCore.Status;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Aspects.Annotations.NeedLogs;
import com.vych.EmployersManagerRest.Domain.Rights.Right;
import com.vych.EmployersManagerRest.Domain.Rights.RightScheme;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Repo.Rights.RightRepo;
import com.vych.EmployersManagerRest.Repo.Rights.RightSchemeRepo;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class RightsController {

    private final String CONTROLLER_ENDPOINT = "api/rights/";

    private final UserRepo USER_REPO;
    private final RightRepo RIGHT_REPO;
    private final RightSchemeRepo RIGHT_SCHEME_REPO;

    @Autowired
    public RightsController(UserRepo userRepo, RightRepo rightRepo, RightSchemeRepo rightSchemeRepo) {
        this.USER_REPO = userRepo;
        this.RIGHT_REPO = rightRepo;
        this.RIGHT_SCHEME_REPO = rightSchemeRepo;
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "setRights")
    public ApiResponse setUserRights(@RequestBody Right rightsToSet) {
        RIGHT_REPO.findByUser(rightsToSet.getUser()).ifPresent(
                right -> rightsToSet.setId(right.getId())
        );

        try {
            RIGHT_REPO.save(rightsToSet);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке обновить/добавить права пользователя");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "getRights")
    public ApiResponse getUserRights(@RequestParam String username) {
        User user = USER_REPO.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Не найден пользователь по username " + username),
                    "Ошибка при получении прав пользователя"
            );
        }

        Right rights = RIGHT_REPO.findByUser(user).orElse(null);

        if (rights == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Не найдено прав для пользователя " + username),
                    "Ошибка при получении прав пользователя"
            );
        }

        return new ApiResponse().setStatus(new Status().setCode(StatusCode.SUCCESS)).setPayload(rights);
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "deleteRights")
    public ApiResponse deleteUserRights(@RequestParam Long id) {
        try {
            RIGHT_REPO.deleteById(id);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке обновить/добавить права пользователя");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "addRightsScheme")
    public ApiResponse addRightScheme(@RequestBody RightScheme rightScheme) {
        if (RIGHT_SCHEME_REPO.findById(rightScheme.getName()).isPresent()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Схема прав уже существует"),
                    "Ошибка при добавлении схемы прав"
            );
        }

        try {
            RIGHT_SCHEME_REPO.save(rightScheme);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке добавить схему прав");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "deleteRightsScheme")
    public ApiResponse deleteRightScheme(@RequestParam String name) {
        Optional<RightScheme> rightScheme = RIGHT_SCHEME_REPO.findById(name);

        if (rightScheme.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Схема прав " + name + " не существует"),
                    "Ошибка при удалении схемы прав"
            );
        }

        try {
            RIGHT_SCHEME_REPO.delete(rightScheme.get());
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить схему прав");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "updateRightScheme")
    public ApiResponse updateRightScheme(@RequestBody RightScheme rightScheme) {
        Optional<RightScheme> schemeOp = RIGHT_SCHEME_REPO.findById(rightScheme.getName());

        if (schemeOp.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Схема прав " + rightScheme.getName() + " не существует"),
                    "Ошибка при обновлении схемы прав"
            );
        }

        RightScheme scheme = schemeOp.get();
        scheme.setRightsBits(rightScheme.getRightsBits());

        try {
            RIGHT_SCHEME_REPO.save(scheme);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке обновить схему прав");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "getRightScheme")
    public ApiResponse getRightScheme(@RequestParam String name) {
        Optional<RightScheme> rightScheme = RIGHT_SCHEME_REPO.findById(name);

        if (rightScheme.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Схема прав " + name + " не существует"),
                    "Ошибка при получении схемы прав"
            );
        }

        return new ApiResponse().setStatus(new Status().setCode(StatusCode.SUCCESS)).setPayload(rightScheme.get());
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "getAllRightSchemes")
    public ApiResponse getAllRightSchemes() {
        List<RightScheme> schemes;

        try {
            schemes = RIGHT_SCHEME_REPO.findAll();
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить все схемы прав");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(schemes)));
    }
}
