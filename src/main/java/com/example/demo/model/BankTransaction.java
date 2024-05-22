package com.example.demo.model;

import com.example.demo.util.AppRuntimeException;
import com.example.demo.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс BankTransaction представляет собой транзакцию между двумя банковскими счетами, включая операции по внесению и снятию средств. Он содержит информацию о самой транзакции, такую как сумма, статус и дату создания, а также ссылки на счета отправителя и получателя.
 * */

@Entity
@Getter
@Setter
public class BankTransaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1188292871957752186L;//идентификатора версии сериализуемого класса

    private static final Logger logger = LoggerFactory.getLogger(BankTransaction.class);

    @Transient
    private final Lock balanceChangeLock;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionReference;

    @JsonBackReference(value = "account-fromTransaction")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountId")
    private BankAccount fromAccount;

    @JsonBackReference(value = "account-toTransaction")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccountId")
    private BankAccount toAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @CreationTimestamp
    @DateTimeFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime createdAt;

    public BankTransaction() {
        this.balanceChangeLock = new ReentrantLock();
    }

    public BankTransaction(String transactionReference, BankAccount fromAccount, BankAccount toAccount,
                           BigDecimal amount, TransactionStatus status) {
        this.transactionReference = transactionReference;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.status = status;
        this.balanceChangeLock = new ReentrantLock();
    }

    /**
     * метод добавляет сумму amount к балансу счёта toAccount
     * */
    public void deposit(BigDecimal amount) {
        balanceChangeLock.lock();
        try {
            BigDecimal balance = toAccount.getBalance().add(amount);
            toAccount.setBalance(balance);
        } finally {
            balanceChangeLock.unlock();
        }
    }

    /**
     * метод вычитает сумму amount из баланса счёта fromAccount. Если после вычета баланс становится отрицательным, операция отменяется и выводится соответствующее сообщение
     * */
    public void withdraw(BigDecimal amount) {
        balanceChangeLock.lock();
        try {
            BigDecimal balance = fromAccount.getBalance();
            if (balance.compareTo(amount) >= 0) {
                balance = fromAccount.getBalance().subtract(amount);
                fromAccount.setBalance(balance);
            } else {
                logger.info("Try to withdraw from account / Попытка снятия со счета: {} {} ", toAccount, amount);
                logger.info("Insufficient funds. Withdrawal cancelled. / Недостаточно средств. Операция отменена");
                throw new AppRuntimeException();
            }
        } finally {
            balanceChangeLock.unlock();
        }
    }

    @Override
    public String toString() {
        return transactionReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Objects.equals(id, that.id) && Objects.equals(transactionReference, that.transactionReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionReference);
    }
}
