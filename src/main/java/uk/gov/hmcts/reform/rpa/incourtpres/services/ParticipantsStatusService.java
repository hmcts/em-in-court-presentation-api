package uk.gov.hmcts.reform.rpa.incourtpres.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.ParticipantStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParticipantsStatusService {

    private final Map<String, Map<String, ParticipantStatus>> sessions = new HashMap<>();

    public Collection<ParticipantStatus> updateStatus(ParticipantStatus status) {
        sessions.putIfAbsent(status.getSessionId(), new HashMap<>());
        sessions.get(status.getSessionId()).put(status.getName(), status);
        return sessions.get(status.getSessionId()).values();
    }

    public Collection<ParticipantStatus> getStatus(String sessionId) {
        sessions.putIfAbsent(sessionId, new HashMap<>());
        return sessions.get(sessionId).values();
    }
}
