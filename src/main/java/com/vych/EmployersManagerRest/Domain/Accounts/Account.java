package com.vych.EmployersManagerRest.Domain.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;

import javax.persistence.*;

@Entity(name = "accounts")
@Table(name = "accounts")
public class Account implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("amount")
    private Double amount;

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Account setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
