package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetMunicipalityByIdUseCase {
  MunicipalityModel execute(@NotNull @Valid GetMunicipalityByIdQuery query);
}
