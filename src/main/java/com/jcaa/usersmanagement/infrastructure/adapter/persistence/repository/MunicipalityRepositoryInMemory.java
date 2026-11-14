package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.GetAllMunicipalitiesPort;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveMunicipalityPort;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de memoria para la entidad Municipality: implementa los puertos de salida guardando los
 * municipios en un mapa en memoria. No requiere base de datos; los datos se pierden al cerrar la
 * aplicación.
 */
public final class MunicipalityRepositoryInMemory
    implements SaveMunicipalityPort,
        GetMunicipalityByIdPort,
        GetAllMunicipalitiesPort {

  private final Map<MunicipalityId, MunicipalityModel> storage = new ConcurrentHashMap<>();

  @Override
  public MunicipalityModel save(final MunicipalityModel municipality) {
    storage.put(municipality.getId(), municipality);
    return municipality;
  }

  @Override
  public Optional<MunicipalityModel> getById(final MunicipalityId municipalityId) {
    return Optional.ofNullable(storage.get(municipalityId));
  }

  @Override
  public List<MunicipalityModel> getAll() {
    return storage.values().stream()
        .sorted(Comparator.comparing(municipality -> municipality.getName().value()))
        .toList();
  }
}
