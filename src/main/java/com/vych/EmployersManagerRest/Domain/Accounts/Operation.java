package com.vych.EmployersManagerRest.Domain.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;
import com.vych.EmployersManagerRest.Domain.Users.User;

import javax.persistence.*;

@Entity(name = "operations")
@Table(name = "operations")
public class Operation implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    private User user;

    public Long getId() {
        return id;
    }

    public Operation setId(Long id) {
        this.id = id;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public Operation setAccount(Account account) {
        this.account = account;
        return this;
    }

    public String getType() {
        return type;
    }

    public Operation setType(String type) {
        this.type = type;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Operation setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Operation setDescription(String description) {
        this.description = description;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Operation setUser(User user) {
        this.user = user;
        return this;
    }
}
