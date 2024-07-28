package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Image { //  модель, которая описывает изображения, прикреплённые к конкретному сообщению.

    Long id; //  уникальный идентификатор изображения,

    long postId; //  — уникальный идентификатор поста, к которому прикреплено изображение

    String originalFileName; // — имя файла, который содержит изображение,

    String filePath; // — путь, по которому изображение было сохранено
}
