package ru.netology.controller;

import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

@Controller
public class PostController extends GenericController<Post> {

    public PostController(PostService service) {
        super(service);
    }
}
