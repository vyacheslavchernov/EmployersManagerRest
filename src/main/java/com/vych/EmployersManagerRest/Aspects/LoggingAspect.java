package com.vych.EmployersManagerRest.Aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vych.EmployersManagerRest.ApiCore.ApiResponse;
import com.vych.EmployersManagerRest.Domain.Aspects.LogEntity;
import com.vych.EmployersManagerRest.Repo.Aspects.LogsRepo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Aspect
@Component
public class LoggingAspect {

    private final LogsRepo LOGS_REPO;

    @Autowired LoggingAspect(LogsRepo logsRepo) {
        this.LOGS_REPO = logsRepo;
    }

    @AfterReturning(pointcut="@annotation(com.vych.EmployersManagerRest.Aspects.Annotations.NeedLogs)", returning="response")
    public void callAtNeedLogs(JoinPoint joinPoint, ApiResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();

        LogEntity logEntity = new LogEntity()
                .setUsername(auth.getName())
                .setIp(details.getRemoteAddress())
                .setSession(details.getSessionId())
                .setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder("Request to ").append(joinPoint.getSignature()).append("\n");

        for (Object arg : joinPoint.getArgs()) {
            try {
                sb.append(objectMapper.writeValueAsString(arg)).append("\n");
            } catch (JsonProcessingException e) {
                sb.append("Не удалось преобразовать переданные в запросе данные в формат JSON").append("\n");
            }
        }
        logEntity.setRequestBody(sb.toString());
        sb = new StringBuilder("Response:\n");

        try {
            sb.append(objectMapper.writeValueAsString(response)).append("\n");
        } catch (JsonProcessingException e) {
            sb.append("Не удалось преобразовать ответ на запрос в формат JSON").append("\n");
        }
        logEntity.setResponse(sb.toString());

        LOGS_REPO.save(logEntity);
    }
}
