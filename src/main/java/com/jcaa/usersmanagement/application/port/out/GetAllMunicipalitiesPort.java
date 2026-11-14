package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import java.util.List;

public interface GetAllMunicipalitiesPort {
  List<MunicipalityModel> getAll();
}
