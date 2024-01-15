package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TokenTestUtil {

    @Mock
    DecodedJWT jwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String BAD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";

    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWlNRHFtQzJCY3h6MUEtU2hCbzhmZW1BY3Q0In0.eyJleHAiOjE3MDM2ODAzNTgsImlhdCI6MTcwMzY4MDA1OCwianRpIjoiZDA5YTdhZjQtZDI1MS00OWY0LWFkNDQtZDM3ZWM5YTBjZjQ5IiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MC9yZWFsbXMvcGlwLWlhcCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3MzA2NTA3ZC04YWI3LTQ5ZTYtODNjMS0yYmUwMzQ1ZDZkODQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJwaXAtY2h1Z2d5Iiwic2Vzc2lvbl9zdGF0ZSI6ImEyY2YxYzU1LWExNDYtNDE3YS05YzdjLTQ4OTM1ODIxYTQwNyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1waXAtaWFwIiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiYTJjZjFjNTUtYTE0Ni00MTdhLTljN2MtNDg5MzU4MjFhNDA3IiwiY2xpZW50SG9zdCI6IjEyNy4wLjAuMSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1waXAtaWFwIiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl0sInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1waXAtY2h1Z2d5IiwiY2xpZW50QWRkcmVzcyI6IjEyNy4wLjAuMSIsImNsaWVudF9pZCI6InBpcC1jaHVnZ3kifQ.d1RQcmFt1dq7O0m78H-dvi9qEOzzxeuS8Cw7WdLfvdsGfdexHsfnmOuB3m_AgMFz0ez9vAOzKGJLduLdRMDBHRd17_IarZlJpt6iZP4hZ0nosH1flSDYYwotY7Ww6udIqRgIHPjtHkM7X8-7dOiJLivbhXkwXlN0Zjt_F53nA9SpN2Qd5f4vwVHYZIswTJv1KwVYHpQMd7XQ1YeFUzaa6zYrBwQpdfm6HohyeWsxsOPQsrhQkNx88I1chgrl0-1jaSNASI6lIEpWnlRM7f6DKuIKm-QWMDbziXoRCBzPkLhnHZD9bfY-3pODI2uJuCADJTboaAC1ITNNHDOhpj0S_g";

    public static OIDCToken createToken() {
        return OIDCToken.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(UUID.randomUUID().toString())
                .tokenType("Bearer")
                .expiresIn("300")
                .refreshExpiresIn("1000")
                .notBeforePolicy("0")
                .build();
    }

    @Test
    public void testIsTokenValid_Success(){
        Date expiresDate =  new Date();
        expiresDate.setDate(expiresDate.getDate() + 100);
        Instant expiresAt = Instant.now().plusSeconds(600);
        when(jwt.getExpiresAt()).thenReturn(expiresDate);

        boolean isValidToken = TokenUtil.isTokenValid(ACCESS_TOKEN, Instant.now().minus(1000, ChronoUnit.DAYS));
        Assertions.assertTrue(isValidToken);
    }

    @Test
    public void testIsTokenValid_Failure(){
        Date expiresDate =  new Date();
        expiresDate.setDate(expiresDate.getDate() + 1);
        Instant expiresAt = Instant.now().plusSeconds(600);
        when(jwt.getExpiresAt()).thenReturn(expiresDate);

        boolean isValidToken = TokenUtil.isTokenValid(ACCESS_TOKEN, Instant.now());
        Assertions.assertFalse(isValidToken);
    }
}
