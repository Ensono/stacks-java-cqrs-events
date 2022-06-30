package com.amido.stacks.workloads.menu.api.v1;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@ApiResponse(
        responseCode = "200",
        description = "Search results matching criteria",
        content =
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SearchMenuResult.class)))
@ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content =
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)))
@SecurityRequirement(name = "bearerAuth")
public @interface SearchAPIResponses {}
