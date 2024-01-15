package com.accenture.pip.ordermanagementservice.constants;

public final class RestaurantManagementConstant {
    public static final String EMAIL_ALREADY_IN_USE_EXCEPTION = "This email address is already registered with another user, please use different email address";
    public static final String CONTACT_NUMBER_ALREADY_IN_USE_EXCEPTION = "This contact number is already registered with another user, please use different contact number";

    public static final String INVALID_JWT_TOKEN = "Invalid JWT token- authentication failed";

    public static final String AUTHENTICATION_FAILED = "login failed for the user - Invalid username/password";

    public static final String RESTAURANT_NOT_FOUND = "unable to find the restaurant";

    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
}
