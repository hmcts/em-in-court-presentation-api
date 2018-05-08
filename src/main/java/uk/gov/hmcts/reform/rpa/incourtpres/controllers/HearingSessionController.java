package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.HearingSession;
import uk.gov.hmcts.reform.rpa.incourtpres.services.HearingSessionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController()
@RequestMapping("sessions")
public class HearingSessionController {

    private HearingSessionService hearingSessionService;

    @Autowired
    public HearingSessionController(HearingSessionService hearingSessionService) {
        this.hearingSessionService = hearingSessionService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HearingSession> newSession(
            @RequestBody final HearingSession hearingSession) throws URISyntaxException {
        String location = "/sessions/" + this.hearingSessionService.newSession(hearingSession).getId();
        return ResponseEntity.created(new URI(location)).build();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HearingSession> getSession(@PathVariable UUID id) {
        return ResponseEntity.ok(this.hearingSessionService.getSession(id));
    }
}
