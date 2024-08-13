package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();


    public Optional<User> findUserById(Long userId){
        return Optional.ofNullable(users.get(userId));
    }

    public Collection<User> getUserList(Long size, Long from, String sort) {

        return users.values().stream()
                .sorted((p0, p1) -> {
                    long comp = p0.getRegistrationDate().compareTo(p1.getRegistrationDate());
                    if (sort.equals("desc")){
                        comp = -1 * comp;
                    }
                    return (int) comp;
                }).skip(from).limit(size).collect(Collectors.toList());
    }


    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        } else if (checkEmail(user)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);

        return user;
    }


    public User updateUser(User user) {

        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = users.get(user.getId());
        if (checkEmail(user)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        } else if (user.getEmail() == null && user.getUsername() == null && user.getPassword() == null) {
            return oldUser;
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());

        return oldUser;
    }

    private boolean checkEmail(User user) {
        for (User user1 : users.values()) {
            if (user1.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
