package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityRepositoryInMemory.
 *
 * <p>El adaptador de memoria no requiere infraestructura, por lo que se prueba directamente sin
 * mocks: se verifica cada operación de los puertos de salida que implementa.
 */
@DisplayName("MunicipalityRepositoryInMemory")
class MunicipalityRepositoryInMemoryTest {

  private MunicipalityRepositoryInMemory repository;

  @BeforeEach
  void setUp() {
    repository = new MunicipalityRepositoryInMemory();
  }

  private static MunicipalityModel buildMunicipality(final String id, final String name) {
    return MunicipalityModel.create(
        new MunicipalityId(id), new MunicipalityName(name), new ProvinceName("Ciudad Real"));
  }

  @Test
  @DisplayName("save() almacena el municipio y getById() lo recupera")
  void shouldSaveAndRetrieveMunicipality() {
    // Arrange
    final MunicipalityModel municipality = buildMunicipality("13034", "Ciudad Real");

    // Act
    final MunicipalityModel saved = repository.save(municipality);
    final Optional<MunicipalityModel> found = repository.getById(new MunicipalityId("13034"));

    // Assert
    assertAll(
        "save + getById",
        () -> assertSame(municipality, saved, "save() debe retornar el mismo municipio"),
        () -> assertTrue(found.isPresent(), "el municipio debe existir"),
        () -> assertEquals(municipality, found.orElseThrow()));
  }

  @Test
  @DisplayName("getById() retorna vacío cuando el municipio no existe")
  void shouldReturnEmptyWhenMunicipalityDoesNotExist() {
    // Act
    final Optional<MunicipalityModel> found = repository.getById(new MunicipalityId("99999"));

    // Assert
    assertTrue(found.isEmpty());
  }
}
