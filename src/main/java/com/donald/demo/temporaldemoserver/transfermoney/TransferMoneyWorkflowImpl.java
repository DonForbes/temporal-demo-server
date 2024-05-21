package com.donald.demo.temporaldemoserver.transfermoney;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

import org.apache.catalina.util.ServerInfo;
import org.slf4j.Logger;

import com.donald.demo.temporaldemoserver.hello.HelloActivity;
import com.donald.demo.temporaldemoserver.hello.HelloWorkflowImpl;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransfer;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferState;
import com.donald.demo.temporaldemoserver.transfermoney.util.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;


@WorkflowImpl(taskQueues = "TransferMoneyDemoTaskQueue")
public class TransferMoneyWorkflowImpl implements TransferMoneyWorkflow {
    private MoneyTransferState workflowState = new MoneyTransferState();

    public static final Logger logger = Workflow.getLogger(TransferMoneyWorkflowImpl.class);

    private AccountTransferActivities activity =
    Workflow.newActivityStub(
        AccountTransferActivities.class,
        ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(120)).build());

    @Override
    public MoneyTransferResponse transfer(MoneyTransfer moneyTransfer) {
        logger.debug(("Entered - transfer method started."));
        workflowState.setProgressPercentage(10);
        
        Workflow.sleep(Duration.ofSeconds(30));

        activity.validate(moneyTransfer.getWorkflowOption());

        workflowState.setProgressPercentage(50);

        
        Workflow.sleep(Duration.ofSeconds(3));
        
        MoneyTransferResponse response = new MoneyTransferResponse();
        response.setChargeId(IdGenerator.generateTransferId());
        workflowState.setProgressPercentage(100);
        return response;
    }

    @Override
    public MoneyTransferState getStateQuery() throws JsonProcessingException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStateQuery'");
    }

    @Override
    public void approveTransfer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveTransfer'");
    }

    @Override
    public String approveTransferUpdate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveTransferUpdate'");
    }

    @Override
    public void approveTransferUpdateValidator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveTransferUpdateValidator'");
    }

}
