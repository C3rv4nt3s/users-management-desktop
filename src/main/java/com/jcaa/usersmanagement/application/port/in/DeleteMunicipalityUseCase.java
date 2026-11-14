package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DeleteMunicipalityUseCase {
  void execute(@NotNull @Valid DeleteMunicipalityCommand command);
}
