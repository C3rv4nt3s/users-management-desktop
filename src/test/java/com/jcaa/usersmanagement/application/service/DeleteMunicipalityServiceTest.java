package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteMunicipalityPort;
import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
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
 * Tests para DeleteMunicipalityService.
 *
 * <p>Cubre: flujo feliz (delete invocado), municipio no encontrado y validación del command.
 */
@DisplayName("DeleteMunicipalityService")
@ExtendWith(MockitoExtension.class)
class DeleteMunicipalityServiceTest {

  @Mock private DeleteMunicipalityPort deleteMunicipalityPort;
  @Mock private GetMunicipalityByIdPort getMunicipalityByIdPort;

  private DeleteMunicipalityService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new DeleteMunicipalityService(
              deleteMunicipalityPort, getMunicipalityByIdPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() invoca deleteMunicipalityPort cuando el municipio existe")
  void shouldDeleteWhenMunicipalityExists() {
    // Arrange
    final DeleteMunicipalityCommand command = new DeleteMunicipalityCommand("13034");

    final MunicipalityModel existing =
        MunicipalityModel.create(
            new MunicipalityId("13034"),
            new MunicipalityName("Ciudad Real"),
            new ProvinceName("Ciudad Real"));

    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.of(existing));

    // Act
    service.execute(command);

    // Assert
    verify(deleteMunicipalityPort).delete(new MunicipalityId("13034"));
  }

  // ── municipio no encontrado

  @Test
  @DisplayName("execute() lanza MunicipalityNotFoundException cuando el id no existe")
  void shouldThrowWhenMunicipalityNotFound() {
    // Arrange
    final DeleteMunicipalityCommand command = new DeleteMunicipalityCommand("99999");

    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(MunicipalityNotFoundException.class, () -> service.execute(command));
    verify(deleteMunicipalityPort, never()).delete(any());
  }

  // ── validación del command

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final DeleteMunicipalityCommand command = new DeleteMunicipalityCommand("  ");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(deleteMunicipalityPort, getMunicipalityByIdPort);
  }
}
