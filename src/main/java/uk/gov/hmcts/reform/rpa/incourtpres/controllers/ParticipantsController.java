package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.hmcts.reform.rpa.incourtpres.services.ParticipantsStatusService;

import java.util.Collection;
import java.util.List;

@Controller
public class ParticipantsController {

    private final ParticipantsStatusService participantsStatusService;

    @Autowired
    public ParticipantsController(ParticipantsStatusService participantsStatusService) {
        this.participantsStatusService = participantsStatusService;
    }

    @MessageMapping("/participants/{sessionId}")
    @SendTo("/topic/participants/{sessionId}")
    public Collection<ParticipantStatus> statusChange(ParticipantStatus status) {
        return participantsStatusService.updateStatus(status);
    }

}
