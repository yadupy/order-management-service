package com.accenture.pip.ordermanagementservice.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Valid()
public class AddressDTO {

    @JsonProperty(required = false)
    private String addressId;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String pinCode;


}
