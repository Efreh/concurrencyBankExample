package com.efr.virtualBank.app;

import com.efr.virtualBank.model.BankAccount;
import com.efr.virtualBank.model.ConcurrentBank;

import java.math.BigDecimal;

/**
 * Пример использования многопоточной модели банка с переводами между счетами.
 */
public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        BankAccount bankAccount1 = bank.createAccount(new BigDecimal(100));
        BankAccount bankAccount2 = bank.createAccount(new BigDecimal(700));

        System.out.println("Initial Total Balance: " + bank.getTotalBalance());
        System.out.println("Account ID : "+ bankAccount1.getUuid()+ ", balance : "+ bankAccount1.getBalance());
        System.out.println("Account ID : "+ bankAccount2.getUuid()+ ", balance : "+ bankAccount2.getBalance());

        // Создаем потоки для выполнения переводов
        Thread transferThread1 = new Thread(() -> bank.transfer(bankAccount1, bankAccount2, new BigDecimal(200)));
        Thread transferThread2 = new Thread(() -> bank.transfer(bankAccount2, bankAccount1, new BigDecimal(100)));

        // Запускаем потоки для выполнения переводов
        transferThread2.start();
        transferThread1.start();

        try {
            // Ожидаем завершения потоков
            transferThread1.join();
            transferThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--------------------");

        System.out.println("Final Total Balance: " + bank.getTotalBalance());
        System.out.println("Account ID : "+ bankAccount1.getUuid()+ ", balance : "+ bankAccount1.getBalance());
        System.out.println("Account ID : "+ bankAccount2.getUuid()+ ", balance : "+ bankAccount2.getBalance());
    }
}
