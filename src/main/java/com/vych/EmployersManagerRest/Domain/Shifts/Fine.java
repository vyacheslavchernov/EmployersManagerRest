package com.vych.EmployersManagerRest.Domain.Shifts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;

import javax.persistence.*;

@Entity(name = "fines")
@Table(name = "fines")
public class Fine implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("description")
    private Double description;

    public Long getId() {
        return id;
    }

    public Fine setId(Long id) {
        this.id = id;
        return this;
    }

    public Shift getShift() {
        return shift;
    }

    public Fine setShift(Shift shift) {
        this.shift = shift;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Fine setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getDescription() {
        return description;
    }

    public Fine setDescription(Double description) {
        this.description = description;
        return this;
    }
}
