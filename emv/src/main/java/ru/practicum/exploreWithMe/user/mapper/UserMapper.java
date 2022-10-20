package ru.practicum.exploreWithMe.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.user.dto.UserFullDto;
import ru.practicum.exploreWithMe.user.dto.UserNewDto;
import ru.practicum.exploreWithMe.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserNewDto userNewDto) {
        return User.builder()
                .email(userNewDto.getEmail())
                .name(userNewDto.getName())
                .build();
    }

    public UserFullDto toUserDto(User user) {
        return UserFullDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}