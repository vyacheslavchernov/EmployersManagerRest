package com.vych.EmployersManagerRest.Domain.Rights;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vych.EmployersManagerRest.ApiCore.Payloads.ResponsePayload;
import com.vych.EmployersManagerRest.Domain.Users.User;
import com.vych.EmployersManagerRest.Domain.Views;

import javax.persistence.*;

@Entity(name = "rights")
@Table(name = "rights")
public class Right implements ResponsePayload {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @JsonView(Views.NonSensitiveData.class)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty("rightsBits")
    private String rightsBits;

    @JsonProperty("useRightsScheme")
    private boolean useRightsScheme;

    @ManyToOne
    @JoinColumn(name = "right_schemes_name")
    private RightScheme rightScheme;

    public Long getId() {
        return id;
    }

    public Right setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Right setUser(User user) {
        this.user = user;
        return this;
    }

    public String getRightsBits() {
        return rightsBits;
    }

    public Right setRightsBits(String rightsBits) {
        this.rightsBits = rightsBits;
        return this;
    }

    public boolean isUseRightsScheme() {
        return useRightsScheme;
    }

    public Right setUseRightsScheme(boolean useRightsScheme) {
        this.useRightsScheme = useRightsScheme;
        return this;
    }

    public RightScheme getRightScheme() {
        return rightScheme;
    }

    public Right setRightScheme(RightScheme rightScheme) {
        this.rightScheme = rightScheme;
        return this;
    }
}
