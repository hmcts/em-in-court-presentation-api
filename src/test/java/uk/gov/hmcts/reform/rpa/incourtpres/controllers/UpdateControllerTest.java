package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import uk.gov.hmcts.reform.rpa.incourtpres.IncourtPresApplication;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.ScreenChange;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = IncourtPresApplication.class)
public class UpdateControllerTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String SCREEN_CHANGE_PUBLISH = "/icp/screen-change/123";
    private static final String SUBSCRIBE_SCREEN_CHANGE = "/topic/screen-change/123";

    private CompletableFuture<ScreenChange> completableFuture;

    @Before
    public void setup() {
        completableFuture = new CompletableFuture<ScreenChange>();
        URL = "ws://localhost:" + port + "/icp/ws";
    }

    @Test
    public void should_share_page_changes() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_SCREEN_CHANGE, new CreateScreenChangeStompFrameHandler());
        stompSession.send(SCREEN_CHANGE_PUBLISH, new ScreenChange(2, "http://doc.com/documents/123"));

        ScreenChange screenChange = completableFuture.get(10, SECONDS);

        assertThat(screenChange.getPage(), equalTo(2));
        assertThat(screenChange.getDocument(), equalTo("http://doc.com/documents/123"));
    }



    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateScreenChangeStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ScreenChange.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((ScreenChange) o);
        }
    }
}