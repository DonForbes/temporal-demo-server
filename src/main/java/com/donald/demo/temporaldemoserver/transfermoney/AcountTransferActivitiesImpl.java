package com.donald.demo.temporaldemoserver.transfermoney;

import org.springframework.stereotype.Component;

import com.donald.demo.temporaldemoserver.transfermoney.model.ExecutionScenario;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;
import com.donald.demo.temporaldemoserver.transfermoney.util.IdGenerator;

import io.temporal.spring.boot.ActivityImpl;

@Component
@ActivityImpl(taskQueues = "TransferMoneyDemoTaskQueue")
public class AcountTransferActivitiesImpl implements AccountTransferActivities {

    @Override
    public Boolean validate(ExecutionScenario scenario) {
         return Boolean.valueOf(true);
    }

/*    
    @Override
    public String withdraw(float amount, ExecutionScenario scenario) {
       return "withdraw-success";
    }

    @Override
    public MoneyTransferResponse deposit(String idempotencyKey, float amount, ExecutionScenario scenario) {
        MoneyTransferResponse response = new MoneyTransferResponse();
        response.setChargeId(IdGenerator.generateTransferId());
        return response;
    }

    @Override
    public boolean undoWithdraw(float amount) {
        return true;
    }

*/
}
