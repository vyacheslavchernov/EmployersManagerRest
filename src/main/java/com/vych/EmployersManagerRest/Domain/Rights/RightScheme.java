package com.vych.EmployersManagerRest.Domain.Rights;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;

import javax.persistence.*;

@Entity(name = "rights_schemes")
@Table(name = "rights_schemes")
public class RightScheme implements ResponsePayload {
    @Id
    @JsonProperty("name")
    private String name;

    @JsonProperty("rightsBits")
    private String rightsBits;

    public String getName() {
        return name;
    }

    public RightScheme setName(String name) {
        this.name = name;
        return this;
    }

    public String getRightsBits() {
        return rightsBits;
    }

    public RightScheme setRightsBits(String rightsBits) {
        this.rightsBits = rightsBits;
        return this;
    }
}
