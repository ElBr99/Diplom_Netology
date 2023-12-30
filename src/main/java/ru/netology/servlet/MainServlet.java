package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.dto.RequestDto;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static ru.netology.controller.WebContext.*;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse response) {
        try {
            final var requestDto = new RequestDto(req.getRequestURI(), req.getMethod());
            var handlerMap = Map.<RequestDto, Runnable>of(
                    new RequestDto(API_POSTS, GET_METHOD), () -> controller.all(response),
                    new RequestDto(API_POSTS_ID, GET_METHOD), () -> controller.getById(extractId(req.getRequestURI()), response),
                    new RequestDto(API_POSTS, POST_METHOD), () -> {
                        try {
                            controller.save(req.getReader(), response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    new RequestDto(API_POSTS, DELETE_METHOD), () -> controller.removeById(extractId(req.getRequestURI()), response)
            );

            Optional.ofNullable(handlerMap.get(requestDto)).orElseThrow(NotFoundException::new).run();
        } catch (NotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private static long extractId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(STR) + 1));
    }
}