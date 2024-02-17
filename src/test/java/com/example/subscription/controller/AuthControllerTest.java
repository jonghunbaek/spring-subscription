package com.example.subscription.controller;

import com.example.subscription.controller.dto.AuthInfo;
import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.PassType;
import com.example.subscription.filter.PassFilter;
import com.example.subscription.service.PassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    PassService passService;
    @MockBean
    PassFilter passFilter;

    @DisplayName("로그인 시 이용권 정보를 찾아 응답 헤더에 담아 반환한다.")
    @Test
    void findPassAndPutHeader() throws Exception {
        AuthInfo authInfo = new AuthInfo(1L, "zxcv");

        mockMvc.perform(
            post("/sign-in")
                .content(objectMapper.writeValueAsString(authInfo))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(PassType.CHAT_CONSUMABLE.toString(), "10:0"));
    }

    @DisplayName("로그인 시 이용권 정보를 찾아 응답 헤더에 담아 반환한다.")
    @Test
    void findPassAndPutHeade2r() throws Exception {
        AuthInfo authInfo = new AuthInfo(1L, "zxcv");
        PassInfo passInfo = PassInfo.builder()
                .remainingChatTimes(10)
                .remainingUnitTimes(0)
                .passType(PassType.CHAT_CONSUMABLE)
                .build();
        Mockito.when(passService.findPass(authInfo.getMemberId())).thenReturn(passInfo);
        mockMvc.perform(
                post("/sign-in")
                    .content(objectMapper.writeValueAsString(authInfo))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(PassType.CHAT_CONSUMABLE.toString(), "10:0"));
    }
}