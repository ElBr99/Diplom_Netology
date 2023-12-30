package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;

public interface CrudService<T> {
    List<T> all();

    T getById(long id);

    Post save(T post);

    void removeById(long id);

    Class<T> getType();

}
