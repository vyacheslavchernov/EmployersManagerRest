package com.vych.EmployersManagerRest.Domain.Rights;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "rights_schemes")
@Table(name = "rights_schemes")
public class RightScheme implements ResponsePayload {
    @Id
    @JsonProperty("name")
    private String name;

    @JsonProperty("rightsBits")
    private String rightsBits;

    @OneToMany(mappedBy = "rightScheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Right> rights = new java.util.LinkedHashSet<>();

    public Set<Right> getRights() {
        return rights;
    }

    public void setRights(Set<Right> rights) {
        this.rights = rights;
    }

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
