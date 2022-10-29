package ru.practicum.exploreWithMe.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import ru.practicum.exploreWithMe.exception.exceptions.ConditionException;
import ru.practicum.exploreWithMe.exception.exceptions.ConflictException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;
import ru.practicum.exploreWithMe.exception.exceptions.ValidationException;
import ru.practicum.exploreWithMe.exception.model.ApiError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        return ApiError.builder()
                .errors(List.of(Arrays.toString(e.getStackTrace())))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConditionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleConditionException(ConditionException e) {
        return ApiError.builder()
                .errors(List.of(Arrays.toString(e.getStackTrace())))
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found")
                //  "Event with id=21 was not found."
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleConflictException(HttpServerErrorException.InternalServerError e) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Error occurred")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
