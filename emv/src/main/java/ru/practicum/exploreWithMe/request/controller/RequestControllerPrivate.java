package ru.practicum.exploreWithMe.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.request.dto.RequestDto;
import ru.practicum.exploreWithMe.request.service.RequestServicePrivate;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestControllerPrivate {
    private final RequestServicePrivate requestServicePrivate;

    @PostMapping
    public RequestDto create(
            @PathVariable @Positive int userId,
            @RequestParam @Positive int eventId) {
        return requestServicePrivate.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable @Positive int userId,
                             @PathVariable @Positive int requestId) {
        return requestServicePrivate.cancel(userId, requestId);
    }

    @GetMapping
    public List<RequestDto> getAllByRequester(@PathVariable @Positive int userId) {
        return requestServicePrivate.getAllByRequester(userId);
    }
}