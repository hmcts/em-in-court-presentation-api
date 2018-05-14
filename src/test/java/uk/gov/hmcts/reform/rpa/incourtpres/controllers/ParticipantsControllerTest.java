package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import uk.gov.hmcts.reform.rpa.incourtpres.IncourtPresApplication;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.ParticipantStatus;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.ScreenChange;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.SubscriptionStatus;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static uk.gov.hmcts.reform.rpa.incourtpres.domain.SubscriptionStatus.CONNECTED;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = IncourtPresApplication.class)
@AutoConfigureMockMvc
public class ParticipantsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String PARTICIPANTS_PUBLISH = "/icp/participants/123";
    private static final String SUBSCRIBE_PARTICIPANTS = "/topic/participants/123";

    private CompletableFuture<Collection<Map<String, Object>>> completableFuture;

    @Before
    public void setup() {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/icp/ws";
    }

    @Test
    public void should_share_status_changes() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_PARTICIPANTS, new CreateScreenChangeStompFrameHandler());
        stompSession.send(PARTICIPANTS_PUBLISH, new ParticipantStatus("Louis", "123", CONNECTED));

        Collection<Map<String, Object>> participantStatuses = completableFuture.get(10, SECONDS);

        assertThat(participantStatuses.size(), is(1));
        Map<String, Object> participantStatus = participantStatuses.stream().collect(Collectors.toList()).get(0);
        assertThat(participantStatus.get("status"), CoreMatchers.equalTo(CONNECTED.toString()));
    }

    @Test
    public void should_get_statuses() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_PARTICIPANTS, new CreateScreenChangeStompFrameHandler());
        String sessionId = UUID.randomUUID().toString();
        stompSession.send("/icp/participants/" + sessionId,
                new ParticipantStatus("Louis", sessionId, CONNECTED));

        mvc.perform(get("/icp/sessions/"+ sessionId + "/participants"))
                .andExpect(jsonPath("$[0].status", equalTo(CONNECTED.toString())));
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateScreenChangeStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Collection.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Collection<Map<String, Object>>) o);
        }
    }
}