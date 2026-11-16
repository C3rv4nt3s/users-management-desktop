package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateMunicipalityCommand(
    @NotBlank(message = "id must not be blank")
        @Pattern(regexp = "\\d{5}", message = "id must be a 5-digit code")
        String id,
    @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name must have at least 3 characters")
        String name,
    @NotBlank(message = "provinceName must not be blank")
        @Size(min = 3, message = "provinceName must have at least 3 characters")
        String provinceName)
{

}
