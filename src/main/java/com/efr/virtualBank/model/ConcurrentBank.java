package com.efr.virtualBank.model;

import com.efr.virtualBank.service.TransferHelper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {

    // Потокобезопасная карта для хранения счетов по их уникальным UUID.
    private final Map<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    /**
     * Получение общего баланса всех счетов в банке.
     * Операция потокобезопасна, так как ConcurrentHashMap обеспечивает безопасный доступ.
     */
    public BigDecimal getTotalBalance() {
        return accounts.values().stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Создает новый банковский счет с начальным балансом.
     * @param balance начальный баланс счета
     * @return созданный банковский счет
     */
    public BankAccount createAccount(BigDecimal balance) {
        BankAccount bankAccount = new BankAccount(balance);
        accounts.put(bankAccount.getUuid(), bankAccount); // Потокобезопасное добавление
        return bankAccount;
    }

    /**
     * Выполняет перевод средств между двумя счетами.
     * @param fromAccount счет, с которого переводятся средства
     * @param toAccount счет, на который переводятся средства
     * @param amount сумма перевода
     */
    public void transfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        TransferHelper transferHelper = new TransferHelper(fromAccount, toAccount, amount);
        transferHelper.executeTransfer();
    }
}
