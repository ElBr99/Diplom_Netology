package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.service.CrudService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class GenericController<T> {
    public static final String APPLICATION_JSON = "application/json";
    private static Gson gson = new Gson();
    private final CrudService<T> service;

    public GenericController(CrudService<T> service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        writeInResponse(response, data);
    }

    public void getById(long id, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        final var post = service.getById(id);
        writeInResponse(response, gson.toJson(post));
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var post = gson.fromJson(body, service.getType());
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        service.removeById(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static <T> void writeInResponse(HttpServletResponse response, T data) {
        try {
            response.getWriter().print(gson.toJson(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
