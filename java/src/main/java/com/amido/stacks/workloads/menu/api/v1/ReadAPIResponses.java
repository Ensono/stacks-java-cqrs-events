package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ApiResponse(
    responseCode = "200",
    description = "Menu",
    content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = MenuDTO.class)))
@ApiResponse(
    responseCode = "404",
    description = "Menu Not Found",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
@SecurityRequirement(name = "bearerAuth")
public @interface ReadAPIResponses {}
