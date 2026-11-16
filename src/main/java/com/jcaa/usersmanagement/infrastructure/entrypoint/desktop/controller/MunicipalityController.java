package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllMunicipalitiesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetMunicipalityByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateMunicipalityUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.MunicipalityDesktopMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class MunicipalityController {

  private final CreateMunicipalityUseCase createMunicipalityUseCase;
  private final UpdateMunicipalityUseCase updateMunicipalityUseCase;
  private final DeleteMunicipalityUseCase deleteMunicipalityUseCase;
  private final GetMunicipalityByIdUseCase getMunicipalityByIdUseCase;
  private final GetAllMunicipalitiesUseCase getAllMunicipalitiesUseCase;

  public List<MunicipalityResponse> listAllMunicipalities() {
    final var municipalities = getAllMunicipalitiesUseCase.execute();
    return MunicipalityDesktopMapper.toResponseList(municipalities);
  }

  public MunicipalityResponse findMunicipalityById(final String id) {
    final var query = MunicipalityDesktopMapper.toGetByIdQuery(id);
    final var municipality = getMunicipalityByIdUseCase.execute(query);
    return MunicipalityDesktopMapper.toResponse(municipality);
  }

  public MunicipalityResponse createMunicipality(final CreateMunicipalityRequest request) {
    final var command = MunicipalityDesktopMapper.toCreateCommand(request);
    final var municipality = createMunicipalityUseCase.execute(command);
    return MunicipalityDesktopMapper.toResponse(municipality);
  }

  public MunicipalityResponse updateMunicipality(final UpdateMunicipalityRequest request) {
    final var command = MunicipalityDesktopMapper.toUpdateCommand(request);
    final var municipality = updateMunicipalityUseCase.execute(command);
    return MunicipalityDesktopMapper.toResponse(municipality);
  }

  public void deleteMunicipality(final String id) {
    final var command = MunicipalityDesktopMapper.toDeleteCommand(id);
    deleteMunicipalityUseCase.execute(command);
  }
}
