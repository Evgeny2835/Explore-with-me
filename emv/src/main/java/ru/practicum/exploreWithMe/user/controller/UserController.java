package ru.practicum.exploreWithMe.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.user.dto.UserFullDto;
import ru.practicum.exploreWithMe.user.dto.UserNewDto;
import ru.practicum.exploreWithMe.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserFullDto> getUsers(
            @RequestParam(name = "ids") Integer[] ids,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return userService.get(ids, from, size);
    }

    @PostMapping
    public UserFullDto create(
            @RequestBody @Valid UserNewDto userNewDto) {
        return userService.create(userNewDto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") @Positive int userId) {
        userService.delete(userId);
    }
}