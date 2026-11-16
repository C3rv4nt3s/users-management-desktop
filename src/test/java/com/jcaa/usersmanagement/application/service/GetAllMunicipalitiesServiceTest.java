package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllMunicipalitiesPort;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetAllMunicipalitiesService.
 *
 * <p>Cubre: lista con municipios y lista vacía.
 */
@DisplayName("GetAllMunicipalitiesService")
@ExtendWith(MockitoExtension.class)
class GetAllMunicipalitiesServiceTest {

  @Mock private GetAllMunicipalitiesPort getAllMunicipalitiesPort;

  private GetAllMunicipalitiesService service;

  @BeforeEach
  void setUp() {
    service = new GetAllMunicipalitiesService(getAllMunicipalitiesPort);
  }

  // ── lista con municipios

  @Test
  @DisplayName("execute() retorna la lista de municipios del puerto")
  void shouldReturnMunicipalitiesFromPort() {
    // Arrange
    final MunicipalityModel municipality =
        MunicipalityModel.create(
            new MunicipalityId("13034"),
            new MunicipalityName("Ciudad Real"),
            new ProvinceName("Ciudad Real"));

    when(getAllMunicipalitiesPort.getAll()).thenReturn(List.of(municipality));

    // Act
    final List<MunicipalityModel> result = service.execute();

    // Assert
    assertAll(
        "getAll con un municipio",
        () -> assertEquals(1, result.size(), "debe retornar exactamente un municipio"),
        () -> assertSame(municipality, result.get(0), "debe ser el mismo objeto del puerto"));
  }

  // ── lista vacía

  @Test
  @DisplayName("execute() retorna lista vacía cuando no hay municipios")
  void shouldReturnEmptyListWhenNoMunicipalities() {
    // Arrange
    when(getAllMunicipalitiesPort.getAll()).thenReturn(List.of());

    // Act
    final List<MunicipalityModel> result = service.execute();

    // Assert
    assertTrue(result.isEmpty(), "debe retornar lista vacía");
  }
}
