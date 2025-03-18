package com.lshdainty.myhr.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final int code;
    private final String message;
    private final int count;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", getCount(data), data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(0, "success", 0, null);
    }

    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>(0, "fail", 0, data);
    }

    public static <T> ApiResponse<T> fail() {
        return new ApiResponse<>(0, "fail", 0, null);
    }

    private static <T> int getCount(T data) {
        if (data instanceof Object[]) { // array
            return ((Object[]) data).length;
        } else if (data instanceof Collection) {    // List, Set, Map
            return ((Collection<?>) data).size();
        } else {
            // not thing to do
            return 0;
        }
    }
}
