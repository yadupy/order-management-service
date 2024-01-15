package com.accenture.pip.ordermanagementservice.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantManagementErrorResponse {

    private ErrorAdvice errorAdvice;
}
