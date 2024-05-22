package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * класс BankAccount представляет собой модель банковского счёта с возможностью отслеживания транзакций и связи с пользователем
 * */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = 8079071627628658508L;//идентификатора версии сериализуемого класса

    @JsonManagedReference(value = "account-fromTransaction")//при сериализации объекта BankAccount, список fromTransactions будет сериализован, даже если он уже был сериализован как часть другого объекта
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)//список fromTransactions в объекте BankAccount управляется полем fromAccount в объекте BankTransaction. Каскад CascadeType.ALL означает, что любые операции, выполняемые над объектом BankAccount, будут автоматически применены к связанным объектам BankTransaction
    private final List<BankTransaction> fromTransactions = new ArrayList<>();

    @JsonManagedReference(value = "account-toTransaction")//при сериализации объекта BankAccount, список toTransactions будет сериализован, даже если он уже был сериализован как часть другого объекта
    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)//список toTransactions в объекте BankAccount управляется полем toAccount в объекте BankTransaction. Каскад CascadeType.ALL означает, что любые операции, выполняемые над объектом BankAccount, будут автоматически применены к связанным объектам BankTransaction
    private final List<BankTransaction> toTransactions = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private BigDecimal initialBalance;

    private BigDecimal balance;

    @JsonBackReference(value = "user-account")//при сериализации объекта BankAccount, поле user будет игнорироваться, если оно уже было сериализовано как часть другого объекта
    @OneToOne(fetch = FetchType.EAGER)//связь между BankAccount и User должна быть загружена сразу же при запросе объекта BankAccount. Это означает, что при получении объекта BankAccount, связанный с ним объект User также будет загружен автоматически
    @JoinColumn(name = "userId")
    private User user;

    public BankAccount(Long id, String number, BigDecimal balance, User user) {
        this.id = id;
        this.number = number;
        this.initialBalance = balance;
        this.balance = balance;
        this.user = user;
    }

    @Override
    public String toString() {
        return number + " {" + user + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount account = (BankAccount) o;
        return Objects.equals(id, account.id) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
