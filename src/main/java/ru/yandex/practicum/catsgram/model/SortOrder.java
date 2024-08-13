package ru.yandex.practicum.catsgram.model;

public enum SortOrder {
    ASK,
    DESC;

    public static SortOrder from(String sort) {
        if ("ask".equalsIgnoreCase(sort)) {
            return ASK;
        }
        if ("desc".equalsIgnoreCase(sort)) {
            return DESC;
        }

        return null;
    }
}
