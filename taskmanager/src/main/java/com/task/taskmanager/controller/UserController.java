package com.task.taskmanager.controller;

import com.task.taskmanager.dto.LoginRequest;
import com.task.taskmanager.dto.UserRequest;
import com.task.taskmanager.entity.User;
import com.task.taskmanager.dto.ServerResponse;
import com.task.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Authentication", description = "User registration and login APIs")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Register user", description = "Registers a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<ServerResponse<?>> createUser(@Valid @RequestBody UserRequest dto){

        userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ServerResponse<>(true,"User registered successfully",null));
    }


    @Operation(summary = "Get all users", description = "Fetch all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users fetched successfully")
    })
    @GetMapping
    public ResponseEntity<ServerResponse<List<User>>> getUsers(){

        List<User> users = userService.getAllUsers();

        ServerResponse<List<User>> response =
                new ServerResponse<>(true,"Users fetched successfully",users);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ServerResponse<String>> login(@RequestBody LoginRequest dto){

        String token = userService.login(dto);

        return ResponseEntity.ok(
                new ServerResponse<>(true,"Login successful",token)
        );
    }
}