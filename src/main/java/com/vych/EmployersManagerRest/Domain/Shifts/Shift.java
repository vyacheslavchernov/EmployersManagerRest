package com.vych.EmployersManagerRest.Domain.Shifts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;
import com.vych.EmployersManagerRest.Domain.Users.User;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "shifts")
@Table(name = "shifts")
public class Shift implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonProperty("employee")
    private User user;

    @JsonProperty("shiftStart")
    private Date shiftStart;

    @JsonProperty("shiftEnd")
    private Date shiftEnd;

    @JsonProperty("salaryType")
    private String salaryType;

    @JsonProperty("salary")
    private Double salary;

    public Long getId() {
        return id;
    }

    public Shift setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Shift setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getShiftStart() {
        return shiftStart;
    }

    public Shift setShiftStart(Date shiftStart) {
        this.shiftStart = shiftStart;
        return this;
    }

    public Date getShiftEnd() {
        return shiftEnd;
    }

    public Shift setShiftEnd(Date shiftEnd) {
        this.shiftEnd = shiftEnd;
        return this;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public Shift setSalaryType(String salaryType) {
        this.salaryType = salaryType;
        return this;
    }

    public Double getSalary() {
        return salary;
    }

    public Shift setSalary(Double salary) {
        this.salary = salary;
        return this;
    }
}
