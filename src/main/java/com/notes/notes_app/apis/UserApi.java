package com.notes.notes_app.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.notes.notes_app.exchange.userExchange.PostUserRequest;
import com.notes.notes_app.exchange.userExchange.UpdateUserRequest;
import com.notes.notes_app.model.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Users", description = "Users Endpoints")
public interface UserApi {

//     @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user")
//     @ApiResponses(value = {
//             @ApiResponse(responseCode = "201", description = "Successfully created a user", content = {
//                     @Content(schema = @Schema(implementation = UserEntity.class), mediaType = "application/json") }),
//             @ApiResponse(responseCode = "409", description = "Duplicate username")
//     })
//     public ResponseEntity<UserEntity> create(@RequestBody PostUserRequest postUserRequest);

    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all the users")
    })
    public ResponseEntity<List<UserEntity>> getAll();

    @Operation(summary = "Get the user by id", description = "Get the user by id if exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user by id"),
            @ApiResponse(responseCode = "404", description = "Operation failed because id does not exist"),
    })
    public ResponseEntity<UserEntity> getById(@PathVariable String id);

    @Operation(summary = "Update the user by id", description = "Update the user by id and return updated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user"),
            @ApiResponse(responseCode = "404", description = "Id not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<UserEntity> updateById(@PathVariable String id, @RequestBody UpdateUserRequest updateUserRequest);

    @Operation(summary = "Update the user by username", description = "Update the user by username and return updated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user"),
            @ApiResponse(responseCode = "404", description = "Id not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<UserEntity> updateByUsername(@PathVariable String oldUsername, @RequestBody UpdateUserRequest updateUserRequest);

    @Operation(summary = "Delete the user by id", description = "Delete the user by id and return status of deletion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the user"),
            @ApiResponse(responseCode = "404", description = "Id not found")
    })
    public ResponseEntity<?> deleteById(@PathVariable String id);

    @Operation(summary = "Delete all the users", description = "Delete all the users and return status of deletion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted all the users"),
    })
    public ResponseEntity<?> deleteAll();

}
