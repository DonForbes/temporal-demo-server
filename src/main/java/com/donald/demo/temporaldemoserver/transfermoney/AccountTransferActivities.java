package com.donald.demo.temporaldemoserver.transfermoney;


import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransfer;
import com.donald.demo.temporaldemoserver.transfermoney.model.MoneyTransferResponse;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface AccountTransferActivities {

    Boolean validate(MoneyTransfer moneyTransfer);
 
    boolean withdraw(MoneyTransfer moneyTransfer);

    boolean deposit (MoneyTransfer moneyTransfer);

    boolean undoWithdraw(MoneyTransfer moneyTransfer);

}
