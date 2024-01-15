
package com.accenture.pip.ordermanagementservice.controller;

import com.accenture.pip.ordermanagementservice.OrderManagementServiceApplication;
import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.config.OIDCTokenConfig;
import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationRequest;
import com.accenture.pip.ordermanagementservice.exception.AuthenticationException;
import com.accenture.pip.ordermanagementservice.service.KeycloakAuthenticationManager;
import com.accenture.pip.ordermanagementservice.service.OrderService;
import com.accenture.pip.ordermanagementservice.util.OrderValidator;
import com.accenture.pip.ordermanagementservice.util.TokenTestUtil;
import com.accenture.pip.ordermanagementservice.util.TokenUtil;
import com.google.common.io.Resources;
//import mockwebserver3.MockResponse;
//import mockwebserver3.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {OrderManagementServiceApplication.class})
@TestPropertySource("classpath:application-test.properties")
public class AuthControllerIT {

    @Autowired
    KeycloakAuthenticationManager authenticationManager;

    @Mock
    KeycloakAuthenticationManager manager;

    @Autowired
    MockMvc mockMvc;


    @MockBean
    OIDCTokenConfig oidcTokenConfig;

    @MockBean
    @Qualifier("keycloakRestTemplate")
    RestTemplate restTemplate;


    @Mock
    @Qualifier("keycloakWebClient")
    WebClient webClient;

    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.RequestBodySpec requestBodySpec;

    @Mock
    WebClient.ResponseSpec responseSpec;
    @MockBean
    CustomerConfig customerConfig;


    public static MockWebServer mockWebServer;
//
//    @BeforeAll
//    static void setUp() throws IOException {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//    }

//    @BeforeEach
//    void initialize() {
//        String baseUrl = String.format("http://localhost:%s",
//                mockWebServer.getPort());
//        when(oidcTokenConfig.getTokenUri()).thenReturn(baseUrl);
//        //ReflectionTestUtils.setField(oidcTokenConfig,"oidcTokenConfig.getTokenUri()",baseUrl);
//    }

    @Test
    public void testAuthenticateFailure() throws Exception {

        OIDCToken token = TokenTestUtil.createToken();
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(any(),any())).thenReturn(requestBodySpec);

        when(requestHeadersSpec.header(any(),any())).thenReturn(requestHeadersSpec);

        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ArgumentMatchers.<Class<OIDCToken>>notNull()))
                .thenReturn(Mono.just(token));

        //when(oidcTokenConfig.getTokenUri()).thenReturn(baseUrl);
        when(manager.fetchOIDCAccessTokenWithReactive(any(), any(), any()))
                .thenReturn(Optional.of(token));

        String requestBody = Resources.toString(Resources.getResource("payloads/login.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/auth/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void testAuthenticateSuccess() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
               String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        String body = Resources.toString(Resources.getResource("payloads/authenticationResponse.json"), StandardCharsets.UTF_8);
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .setBody(body);
        mockWebServer.enqueue(mockResponse);

        OIDCToken token = TokenTestUtil.createToken();

        //when(oidcTokenConfig.getTokenUri()).thenReturn(baseUrl);
        when(manager.fetchOIDCAccessTokenWithReactive(any(), any(), any()))
                .thenReturn(Optional.of(token));

        String requestBody = Resources.toString(Resources.getResource("payloads/login.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/auth/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());

        mockWebServer.shutdown();
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        String body = Resources.toString(Resources.getResource("payloads/authenticationResponse.json"), StandardCharsets.UTF_8);
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .setBody("logout successful");
        mockWebServer.enqueue(mockResponse);

        OIDCToken token = TokenTestUtil.createToken();

        //when(oidcTokenConfig.getTokenUri()).thenReturn(baseUrl);
        when(manager.fetchOIDCAccessTokenWithReactive(any(), any(), any()))
                .thenReturn(Optional.of(token));

        String requestBody = Resources.toString(Resources.getResource("payloads/logout.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/auth/logout")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        mockWebServer.shutdown();
    }
    @Test
    public void testLogoutFailure() throws Exception {

        OIDCToken token = TokenTestUtil.createToken();
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(any(),any())).thenReturn(requestBodySpec);

        when(requestHeadersSpec.header(any(),any())).thenReturn(requestHeadersSpec);

        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ArgumentMatchers.<Class<String>>notNull()))
                .thenThrow(new AuthenticationException());

        //when(oidcTokenConfig.getTokenUri()).thenReturn(baseUrl);
//        when(manager.fetchOIDCAccessTokenWithReactive(any(), any(), any()))
//                .thenReturn(Optional.of("logout successful"));

        String requestBody = Resources.toString(Resources.getResource("payloads/logout.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/auth/logout")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @AfterAll
//    static void tearDown() throws IOException {
//        mockWebServer.shutdown();
//    }
}

