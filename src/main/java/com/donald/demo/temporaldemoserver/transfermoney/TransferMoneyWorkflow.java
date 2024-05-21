package com.donald.demo.temporaldemoserver.transfermoney;

import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransfer;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferState;
import com.fasterxml.jackson.core.JsonProcessingException;


import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TransferMoneyWorkflow {

    @WorkflowMethod
    public MoneyTransferResponse transfer(MoneyTransfer moneyTransfer);

    @QueryMethod(name = "transferStatus")
    MoneyTransferState getStateQuery() throws JsonProcessingException;

    @SignalMethod(name = "approveTransfer")
    void approveTransfer();

    @UpdateMethod
    String approveTransferUpdate();

    @UpdateValidatorMethod(updateName = "approveTransferUpdate")
    void approveTransferUpdateValidator();
}
