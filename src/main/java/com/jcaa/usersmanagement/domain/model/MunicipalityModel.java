package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import lombok.Value;

@Value
public class MunicipalityModel {

  MunicipalityId id;
  MunicipalityName name;
  ProvinceName provinceName;

  public static MunicipalityModel create(
      final MunicipalityId id, final MunicipalityName name, final ProvinceName provinceName) {
    return new MunicipalityModel(id, name, provinceName);
  }

  /** Province code derived from the municipality id (its first two digits). */
  public String provinceCode() {
    return id.provinceCode();
  }
}
