package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;

public interface SaveMunicipalityPort {
  MunicipalityModel save(MunicipalityModel municipality);
}
