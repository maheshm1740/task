package com.task.taskmanager.service;

import com.task.taskmanager.dto.TaskRequest;
import com.task.taskmanager.dto.TaskResponse;
import com.task.taskmanager.entity.Task;
import com.task.taskmanager.entity.TaskStatus;
import com.task.taskmanager.entity.User;
import com.task.taskmanager.exception.ResourceNotFoundException;
import com.task.taskmanager.repository.TaskRepository;
import com.task.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Authenticated user not found"));
    }

    public TaskResponse createTask(TaskRequest dto){

        User user = getAuthenticatedUser();

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.PENDING)
                .user(user)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    public Page<TaskResponse> getAllTasks(Pageable pageable){

        User user = getAuthenticatedUser();

        return taskRepository
                .findByUserId(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    public TaskResponse getTaskById(Long id) {

        User user = getAuthenticatedUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id " + id));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to access this task");
        }

        return mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        User user = getAuthenticatedUser();

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to update this task");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        if(dto.getStatus() != null){
            task.setStatus(dto.getStatus());
        }

        return mapToResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    private TaskResponse mapToResponse(Task task){

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt().toString()
        );
    }

    public Page<TaskResponse> getAllTasksForAdmin(Pageable pageable) {

        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(this::mapToResponse);
    }
}