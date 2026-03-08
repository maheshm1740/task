package com.task.taskmanager.dto;

import com.task.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Size(max = 500)
    private String description;

    private TaskStatus status;
}
