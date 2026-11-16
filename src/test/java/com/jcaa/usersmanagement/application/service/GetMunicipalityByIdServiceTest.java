package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetMunicipalityByIdService.
 *
 * <p>Cubre: municipio encontrado, municipio no encontrado y validación del query.
 */
@DisplayName("GetMunicipalityByIdService")
@ExtendWith(MockitoExtension.class)
class GetMunicipalityByIdServiceTest {

  @Mock private GetMunicipalityByIdPort getMunicipalityByIdPort;

  private GetMunicipalityByIdService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new GetMunicipalityByIdService(getMunicipalityByIdPort, validatorFactory.getValidator());
    }
  }

  // ── municipio encontrado

  @Test
  @DisplayName("execute() retorna el municipio cuando existe")
  void shouldReturnMunicipalityWhenExists() {
    // Arrange
    final MunicipalityModel existing =
        MunicipalityModel.create(
            new MunicipalityId("13034"),
            new MunicipalityName("Ciudad Real"),
            new ProvinceName("Ciudad Real"));
    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.of(existing));

    // Act
    final MunicipalityModel result = service.execute(new GetMunicipalityByIdQuery("13034"));

    // Assert
    assertSame(existing, result);
    verify(getMunicipalityByIdPort).getById(new MunicipalityId("13034"));
  }

  // ── municipio no encontrado

  @Test
  @DisplayName("execute() lanza MunicipalityNotFoundException cuando el id no existe")
  void shouldThrowWhenMunicipalityNotFound() {
    // Arrange
    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        MunicipalityNotFoundException.class,
        () -> service.execute(new GetMunicipalityByIdQuery("99999")));
  }

  // ── validación del query

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenQueryIsInvalid() {
    // Act & Assert
    assertThrows(
        ConstraintViolationException.class,
        () -> service.execute(new GetMunicipalityByIdQuery("  ")));
    verifyNoInteractions(getMunicipalityByIdPort);
  }
}
