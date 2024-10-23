package com.efr.virtualBank.model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс представляет банковский счет с поддержкой многопоточности.
 */
public class BankAccount {
    private final UUID uuid = UUID.randomUUID();
    private BigDecimal balance;
    private final Lock balanceLock = new ReentrantLock(); // Блокировка для операций с балансом

    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Пополнение баланса счета.
     * Потокобезопасная операция с блокировкой.
     */
    public boolean deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balanceLock.lock();
            try {
                balance = balance.add(amount);
                return true;
            } finally {
                balanceLock.unlock();
            }
        }
        return false;
    }

    /**
     * Снятие средств со счета, если баланс достаточен.
     * Потокобезопасная операция с блокировкой.
     */
    public boolean withdraw(BigDecimal amount) {
        balanceLock.lock();
        try {
            if (balance.compareTo(amount) >= 0 && amount.compareTo(BigDecimal.ZERO) > 0){
                balance = balance.subtract(amount);
                return true;
            }
            return false;
        } finally {
            balanceLock.unlock();
        }
    }
}
