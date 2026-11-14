package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveMunicipalityPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.mapper.MunicipalityApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.MunicipalityAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class CreateMunicipalityService implements CreateMunicipalityUseCase {

  private final SaveMunicipalityPort saveMunicipalityPort;
  private final GetMunicipalityByIdPort getMunicipalityByIdPort;
  private final Validator validator;

  @Override
  public MunicipalityModel execute(final CreateMunicipalityCommand command) {
    validateCommand(command);

    final MunicipalityId municipalityId = new MunicipalityId(command.id());
    ensureIdIsNotTaken(municipalityId);

    final MunicipalityModel municipalityToSave =
        MunicipalityApplicationMapper.fromCreateCommandToModel(command);
    return saveMunicipalityPort.save(municipalityToSave);
  }

  private void validateCommand(final CreateMunicipalityCommand command) {
    final Set<ConstraintViolation<CreateMunicipalityCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureIdIsNotTaken(final MunicipalityId municipalityId) {
    getMunicipalityByIdPort
        .getById(municipalityId)
        .ifPresent(
            ignored -> {
              throw MunicipalityAlreadyExistsException.becauseIdAlreadyExists(
                  municipalityId.value());
            });
  }
}
