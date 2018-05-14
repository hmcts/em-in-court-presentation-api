package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticipantStatus {
    private final String name;
    private final SubscriptionStatus status;
    private final String sessionId;

    @JsonCreator
    public ParticipantStatus(@JsonProperty("name") String name,
                             @JsonProperty("sessionId") String sessionId,
                             @JsonProperty("status") SubscriptionStatus status) {
        this.name = name;
        this.status = status;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public String getSessionId() {
        return sessionId;
    }
}
