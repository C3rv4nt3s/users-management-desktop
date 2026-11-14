package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import java.util.Map;
import lombok.Getter;

@Getter
public final class MunicipalityDeletedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "municipality.deleted";

  private final MunicipalityId municipalityId;

  public MunicipalityDeletedDomainEvent(final MunicipalityId municipalityId) {
    super(EVENT_NAME);
    this.municipalityId = municipalityId;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of("id", municipalityId.value());
  }
}
