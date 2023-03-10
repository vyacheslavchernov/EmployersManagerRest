package com.vych.EmployersManagerRest.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.Exceptions.IncorrectData;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ListPayload;
import com.vych.EmployersManagerRest.ApiCore.ResponseUtil;
import com.vych.EmployersManagerRest.ApiCore.Status;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Aspects.Annotations.NeedLogs;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Domain.Views;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class UsersController {

    private final String CONTROLLER_ENDPOINT = "api/users/";

    private final UserRepo USER_REPO;

    @Autowired
    public UsersController(UserRepo userRepo) {
        this.USER_REPO = userRepo;
    }

    @NeedLogs
    @JsonView(Views.NonSensitiveData.class)
    @GetMapping(CONTROLLER_ENDPOINT + "get")
    public ApiResponse getUser(@RequestParam String username) {
        if (username == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Не указан username искомого пользователя"),
                    "Не удалось получить данные пользователя"
            );
        }

        User user = USER_REPO.findByUsername(username).orElse(null);
        if (user == null ) {
            return ResponseUtil.buildError(
                    new IncorrectData("Не найдено пользователя с username " + username),
                    "Не удалось получить данные пользователя"
            );
        } else {
            return new ApiResponse().setStatus(new Status().setCode(StatusCode.SUCCESS)).setPayload(user);
        }
    }

    @NeedLogs
    @JsonView(Views.NonSensitiveData.class)
    @GetMapping(CONTROLLER_ENDPOINT + "getall")
    public ApiResponse getAllUsers() {
        ArrayList<User> users;

        try {
            users = (ArrayList<User>) USER_REPO.findAll();
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить всех пользователей");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(users)));
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "add")
    public ApiResponse addUser(@RequestBody User user) {
        if (USER_REPO.findByUsername(user.getUsername()).orElse(null) != null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Пользователь с username " + user.getUsername() + " уже существует"),
                    "Не удалось добавить нового пользователя"
            );
        }

        try {
            USER_REPO.save(user);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке добавить пользователя");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "delete")
    public ApiResponse deleteUser(@RequestParam String username) {
        User user = USER_REPO.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Пользователя с username " + username + " не существует"),
                    "Не удалось удалить пользователя"
            );
        }

        try {
            USER_REPO.delete(user);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить пользователя");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "update")
    public ApiResponse updateUser(@RequestBody User userdata) {
        User user = USER_REPO.findByUsername(userdata.getUsername()).orElse(null);
        if (user == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Пользователя с username " + userdata.getUsername() + " не существует"),
                    "Не удалось удалить пользователя"
            );
        }


        userdata
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .getRole().setName(user.getUsername());

        try {
            USER_REPO.save(userdata);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить пользователя");
        }

        return ResponseUtil.buildSuccess();
    }

    //TODO: Установка прав для пользователя из набора битов
    //TODO: Добавление схемы прав
    //TODO: Удаление схемы прав
    //TODO: Получение схемы прав
    //TODO: Установка прав для пользователя (схема прав)
    //TODO: Получение прав пользователя
}
