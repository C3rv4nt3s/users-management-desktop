package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteMunicipalityPort;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.mapper.MunicipalityApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class DeleteMunicipalityService implements DeleteMunicipalityUseCase {

  private final DeleteMunicipalityPort deleteMunicipalityPort;
  private final GetMunicipalityByIdPort getMunicipalityByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteMunicipalityCommand command) {
    validateCommand(command);

    final MunicipalityId municipalityId =
        MunicipalityApplicationMapper.fromDeleteCommandToMunicipalityId(command);
    ensureMunicipalityExists(municipalityId);
    deleteMunicipalityPort.delete(municipalityId);
  }

  private void validateCommand(final DeleteMunicipalityCommand command) {
    final Set<ConstraintViolation<DeleteMunicipalityCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureMunicipalityExists(final MunicipalityId municipalityId) {
    getMunicipalityByIdPort
        .getById(municipalityId)
        .orElseThrow(
            () -> MunicipalityNotFoundException.becauseIdWasNotFound(municipalityId.value()));
  }
}
