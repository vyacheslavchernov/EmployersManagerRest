package com.vych.EmployersManagerRest.Domain.Users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;
import com.vych.EmployersManagerRest.Domain.Shifts.ShiftPlan;
import com.vych.EmployersManagerRest.Domain.Views;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Учётные данные пользователей
 */
@Entity
@Table(name = "users")
public class User implements ResponsePayload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.NonSensitiveData.class)
    private Long id;

    @JsonProperty("firstName")
    @JsonView(Views.NonSensitiveData.class)
    private String firstName;

    @JsonProperty("lastName")
    @JsonView(Views.NonSensitiveData.class)
    private String lastName;

    @JsonProperty("patrname")
    @JsonView(Views.NonSensitiveData.class)
    private String patrname;

    @JsonProperty("pseudonym")
    @JsonView(Views.NonSensitiveData.class)
    private String pseudonym;

    @JsonProperty("position")
    @JsonView(Views.NonSensitiveData.class)
    private String position;

    @JsonProperty("employed_from")
    @JsonView(Views.NonSensitiveData.class)
    private Date employed_from;

    @JsonProperty("fired_from")
    @JsonView(Views.NonSensitiveData.class)
    private Date fired_from;

    @JsonProperty("username")
    @JsonView(Views.NonSensitiveData.class)
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("enabled")
    @JsonView(Views.NonSensitiveData.class)
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorities_username", unique = true, nullable = false)
    @JsonProperty("role")
    @JsonView(Views.NonSensitiveData.class)
    private Role role;

//    @OneToMany(mappedBy = "user")
//    private List<ShiftPlan> shiftsPlan;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPatrname() {
        return patrname;
    }

    public User setPatrname(String patrname) {
        this.patrname = patrname;
        return this;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public User setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public User setPosition(String position) {
        this.position = position;
        return this;
    }

    public Date getEmployed_from() {
        return employed_from;
    }

    public User setEmployed_from(Date employed_from) {
        this.employed_from = employed_from;
        return this;
    }

    public Date getFired_from() {
        return fired_from;
    }

    public User setFired_from(Date fired_from) {
        this.fired_from = fired_from;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

//    public List<ShiftPlan> getShiftsPlan() {
//        return shiftsPlan;
//    }
//
//    public User setShiftsPlan(List<ShiftPlan> shiftsPlan) {
//        this.shiftsPlan = shiftsPlan;
//        return this;
//    }
}
