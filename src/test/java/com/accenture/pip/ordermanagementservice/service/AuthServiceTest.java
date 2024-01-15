//package com.accenture.pip.ordermanagementservice.service;
//
//import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
//import com.accenture.pip.ordermanagementservice.config.OIDCTokenConfig;
//import com.accenture.pip.ordermanagementservice.dto.Health;
//import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
//import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
//import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
//import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationRequest;
//import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationResponse;
//import com.accenture.pip.ordermanagementservice.util.CustomerUtilTest;
//import com.accenture.pip.ordermanagementservice.util.TokenTestUtil;
//import com.google.common.io.Resources;
//import jakarta.inject.Inject;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class AuthServiceTest {
//
//    @InjectMocks
//    @Autowired
//    KeycloakAuthenticationManager authenticationManager;
//
//    @Mock
//    KeycloakAuthenticationManager manager;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Mock
//    @Qualifier("keycloakRestTemplate")
//    RestTemplate restTemplate;
//
//
//    @Mock
//    OIDCTokenConfig oidcTokenConfig;
//
//    @Mock
//    @Qualifier("keycloakWebClient")
//    WebClient webClient;
//
//    @Mock
//    CustomerConfig customerConfig;
//
//    public static MockWebServer mockWebServer;
//
//
//    private WebClient.RequestBodyUriSpec requestBodyUriMock;
//    private WebClient.RequestHeadersSpec requestHeadersMock;
//    private WebClient.RequestBodySpec requestBodyMock;
//    private WebClient.ResponseSpec responseMock;
//    private WebClient webClientMock;
//
////    @BeforeAll
////    static void setUp() throws IOException {
////        mockWebServer = new MockWebServer();
////        mockWebServer.start();
////    }
//
//    @BeforeEach
//    void mockWebClient() {
//        MockitoAnnotations.openMocks(this);
//        requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
//        requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
//        requestBodyMock = mock(WebClient.RequestBodySpec.class);
//        responseMock = mock(WebClient.ResponseSpec.class);
//        webClientMock = mock(WebClient.class);
//    }
//
//
//    @Test
//    public void testAuthenticateSuccess_withValidUser() throws Exception {
//
//        OIDCToken token = TokenTestUtil.createToken();
//
//        when(webClientMock.post()).thenReturn(requestBodyUriMock);
//        when(requestBodyUriMock.uri(anyString())).thenReturn(requestBodyMock);
//        when(requestBodyMock.bodyValue(any())).thenReturn(requestHeadersMock);
//        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
//        when(responseMock.bodyToMono(OIDCToken.class)).thenReturn(Mono.just(token));
//
//       // when(oidcTokenConfig.getTokenUri()).thenReturn("http://0.0.0.0:8080/realms/pip-iap/protocol/openid-connect/");
//        CustomerResponse response = CustomerUtilTest.createCustomer();
//        //String body = Resources.toString(Resources.getResource("payloads/authenticationResponse.json"), StandardCharsets.UTF_8);
////        MockResponse mockResponse = new MockResponse()
////                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
////                .setBody(body);
////        mockWebServer.enqueue(mockResponse);
//
//        when(manager.getCustomerByUserName(any()))
//                .thenReturn(response);
//
//        Mono<AuthenticationResponse> authenticationResponse  = authenticationManager.authenticate(new AuthenticationRequest("naruto","password"));
//
//        Assertions.assertTrue(!authenticationResponse.block().getAccessToken().isEmpty());
//
//    }
//
////        @AfterAll
////    static void tearDown() throws IOException {
////        mockWebServer.shutdown();
////    }
//}
