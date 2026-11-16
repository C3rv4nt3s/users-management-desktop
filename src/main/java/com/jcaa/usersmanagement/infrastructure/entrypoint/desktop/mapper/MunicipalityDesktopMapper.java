package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateMunicipalityRequest;

import java.util.List;

public final class MunicipalityDesktopMapper {

  private MunicipalityDesktopMapper() {}

  public static CreateMunicipalityCommand toCreateCommand(final CreateMunicipalityRequest request) {
    return new CreateMunicipalityCommand(request.id(), request.name(), request.provinceName());
  }

  public static UpdateMunicipalityCommand toUpdateCommand(final UpdateMunicipalityRequest request) {
    return new UpdateMunicipalityCommand(request.id(), request.name(), request.provinceName());
  }

  public static DeleteMunicipalityCommand toDeleteCommand(final String id) {
    return new DeleteMunicipalityCommand(id);
  }

  public static GetMunicipalityByIdQuery toGetByIdQuery(final String id) {
    return new GetMunicipalityByIdQuery(id);
  }

  public static MunicipalityResponse toResponse(final MunicipalityModel municipality) {
    return new MunicipalityResponse(
        municipality.getId().value(),
        municipality.getName().value(),
        municipality.getProvinceName().value(),
        municipality.provinceCode());
  }

  public static List<MunicipalityResponse> toResponseList(
      final List<MunicipalityModel> municipalities) {
    return municipalities.stream().map(MunicipalityDesktopMapper::toResponse).toList();
  }
}
