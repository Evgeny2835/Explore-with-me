package ru.practicum.exploreWithMe.user.service;

import ru.practicum.exploreWithMe.user.dto.UserFullDto;
import ru.practicum.exploreWithMe.user.dto.UserNewDto;
import ru.practicum.exploreWithMe.user.dto.UserShortDto;
import ru.practicum.exploreWithMe.user.model.User;

import java.util.List;

public interface UserService {

    List<UserFullDto> get(Integer[] ids, Integer from, Integer size);

    UserFullDto create(UserNewDto userNewDto);

    void delete(Integer userId);

    User get(Integer userId);
}