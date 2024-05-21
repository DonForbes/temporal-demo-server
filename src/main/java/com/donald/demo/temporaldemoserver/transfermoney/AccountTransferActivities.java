package com.donald.demo.temporaldemoserver.transfermoney;

import com.donald.demo.temporaldemoserver.transfermoney.model.ExecutionScenario;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface AccountTransferActivities {

    Boolean validate(ExecutionScenario scenario);

 /*
     String withdraw(float amount, ExecutionScenario scenario);

    MoneyTransferResponse deposit (String idempotencyKey, float amount, ExecutionScenario scenario);

    boolean undoWithdraw(float amount);
*/

}
