package ru.practicum.exploreWithMe.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiError {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private String reason;
    private HttpStatus status;
    private LocalDateTime timestamp;
}