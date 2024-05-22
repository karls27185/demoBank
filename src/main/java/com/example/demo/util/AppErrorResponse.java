package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс AppErrorResponse предназначен для хранения информации об ошибке, которая произошла в приложении.
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppErrorResponse {
    private String message;
    private long timestamp;
}
