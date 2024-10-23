
# Виртуальный Банк

**Виртуальный Банк** — это простой демонстрационный проект, реализующий работу банковских счетов и переводы средств между ними с учетом многопоточного доступа. В проекте используется Java и пакет `java.util.concurrent` для обеспечения потокобезопасности при работе с банковскими счетами.

## Функционал

- Создание банковских счетов с начальным балансом.
- Получение общего баланса всех счетов.
- Переводы средств между счетами с учетом многопоточной обработки.
- Обработка исключений, связанных с недостатком средств.

## Технологии

- **Язык программирования**: Java 8+
- **Потокобезопасность**: Использование `ConcurrentHashMap` для хранения счетов и синхронизация операций `withdraw` и `deposit`.
- **Исключения**: Обработка недостатка средств через кастомное исключение `InsufficientFundsException`.

## Структура проекта

- `com.efr.virtualBank.model` — модели данных для банковских счетов.
- `com.efr.virtualBank.service` — служебный класс для выполнения трансферов между счетами.
- `com.efr.virtualBank.exceptions` — кастомные исключения для обработки ошибок.
- `com.efr.virtualBank.app` — основной класс для запуска и демонстрации работы системы.

## Примеры использования

```java
ConcurrentBank bank = new ConcurrentBank();

BankAccount account1 = bank.createAccount(new BigDecimal(100));
BankAccount account2 = bank.createAccount(new BigDecimal(700));

System.out.println("Общий баланс: " + bank.getTotalBalance());

Thread transfer1 = new Thread(() -> bank.transfer(account1, account2, new BigDecimal(100)));
Thread transfer2 = new Thread(() -> bank.transfer(account2, account1, new BigDecimal(50)));

transfer1.start();
transfer2.start();

transfer1.join();
transfer2.join();

System.out.println("Баланс счета 1: " + account1.getBalance());
System.out.println("Баланс счета 2: " + account2.getBalance());
```

## Важные замечания

- **Потокобезопасность**: Методы `withdraw` и `deposit` синхронизированы, чтобы избежать состояния гонки при одновременных операциях с балансом счета.
- **Переводы между счетами**: Класс `TransferHelper` обеспечивает безопасные переводы между счетами, блокируя их в определённом порядке для предотвращения deadlock'ов.