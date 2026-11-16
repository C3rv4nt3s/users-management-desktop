package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetMunicipalityByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
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
public final class GetMunicipalityByIdService implements GetMunicipalityByIdUseCase {

  private final GetMunicipalityByIdPort getMunicipalityByIdPort;
  private final Validator validator;

  @Override
  public MunicipalityModel execute(final GetMunicipalityByIdQuery query) {
    validateQuery(query);

    final MunicipalityId municipalityId =
        MunicipalityApplicationMapper.fromGetMunicipalityByIdQueryToMunicipalityId(query);
    return getMunicipalityByIdPort
        .getById(municipalityId)
        .orElseThrow(
            () -> MunicipalityNotFoundException.becauseIdWasNotFound(municipalityId.value()));
  }

  private void validateQuery(final GetMunicipalityByIdQuery query) {
    final Set<ConstraintViolation<GetMunicipalityByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
