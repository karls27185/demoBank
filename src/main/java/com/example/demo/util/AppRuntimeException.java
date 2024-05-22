package com.example.demo.util;

/**
 * Класс AppRuntimeException расширяет класс RuntimeException и служит для создания исключений в приложении.
 * */

public class AppRuntimeException extends RuntimeException {
    public AppRuntimeException() {
    }

    public AppRuntimeException(String message) {
        super(message);
    }
}
