package com.donald.demo.temporaldemoserver.transfermoney.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyTransferState {

    private int approvalTime = 0;
    private int progressPercentage = 0;
    private String transferState = "NEW";
    private String workflowStatus = "NEW";
    private MoneyTransferResponse moneyTransferResponse;
}
