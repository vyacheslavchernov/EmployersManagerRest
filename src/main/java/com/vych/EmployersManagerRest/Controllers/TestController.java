package com.vych.EmployersManagerRest.Controllers;

import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.ApiCore.Status;
import com.vych.EmployersManagerRest.ApiCore.StatusCode;
import com.vych.EmployersManagerRest.ApiCore.Payloads.StringPayload;
import com.vych.EmployersManagerRest.Aspects.Annotations.NeedLogs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Тестовый контроллер для проверки успешности развёртки приложения
 */
@RestController
public class TestController {

    @NeedLogs
    @GetMapping("api/echo")
    public ApiResponse testMethod(@RequestParam String msg) {
        return new ApiResponse()
                .setStatus(new Status().setCode(StatusCode.SUCCESS))
                .setPayload(new StringPayload().setMessage(msg));
    }
}
