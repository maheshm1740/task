package com.task.taskmanager.controller;

import com.task.taskmanager.dto.ServerResponse;
import com.task.taskmanager.dto.TaskRequest;
import com.task.taskmanager.dto.TaskResponse;
import com.task.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task Management", description = "APIs for managing user tasks")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task", description = "Creates a task for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ServerResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest dto){

        TaskResponse task = taskService.createTask(dto);

        ServerResponse<TaskResponse> response =
                new ServerResponse<>(true,"Task created successfully",task);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get paginated tasks",
            description = "Returns tasks of the authenticated user with pagination and sorting"
    )
    public ResponseEntity<ServerResponse<Page<TaskResponse>>> getTasks(
            @ParameterObject
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {

        Page<TaskResponse> tasks = taskService.getAllTasks(pageable);

        ServerResponse<Page<TaskResponse>> response =
                new ServerResponse<>(true,"Tasks fetched successfully",tasks);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task by ID", description = "Fetch a specific task belonging to the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServerResponse<TaskResponse>> getTask(
            @Parameter(description = "Task ID") @PathVariable Long id) {

        TaskResponse task = taskService.getTaskById(id);

        ServerResponse<TaskResponse> response =
                new ServerResponse<>(true,"Task fetched successfully",task);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Update task", description = "Updates title, description or status of a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServerResponse<TaskResponse>> updateTask(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Valid @RequestBody TaskRequest dto) {

        TaskResponse task = taskService.updateTask(id, dto);

        ServerResponse<TaskResponse> response =
                new ServerResponse<>(true,"Task updated successfully",task);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Delete task", description = "Deletes a task (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse<String>> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

        ServerResponse<String> response =
                new ServerResponse<>(true, "Task deleted successfully", null);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all tasks (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All tasks fetched"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<ServerResponse<Page<TaskResponse>>> getAllTasksForAdmin(
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        Page<TaskResponse> tasks = taskService.getAllTasksForAdmin(pageable);

        ServerResponse<Page<TaskResponse>> response =
                new ServerResponse<>(true, "All tasks fetched successfully", tasks);

        return ResponseEntity.ok(response);
    }
}