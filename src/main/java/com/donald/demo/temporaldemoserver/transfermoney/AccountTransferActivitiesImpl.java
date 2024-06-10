package com.donald.demo.temporaldemoserver.transfermoney;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.donald.demo.temporaldemoserver.transfermoney.model.ExecutionScenario;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransfer;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;
import com.donald.demo.temporaldemoserver.transfermoney.util.IdGenerator;

import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;

@Component
@ActivityImpl(taskQueues = "TransferMoneyDemoTaskQueue")
public class AccountTransferActivitiesImpl implements AccountTransferActivities {
  private static final Logger log = LoggerFactory.getLogger(AccountTransferActivitiesImpl.class);

    @Override
    public Boolean validate(MoneyTransfer moneyTransfer) {
        if (moneyTransfer.getWorkflowOption() == ExecutionScenario.INVALID_ACCOUNT)
            return Boolean.valueOf(false);
        else
            return Boolean.valueOf(true);
    }  //End validate

    @Override
    public boolean withdraw(MoneyTransfer moneyTransfer) {
        log.debug("Witdraw for input details of [" + moneyTransfer.toString() + "]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean undoWithdraw(MoneyTransfer moneyTransfer) {
        log.debug("Reverting withdrawal for [" + moneyTransfer.toString() + "]");
        return true;
    }

    @Override
    public boolean deposit(MoneyTransfer moneyTransfer) {
        log.debug("Deposit for input details of [" + moneyTransfer.toString() + "]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
