package com.vych.EmployersManagerRest.Controllers;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.Exceptions.IncorrectData;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ListPayload;
import com.vych.EmployersManagerRest.ApiCore.ResponseUtil;
import com.vych.EmployersManagerRest.ApiCore.Status;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.Aspects.Annotations.NeedLogs;
import com.vych.EmployersManagerRest.Domain.Shifts.Fine;
import com.vych.EmployersManagerRest.Domain.Shifts.Shift;
import com.vych.EmployersManagerRest.Domain.Shifts.ShiftPlan;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Repo.Shifts.FineRepo;
import com.vych.EmployersManagerRest.Repo.Shifts.ShiftPlanRepo;
import com.vych.EmployersManagerRest.Repo.Shifts.ShiftRepo;
import com.vych.EmployersManagerRest.Repo.Users.UserRepo;
import com.vych.EmployersManagerRest.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ShiftsController {

    private final String CONTROLLER_ENDPOINT = "api/shifts/";

    private final UserRepo USER_REPO;
    private final ShiftRepo SHIFT_REPO;
    private final FineRepo FINE_REPO;
    private final ShiftPlanRepo SHIFT_PLAN_REPO;

    @Autowired
    public ShiftsController(UserRepo userRepo, ShiftRepo shiftRepo, FineRepo fineRepo, ShiftPlanRepo shiftPlanRepo) {
        this.USER_REPO = userRepo;
        this.SHIFT_REPO = shiftRepo;
        this.FINE_REPO = fineRepo;
        this.SHIFT_PLAN_REPO = shiftPlanRepo;
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "plan/add")
    public ApiResponse addShiftToPlan(@RequestBody ShiftPlan shiftPlan) {
        if (
                !SHIFT_PLAN_REPO.findAllByUserIdAndFromDateAndToDate(
                        shiftPlan.getUser().getId(),
                        Utils.toZeroHour(shiftPlan.getShiftDate()),
                        Utils.toZeroHour(shiftPlan.getShiftDate())
                ).isEmpty()
        ) {
            return ResponseUtil.buildError(
                    new IncorrectData("У пользователя " + shiftPlan.getUser().getUsername() + " уже запланирована смена"),
                    "Ошибка при попытке добавить смену в план"
            );
        }

        try {
            shiftPlan.setShiftDate(Utils.toZeroHour(shiftPlan.getShiftDate()));
            SHIFT_PLAN_REPO.save(shiftPlan);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке добавить смену в план");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "plan/delete")
    public ApiResponse deleteShiftFromPlanById(@RequestParam Long id) {
        Optional<ShiftPlan> shiftPlan = SHIFT_PLAN_REPO.findById(id);

        if (shiftPlan.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Смены с id " + id + " не существует"),
                    "Ошибка при попытке удалить смену из плана"
            );
        }

        try {
            SHIFT_PLAN_REPO.delete(shiftPlan.get());
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить смену из плана");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "plan/getForUser")
    public ApiResponse getUserShiftFromPlan(@RequestParam String username, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        from = from != null ? Utils.toZeroHour(from) : null;
        to = to != null ? Utils.toZeroHour(to) : null;

        Optional<User> userOp = USER_REPO.findByUsername(username);
        if (userOp.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Пользователя с username " + username + " не существует"),
                    "Ошибка при попытке получить план смен пользователя"
            );
        }

        User user = userOp.get();
        List<ShiftPlan> shifts;

        try {
            if (from == null && to == null) {
                shifts = SHIFT_PLAN_REPO.findAllByUser(user);
            } else {
                if (from == null) {
                    return ResponseUtil.buildError(
                            new IncorrectData("Не задано условие выборки from при заданном to"),
                            "Ошибка при попытке получить план смен пользователя"
                    );
                }
                shifts = to == null
                        ? SHIFT_PLAN_REPO.findAllByUserIdAndFromDate(user.getId(), from)
                        : SHIFT_PLAN_REPO.findAllByUserIdAndFromDateAndToDate(user.getId(), from, to);
            }
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить список смен пользователя");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(shifts)));
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "plan/getForAll")
    public ApiResponse getAllShiftFromPlan(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        from = from != null ? Utils.toZeroHour(from) : null;
        to = to != null ? Utils.toZeroHour(to) : null;
        List<ShiftPlan> shifts;

        try {
            if (from == null && to == null) {
                shifts = SHIFT_PLAN_REPO.findAll();
            } else {
                if (from == null) {
                    return ResponseUtil.buildError(
                            new IncorrectData("Не задано условие выборки from при заданном to"),
                            "Ошибка при попытке получить план смен"
                    );
                }
                shifts = to == null
                        ? SHIFT_PLAN_REPO.findAllByFromDate(from)
                        : SHIFT_PLAN_REPO.findAllByFromDateAndToDate(from, to);
            }
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить список смен");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(shifts)));
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fact/add")
    public ApiResponse addShiftToFact(@RequestBody Shift shift) {
        if (SHIFT_PLAN_REPO.findByUser(shift.getUser()).isPresent()) {
            return ResponseUtil.buildError(
                    new IncorrectData("У пользователя " + shift.getUser().getUsername() + " уже есть смена"),
                    "Ошибка при попытке добавить смену в факт"
            );
        }

        try {
            SHIFT_REPO.save(shift);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке добавить смену в факт");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fact/delete")
    public ApiResponse deleteShiftFromFactById(@RequestParam Long id) {
        Optional<Shift> shift = SHIFT_REPO.findById(id);

        if (shift.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Смены с id " + id + " не существует"),
                    "Ошибка при попытке удалить смену из факта"
            );
        }

        try {
            SHIFT_REPO.delete(shift.get());
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить смену из факта");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "fact/getForUser")
    public ApiResponse getUserShiftFromFact(@RequestParam String username, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        from = from != null ? Utils.toZeroHour(from) : null;
        to = to != null ? Utils.toZeroHour(to) : null;

        Optional<User> userOp = USER_REPO.findByUsername(username);
        if (userOp.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Пользователя с username " + username + " не существует"),
                    "Ошибка при попытке получить план смен пользователя"
            );
        }

        User user = userOp.get();
        List<Shift> shifts;

        try {
            if (from == null && to == null) {
                shifts = SHIFT_REPO.findAllByUser(user);
            } else {
                if (from == null) {
                    return ResponseUtil.buildError(
                            new IncorrectData("Не задано условие выборки from при заданном to"),
                            "Ошибка при попытке получить план смен пользователя"
                    );
                }
                shifts = to == null
                        ? SHIFT_REPO.findAllByUserIdAndFromDate(user.getId(), from)
                        : SHIFT_REPO.findAllByUserIdAndFromDateAndToDate(user.getId(), from, to);
            }
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить список смен пользователя");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(shifts)));
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "fact/getForAll")
    public ApiResponse getUserShiftFromFact(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        from = from != null ? Utils.toZeroHour(from) : null;
        to = to != null ? Utils.toZeroHour(to) : null;

        List<Shift> shifts;

        try {
            if (from == null && to == null) {
                shifts = SHIFT_REPO.findAll();
            } else {
                if (from == null) {
                    return ResponseUtil.buildError(
                            new IncorrectData("Не задано условие выборки from при заданном to"),
                            "Ошибка при попытке получить план смен"
                    );
                }
                shifts = to == null
                        ? SHIFT_REPO.findAllByFromDate(from)
                        : SHIFT_REPO.findAllByFromDateAndToDate(from, to);
            }
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить список смен");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(shifts)));
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fact/update")
    public ApiResponse updateShift(@RequestBody Shift shiftData) {
        Shift shift = SHIFT_REPO.findById(shiftData.getId()).orElse(null);
        if (shift == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Смены с id " + shiftData.getId() + " не существует"),
                    "Не удалось обновить смену"
            );
        }

        try {
            SHIFT_REPO.save(shiftData);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке обновить смену");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fines/add")
    public ApiResponse addFine(@RequestBody Fine fine) {
        if (fine.getShift() == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Не указана смена для добавления штрафа"),
                    "Не удалось добавить штраф"
            );
        }

        try {
            FINE_REPO.save(fine);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке добавить штраф");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @GetMapping(CONTROLLER_ENDPOINT + "fines/get")
    public ApiResponse getFine(@RequestParam Long shiftId) {
        Optional<Shift> shift = SHIFT_REPO.findById(shiftId);

        if (shift.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Смены с id " + shiftId + " не существует"),
                    "Ошибка при попытке получить штрафы за смену"
            );
        }

        List<Fine> fines;

        try {
            fines = FINE_REPO.findAllByShift(shiftId);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке получить штрафы за смену");
        }

        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new ListPayload().setListOfPayload(Collections.singletonList(fines)));
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fines/delete")
    public ApiResponse deleteFine(@RequestParam Long id) {
        Optional<Fine> fine = FINE_REPO.findById(id);

        if (fine.isEmpty()) {
            return ResponseUtil.buildError(
                    new IncorrectData("Штрафа с id " + id + " не существует"),
                    "Ошибка при попытке удалить штраф"
            );
        }

        try {
            FINE_REPO.delete(fine.get());
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке удалить штраф");
        }

        return ResponseUtil.buildSuccess();
    }

    @NeedLogs
    @PostMapping(CONTROLLER_ENDPOINT + "fines/update")
    public ApiResponse updateFine(@RequestBody Fine fineData) {
        Fine fine = FINE_REPO.findById(fineData.getId()).orElse(null);
        if (fine == null) {
            return ResponseUtil.buildError(
                    new IncorrectData("Штрафа с id " + fineData.getId() + " не существует"),
                    "Не удалось обновить штраф"
            );
        }

        try {
            FINE_REPO.save(fineData);
        } catch (Exception e) {
            return ResponseUtil.buildError(e, "Ошибка при попытке обновить штраф");
        }

        return ResponseUtil.buildSuccess();
    }
}
