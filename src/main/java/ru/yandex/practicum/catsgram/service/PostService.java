package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    UserService userService;

    @Autowired
    PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(Long size, Long from, String sort) {

        return posts.values().stream()
                .sorted((p0, p1) -> {
                    long comp = p0.getCreationDate().compareTo(p1.getCreationDate());
                    if (sort.equals("desc")) {
                        comp = -1 * comp;
                    }
                    return (int) comp;
                }).skip(from).limit(size).collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException(String.format("Автор с id = %d не найден", post.getAuthorId()));
        }

        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Optional<Post> findPostById(Long postById) {
        return Optional.ofNullable(posts.get(postById));
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}