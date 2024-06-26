package com.donald.demo.temporaldemoserver.transfermoney;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

import org.apache.catalina.util.ServerInfo;
import org.slf4j.Logger;

import com.donald.demo.temporaldemoserver.hello.HelloActivity;
import com.donald.demo.temporaldemoserver.hello.HelloWorkflowImpl;
import com.donald.demo.temporaldemoserver.transfermoney.model.ExecutionScenario;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransfer;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferState;
import com.donald.demo.temporaldemoserver.transfermoney.model.TransferState;
import com.donald.demo.temporaldemoserver.transfermoney.util.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.internal.worker.WorkflowExecutionException;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

@WorkflowImpl(taskQueues = "TransferMoneyDemoTaskQueue")
public class TransferMoneyWorkflowImpl implements TransferMoneyWorkflow {
    private MoneyTransferState moneyTransferState = new MoneyTransferState();
    
    public static final Logger logger = Workflow.getLogger(TransferMoneyWorkflowImpl.class);

    private AccountTransferActivities activity = Workflow.newActivityStub(
            AccountTransferActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(120)).build());

    @Override
    public MoneyTransferResponse transfer(MoneyTransfer moneyTransfer) {
        logger.debug(("Entered - transfer method started."));
        moneyTransferState.setProgressPercentage(10);
        moneyTransferState.setWorkflowStatus("RUNNING");
        moneyTransferState.setWorkflowId(Workflow.getInfo().getWorkflowId());
      
        Workflow.sleep(Duration.ofSeconds(5));

        //  ***************************
        //  ***     VALIDATION      ***
        //  ***************************
        if  (activity.validate(moneyTransfer) == false) {
           moneyTransferState.setProgressPercentage(100);
           moneyTransferState.setTransferState(TransferState.VALIDATION_FAILED);
           moneyTransferState.setWorkflowStatus("FAILED");

           throw ApplicationFailure.newFailure("The transfer failed to validate.", "ValidateionFailure");
        }
        moneyTransferState.setTransferState(TransferState.VALIDATED);
        moneyTransferState.setProgressPercentage(40);

        //  ***************************
        //  ***     APPROVAL        ***
        //  ***************************
        if ((moneyTransfer.getWorkflowOption() == ExecutionScenario.HUMAN_IN_LOOP) | (Long.parseLong(moneyTransfer.getAmount()) > 10000) )
        {
            moneyTransferState.setApprovalRequired(true);
            boolean receivedSignal = Workflow.await(Duration.ofSeconds(moneyTransferState.getApprovalTime()), () -> moneyTransferState.getApprovedTime() != "");

            if (!receivedSignal) {
                logger.error("Approval not received within the time limit.  Failing the workflow");
                moneyTransferState.setTransferState(TransferState.APPROVAL_TIMED_OUT);
                throwApplicationFailure("Transfer not approved within timelimit.", "ApprovalTimeout", null);
            }
            else
                moneyTransferState.setTransferState(TransferState.APPROVED);
        }
 
        //  ***************************
        //  ***     WITHDRAWAL      ***
        //  ***************************
        if (activity.withdraw(moneyTransfer))
            moneyTransferState.getMoneyTransferResponse().setWithdrawId(IdGenerator.generateTransferId());
        else
        {
            moneyTransferState.setTransferState(TransferState.WITHDRAW_FAILED);
            throwApplicationFailure("Withdrawal Failed to complete successsfully.", "WithdrawalFailure", null);
        }

        moneyTransferState.setTransferState(TransferState.FUNDS_WITHDRAWN);
        moneyTransferState.setProgressPercentage(50);

        Workflow.sleep(Duration.ofSeconds(4));

        //  ***************************
        //  ***      DEPOSIT        ***
        //  ***************************
        if (activity.deposit(moneyTransfer))
            moneyTransferState.getMoneyTransferResponse().setChargeId(IdGenerator.generateTransferId());
        else
        {
            moneyTransferState.setTransferState(TransferState.DEPSIT_FAILED);
            throwApplicationFailure("Deposit Failed to complete successsfully.", "DepositFailure", null);
        }

        moneyTransferState.setTransferState(TransferState.FUNDS_DEPOSITED);
        moneyTransferState.setProgressPercentage(70);

        Workflow.sleep(Duration.ofSeconds(3));

        moneyTransferState.setProgressPercentage(100);
        moneyTransferState.setTransferState(TransferState.COMPLETED);
        moneyTransferState.setWorkflowStatus("COMPLETED");
        return moneyTransferState.getMoneyTransferResponse();
    }

    private void throwApplicationFailure(String message, String type, Object details) throws ApplicationFailure
    {
        moneyTransferState.setWorkflowStatus("FAILED");
        throw ApplicationFailure.newFailure(message, type, details);      
    }// End throwApplicationFailure


    @Override
    public MoneyTransferState getStateQuery() throws JsonProcessingException {
        logger.debug("Querying workflow - " + this.moneyTransferState.toString());
        return this.moneyTransferState;
    }


    @Override
    public void approveTransfer() {
        this.moneyTransferState.setApprovedTime(IdGenerator.returnFormattedWorkflowDate("dd MMM yyyy HH:mm:ss"));
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
