package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateMunicipalityPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.mapper.MunicipalityApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class UpdateMunicipalityService implements UpdateMunicipalityUseCase {

  private final UpdateMunicipalityPort updateMunicipalityPort;
  private final GetMunicipalityByIdPort getMunicipalityByIdPort;
  private final Validator validator;

  @Override
  public MunicipalityModel execute(final UpdateMunicipalityCommand command) {
    validateCommand(command);

    final MunicipalityId municipalityId = new MunicipalityId(command.id());
    ensureMunicipalityExists(municipalityId);

    final MunicipalityModel municipalityToUpdate =
        MunicipalityApplicationMapper.fromUpdateCommandToModel(command);
    return updateMunicipalityPort.update(municipalityToUpdate);
  }

  private void validateCommand(final UpdateMunicipalityCommand command) {
    final Set<ConstraintViolation<UpdateMunicipalityCommand>> violations = validator.validate(command);
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
