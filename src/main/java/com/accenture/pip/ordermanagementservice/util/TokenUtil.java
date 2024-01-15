package com.accenture.pip.ordermanagementservice.util;


import com.accenture.pip.ordermanagementservice.exception.JwtTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.aspectj.weaver.patterns.Declare;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Slf4j
public final class TokenUtil {

    public final static int TOKEN_REFRESH_OFFSET_SECONDS = 100;

    private TokenUtil() {
    }

    public static boolean isTokenValid(String accessToken, Instant refrenceTime) {
        Objects.requireNonNull(accessToken, "Token is null");
        Objects.requireNonNull(refrenceTime, "refrenceTime is null");
        boolean returnValue;
        try {
            DecodedJWT jwt = JWT.decode(accessToken);
            Date expiresDate =  jwt.getExpiresAt();
            final Instant expiresAt =expiresDate.toInstant();
            final long diff = expiresAt.getEpochSecond() - refrenceTime.getEpochSecond();
            returnValue = diff > TOKEN_REFRESH_OFFSET_SECONDS;
            if (returnValue) {
                log.info("token is valid for : {}", DurationFormatUtils.formatDurationHMS(100 * diff));
            } else {
                log.info("Token has expired");
            }
        } catch (Exception e) {
            log.error("failed to extract access token");
            throw new JwtTokenException();
        }
        return returnValue;
    }
}
