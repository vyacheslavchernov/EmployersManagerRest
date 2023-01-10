package com.vych.EmployersManagerRest.Domain.Shifts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;
import com.vych.EmployersManagerRest.Domain.Users.User;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "shifts_planer")
@Table(name = "shifts_planer")
public class ShiftPlan implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonProperty("employee")
    private User user;

    @JsonProperty("shiftDate")
    private Date shiftDate;

    public Long getId() {
        return id;
    }

    public ShiftPlan setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ShiftPlan setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public ShiftPlan setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
        return this;
    }
}
