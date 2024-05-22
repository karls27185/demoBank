package com.example.demo.web.controller;

import com.example.demo.model.BankAccount;
import com.example.demo.model.BankTransaction;
import com.example.demo.service.BankTransactionService;
import com.example.demo.util.AppErrorResponse;
import com.example.demo.util.AppRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demo.util.ErrorsUtil.returnErrorsToClient;

/**
 * Класс BankTransactionController является контроллером REST, который предоставляет API для работы с банковскими транзакциями.
 * */

@RestController
@RequestMapping("api/transactions")
public class BankTransactionController {
    private static final Logger logger = LoggerFactory.getLogger(BankTransactionController.class);
    private final BankTransactionService bankTransactionService;

    @Autowired
    public BankTransactionController(BankTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<BankTransaction>> getAllBankTransactions() {
        List<BankTransaction> transactions = bankTransactionService.getAllBankTransactions();
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankTransaction> getBankTransaction(@PathVariable("id") Long id) {
        Optional<BankTransaction> transaction = bankTransactionService.getByTransactionId(id);
        return ResponseEntity.of(transaction);
    }

    /**
     * Метод, который переводит средства между двумя банковскими счетами.
     * Метод проверяет наличие ошибок в данных запроса и, если ошибки обнаружены, возвращает клиенту соответствующее сообщение об ошибке.
     * В противном случае метод вызывает метод internalFundTransfer службы bankTransactionService для выполнения перевода средств
     * */
    @PostMapping("/transfer")
    public ResponseEntity<BankTransaction> transferFunds(@RequestBody BankAccount fromAccount, BankAccount toAccount,
                                                         BigDecimal amount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        bankTransactionService.internalFundTransfer(fromAccount, toAccount, amount);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    private ResponseEntity<AppErrorResponse> handleException(AppRuntimeException e) {
        AppErrorResponse response = new AppErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
