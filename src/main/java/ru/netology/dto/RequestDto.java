package ru.netology.dto;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static ru.netology.controller.WebContext.API_POSTS_ID;

public final class RequestDto {

    private String pattern;
    private final String method;

    public RequestDto(String path, String method) {
        if (path.matches(API_POSTS_ID)) {
            pattern = API_POSTS_ID;
        } else {
            this.pattern = path;
        }
        this.method = method;
    }

    public String getPattern() {
        return pattern;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestDto that = (RequestDto) o;

        if (!this.pattern.equals(that.pattern)) return false;
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        int result = pattern != null ? pattern.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
