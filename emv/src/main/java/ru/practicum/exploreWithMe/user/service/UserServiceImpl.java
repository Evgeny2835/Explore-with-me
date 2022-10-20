package ru.practicum.exploreWithMe.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.exception.exceptions.ConflictException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;
import ru.practicum.exploreWithMe.user.dto.UserFullDto;
import ru.practicum.exploreWithMe.user.dto.UserNewDto;
import ru.practicum.exploreWithMe.user.mapper.UserMapper;
import ru.practicum.exploreWithMe.user.model.User;
import ru.practicum.exploreWithMe.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserFullDto> get(Integer[] ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
        List<User> users;
        if (ids.length != 0) {
            users = userRepository.findAllByIdIn(List.of(ids), pageRequest);
            log.info("Got a list of users in the repository, requested by users: {}, from={}, size={}",
                    ids.length, from, size);
        } else {
            users = userRepository.findAll(pageRequest).getContent();
            log.info("Got a list of users in the repository, from={}, size={}", from, size);
        }
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserFullDto create(UserNewDto userNewDto) {
        try {
            User user = userMapper.toUser(userNewDto);
            log.info("Adding a new user to the repository, email={}", user.getEmail());
            return userMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Email is already in use: %s", userNewDto.getEmail()));
        }
    }

    public void delete(Integer userId) {
        try {
            log.info("Deleting a user from the repository, id={}", userId);
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User not found: id=%d", userId));
        }
    }

    public User get(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User not found: id=%d", userId)));
    }
}