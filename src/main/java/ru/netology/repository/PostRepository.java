package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
@Repository
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> DATA_BASE = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(DATA_BASE.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(DATA_BASE.get(id));
    }

    public Post save(Post post) {
        synchronized (this) {
            if (post.getId() == 0) {
                var key = (long) DATA_BASE.size();
                post.setId(key);
                DATA_BASE.put(key, post);
                return DATA_BASE.get(key);
            }
        }

        return Optional
                .ofNullable(DATA_BASE.computeIfPresent(post.getId(), (v, v1) -> {
                    v1.setContent(post.getContent());
                    return v1;
                }))
                .orElseThrow(NotFoundException::new);
    }

    public void removeById(long id) {
        DATA_BASE.remove(id);
    }
}
