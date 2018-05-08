package uk.gov.hmcts.reform.rpa.incourtpres.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import uk.gov.hmcts.reform.rpa.incourtpres.IncourtPresApplication;
import uk.gov.hmcts.reform.rpa.incourtpres.domain.HearingSession;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = IncourtPresApplication.class)
@AutoConfigureMockMvc
public class HearingSessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_persist_a_session() throws Exception {

        HearingSession session =
                new HearingSession("My hearing", LocalDateTime.now(), Arrays.asList("lh@test.com"));
        String locationHeader = mvc.perform(post("/sessions")
                .content(objectMapper.writeValueAsString(session))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/sessions/")))
                .andReturn()
                .getResponse()
                .getHeader("location");

        mvc.perform(get(locationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo(session.getDescription())));
    }
}