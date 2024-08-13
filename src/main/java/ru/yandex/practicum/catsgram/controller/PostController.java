package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) Long page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Long size,
            @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
        Long from = page * size;

        SortOrder sortOrder = SortOrder.from(sort);
        if (sortOrder == null) {
            throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: ask или desc");
        }
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }

        if (from < 0) {
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }


        return postService.findAll(size, from, sort);
    }

    @GetMapping("{id}")
    public Optional<Post> findPostById(@PathVariable("id") Long postById) {
        return postService.findPostById(postById);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post newPost = postService.create(post);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }


}