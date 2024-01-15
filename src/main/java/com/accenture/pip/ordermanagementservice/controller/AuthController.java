
package com.accenture.pip.ordermanagementservice.controller;

import com.accenture.pip.ordermanagementservice.dto.Health;
import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
import com.accenture.pip.ordermanagementservice.dto.OrderHealth;
import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationRequest;
import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationResponse;
import com.accenture.pip.ordermanagementservice.dto.security.LogOutRequest;
import com.accenture.pip.ordermanagementservice.exception.AuthenticationException;
import com.accenture.pip.ordermanagementservice.service.KeycloakAuthenticationManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
@Validated
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    KeycloakAuthenticationManager authenticationManager;


    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(
            summary = "Authenticates a user with username/password",
            description = "Authenticates a user with username/password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    public Mono<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody @NotNull @Valid AuthenticationRequest authenticationRequest) {
        try {
            log.info("Authentication request received for user: {}", authenticationRequest.getUserName());
            Mono<AuthenticationResponse> response = authenticationManager.authenticate(authenticationRequest);
            log.info("authentication successful for user: {}", authenticationRequest.getUserName());
            return response.map(ResponseEntity::ok);
        } catch (BadCredentialsException e) {
            log.error("authentication failed for user");
            throw new AuthenticationException();
        }
    }

    @PostMapping(value = "/logout",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(
            summary = "ends current user session",
            description = "ends current user session with keycloak")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    public Mono<ResponseEntity<String>> logout(@RequestBody @NotNull @Valid LogOutRequest request) {
        try {
            log.info("Logout request received ");
            Mono<String> response = authenticationManager.logout(request.getRefreshToken());
            log.info("logout successful for ");
            return response.map(ResponseEntity::ok);
        } catch (BadCredentialsException e) {
            log.error("logout request failed for user");
            throw new AuthenticationException();
        }
    }

}


