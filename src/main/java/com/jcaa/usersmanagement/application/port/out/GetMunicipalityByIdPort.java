package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import java.util.Optional;

public interface GetMunicipalityByIdPort {
  Optional<MunicipalityModel> getById(MunicipalityId municipalityId);
}
