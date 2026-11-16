package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;

public interface UpdateMunicipalityPort {
  MunicipalityModel update(MunicipalityModel municipality);
}
