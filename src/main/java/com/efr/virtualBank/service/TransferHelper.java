package com.efr.virtualBank.service;

import com.efr.virtualBank.exceptions.InsufficientFundsException;
import com.efr.virtualBank.model.BankAccount;

import java.math.BigDecimal;

public class TransferHelper {
    private final BankAccount fromAccount;
    private final BankAccount toAccount;
    private final BigDecimal amount;

    public TransferHelper(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public void executeTransfer() {
        // Сортировка счетов по UUID для предотвращения взаимной блокировки
        BankAccount firstLock, secondLock;
        if (fromAccount.getUuid().compareTo(toAccount.getUuid()) < 0) {
            firstLock = fromAccount;
            secondLock = toAccount;
        } else {
            firstLock = toAccount;
            secondLock = fromAccount;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (!fromAccount.withdraw(amount)) {
                    throw new InsufficientFundsException("Insufficient funds for withdrawal");
                }
                toAccount.deposit(amount);
            }
        }
    }
}
