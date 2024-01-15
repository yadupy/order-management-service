package com.accenture.pip.ordermanagementservice.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Valid()
public class CustomerResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String contactNumber;

    @JsonProperty("address")
    private Set<AddressDTO> addressDTOs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String userName;

}
