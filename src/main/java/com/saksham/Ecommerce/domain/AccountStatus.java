package com.saksham.Ecommerce.domain;

public enum AccountStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    SUSPENDED, // temporary suspended
    DEACTIVATED, // user may have chosen to deactivate
    BANNED, // banned permanently due to violation
    CLOSED // bank account at user request
    ;


}
