package ru.yandex.practicum.catsgram.model;

import lombok.Data;

@Data
public class Image { //  модель, которая описывает изображения, прикреплённые к конкретному сообщению.

    Long id; //  уникальный идентификатор изображения,

    Long postId; //  — уникальный идентификатор поста, к которому прикреплено изображение

    String originalFileName; // — имя файла, который содержит изображение,

    String filePath; // — путь, по которому изображение было сохранено
}
