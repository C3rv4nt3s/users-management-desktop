package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllMunicipalitiesUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllMunicipalitiesPort;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class GetAllMunicipalitiesService implements GetAllMunicipalitiesUseCase {

  private final GetAllMunicipalitiesPort getAllMunicipalitiesPort;

  @Override
  public List<MunicipalityModel> execute() {
    return getAllMunicipalitiesPort.getAll();
  }
}
