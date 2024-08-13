package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Post { //  модель, описывающая сообщения в социальной сети. У неё следующие поля данных:

    Long id; // уникальный идентификатор сообщения
    Long authorId; // пользователь, который создал сообщение,
    String description;  //  текстовое описание сообщения
    Instant postDate; // дата и время создания сообщения
    private final Instant creationDate = Instant.now();

    public Instant getCreationDate() {
        return creationDate;
    }
}
