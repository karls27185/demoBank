package com.example.demo.service.validation;

import com.example.demo.model.Email;
import com.example.demo.model.Phone;
import com.example.demo.model.User;
import com.example.demo.repository.EmailRepository;
import com.example.demo.repository.PhoneRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Класс UserValidator представляет собой сервис, который проверяет валидность объекта User.
 * */

@Service
@Transactional(readOnly = true)
public class UserValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);
    private UserRepository userRepository;
    private PhoneRepository phoneRepository;
    private EmailRepository emailRepository;

    public UserValidator() {
    }

    @Autowired
    public UserValidator(UserRepository userRepository, PhoneRepository phoneRepository, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.emailRepository = emailRepository;
    }

    /**
     * Метод supports определяет, может ли валидатор проверять объект заданного класса.
     * В данном случае, это класс User
     * */
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Метод validate выполняет проверку валидности объекта User. Он проверяет уникальность логина, баланс аккаунта, а также уникальность телефонных номеров и адресов электронной почты.
     * Если проверка не проходит, метод добавляет сообщение об ошибке в коллекцию ошибок.
     * */
    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        User user = (User) target;
        if (userRepository.findUserByLogin(user.getLogin()) != null) {
            String str = String.format("Login is already in use / Логин уже используется: %s",
                    user.getLogin());
            errors.rejectValue("login", "", str);
            logger.warn(str);
        }
        if (user.getAccount().getBalance().compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            String str = "The initial balance must be greater than zero / Начальный баланс должен быть больше нуля";
            errors.rejectValue("account", "", str);
            logger.warn(str);
        }
        for (Phone phone : user.getPhones()) {
            if (phoneRepository.findPhoneByNumber(phone.getNumber()) != null) {
                String str = String.format("Phone number is already in use / Телефонный номер уже используется: %s",
                        phone.getNumber());
                errors.rejectValue("phones", "", str);
                logger.warn(str);
            }
        }
        for (Email email : user.getEmails()) {
            if (emailRepository.findEmailByContent(email.getContent()) != null) {
                String str = String.format("Email is already in use / Email уже используется: %s", email.getContent());
                errors.rejectValue("emails", "", str);
                logger.warn(str);
            }
        }
    }
}
