package uk.gov.hmcts.reform.rpa.incourtpres.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.ParticipantStatus;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.SubscriptionStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Dummy implementation for demo purposes. Obviously in real life this would be saved in a database!!
 */
@Service
public class ParticipantsStatusService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Map<String, Map<String, ParticipantStatus>> sessions = new HashMap<>();

    @Autowired
    public ParticipantsStatusService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public Collection<ParticipantStatus> updateName(String httpSessionId,
                                                    String hearingSessionId,
                                                    String name) {
        sessions.putIfAbsent(hearingSessionId, new HashMap<>());
        Map<String, ParticipantStatus> participantStatusMap = sessions.get(hearingSessionId);
        participantStatusMap.computeIfPresent(httpSessionId, (k, v) -> v.updateName(name));
        return participantStatusMap.values();
    }

    public Collection<ParticipantStatus> getStatus(String sessionId) {
        sessions.putIfAbsent(sessionId, new HashMap<>());
        return sessions.get(sessionId).values();
    }

    public void updateStatus(@DestinationVariable("sessionId") String hearingSessionId,
                                                      String httpSessionId, SubscriptionStatus status) {
        sessions.putIfAbsent(hearingSessionId, new HashMap<>());
        Map<String, ParticipantStatus> participantStatusMap = sessions.get(hearingSessionId);
        participantStatusMap.computeIfAbsent(httpSessionId, k -> new ParticipantStatus("Anon", hearingSessionId, status));
        participantStatusMap.computeIfPresent(httpSessionId, (k, v) -> new ParticipantStatus(v.getName(), hearingSessionId, status));
        simpMessagingTemplate.convertAndSend("/topic/participants/" + hearingSessionId, participantStatusMap.values());
    }

    public void removeParticipant(String sessionId) {
        sessions.entrySet().parallelStream().forEach((e) -> {
            if (e.getValue().containsKey(sessionId)) {
                e.getValue().remove(sessionId);
                simpMessagingTemplate.convertAndSend("/topic/participants/" + e.getKey(), e.getValue().values());
            }
        });
    }

    public void unsubscribeParticipant(String sessionId) {
        sessions.entrySet().parallelStream().forEach((e) -> {
            if (e.getValue().containsKey(sessionId)) {
                e.getValue().compute(sessionId, (k, v) -> v.updateStatus(SubscriptionStatus.CONNECTED));
                simpMessagingTemplate.convertAndSend("/topic/participants/" + e.getKey(), e.getValue().values());
            }
        });
    }
}
