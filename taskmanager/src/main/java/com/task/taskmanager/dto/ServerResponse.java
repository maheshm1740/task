package com.task.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public ServerResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
