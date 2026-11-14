package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class MunicipalityUpdatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "municipality.updated";

  private final MunicipalityModel municipality;

  public MunicipalityUpdatedDomainEvent(final MunicipalityModel municipality) {
    super(EVENT_NAME);
    this.municipality = municipality;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of(
        "id", municipality.getId().value(),
        "name", municipality.getName().value(),
        "provinceName", municipality.getProvinceName().value(),
        "provinceCode", municipality.provinceCode());
  }
}
