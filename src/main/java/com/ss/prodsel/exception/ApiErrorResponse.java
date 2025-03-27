package com.ss.prodsel.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiErrorResponse(HttpStatus status, String uri, String message, List<String> errors) {
}
