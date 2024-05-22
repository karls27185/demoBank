package com.example.demo.util;

import com.example.demo.model.BankAccount;
import com.example.demo.service.BankTransactionService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс ScheduledTasks представляет собой компонент Spring, который выполняет задачу по начислению процентов на банковские счета пользователей каждые 60 секунд.
 * */

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final UserService userService;
    private final BankTransactionService bankTransactionService;

    public ScheduledTasks(UserService userService, BankTransactionService bankTransactionService) {
        this.userService = userService;
        this.bankTransactionService = bankTransactionService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void chargeInterestTask() {
        if (logger.isInfoEnabled()) {
            logger.info("Start of the task 'ChargeInterest' / Запуск регламентного задания 'Начисление процентов'");
        }
        List<BankAccount> accounts = userService.getAllBankAccounts();
        for (BankAccount account : accounts) {
            bankTransactionService.chargeInterest(account);
        }
    }
}
