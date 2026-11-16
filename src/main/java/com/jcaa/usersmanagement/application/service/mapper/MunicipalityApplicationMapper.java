package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MunicipalityApplicationMapper {

  public MunicipalityModel fromCreateCommandToModel(final CreateMunicipalityCommand command) {
    return MunicipalityModel.create(
        new MunicipalityId(command.id()),
        new MunicipalityName(command.name()),
        new ProvinceName(command.provinceName()));
  }

  public MunicipalityId fromGetMunicipalityByIdQueryToMunicipalityId(
      final GetMunicipalityByIdQuery query) {
    return new MunicipalityId(query.id());
  }
}
