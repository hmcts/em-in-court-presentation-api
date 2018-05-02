package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScreenChange {
    private final int page;

    @JsonCreator
    public ScreenChange(@JsonProperty("page") int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
