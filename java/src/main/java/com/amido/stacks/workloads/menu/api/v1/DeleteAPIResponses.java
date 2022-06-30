package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ApiResponse(
    responseCode = "200",
    description = "Success",
    content = @Content(mediaType = "application/json", schema = @Schema(hidden = true)))
@ApiResponse(
    responseCode = "204",
    description = "No Content",
    content = @Content(schema = @Schema(hidden = true)))
@ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(
    responseCode = "401",
    description = "Unauthorized, Access token is missing or invalid",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(
    responseCode = "403",
    description = "Forbidden, the user does not have permission to execute this operation",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(
    responseCode = "409",
    description = "Conflict, an item already exists",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@SecurityRequirement(name = "bearerAuth")
public @interface DeleteAPIResponses {}
