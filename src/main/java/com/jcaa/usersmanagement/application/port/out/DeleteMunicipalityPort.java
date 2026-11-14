package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;

public interface DeleteMunicipalityPort {
  void delete(MunicipalityId municipalityId);
}
