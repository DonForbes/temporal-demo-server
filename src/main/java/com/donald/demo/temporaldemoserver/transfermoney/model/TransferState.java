package com.donald.demo.temporaldemoserver.transfermoney.model;

public enum TransferState {
    NEW,
    APPROVAL_TIMED_OUT,
    APPROVED,
    VALIDATION_FAILED,
    VALIDATED,
    FUNDS_WITHDRAWN,
    WITHDRAW_FAILED,
    FUNDS_DEPOSITED,
    DEPSIT_FAILED,
    COMPLETED
}
