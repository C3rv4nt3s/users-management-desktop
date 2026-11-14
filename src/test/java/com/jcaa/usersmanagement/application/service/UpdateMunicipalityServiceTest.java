package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateMunicipalityPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateMunicipalityCommand;
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
import org.mockito.ArgumentCaptor;

/**
 * Tests para UpdateMunicipalityService.
 *
 * <p>Cubre: flujo feliz (se actualiza con los datos del command), municipio inexistente y
 * validación del command.
 */
@DisplayName("UpdateMunicipalityService")
@ExtendWith(MockitoExtension.class)
class UpdateMunicipalityServiceTest {

  @Mock private UpdateMunicipalityPort updateMunicipalityPort;
  @Mock private GetMunicipalityByIdPort getMunicipalityByIdPort;

  private UpdateMunicipalityService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateMunicipalityService(
              updateMunicipalityPort, getMunicipalityByIdPort, validatorFactory.getValidator());
    }
  }

  private static MunicipalityModel buildMunicipality(final String id, final String name) {
    return MunicipalityModel.create(
        new MunicipalityId(id), new MunicipalityName(name), new ProvinceName("Ciudad Real"));
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() actualiza el municipio cuando existe")
  void shouldUpdateMunicipalityWhenExists() {
    // Arrange
    final UpdateMunicipalityCommand command =
        new UpdateMunicipalityCommand("13034", "Ciudad Real Capital", "Ciudad Real");
    final MunicipalityModel updated = buildMunicipality("13034", "Ciudad Real Capital");

    when(getMunicipalityByIdPort.getById(any()))
        .thenReturn(Optional.of(buildMunicipality("13034", "Ciudad Real")));
    when(updateMunicipalityPort.update(any())).thenReturn(updated);

    // Act
    final MunicipalityModel result = service.execute(command);

    // Assert
    final ArgumentCaptor<MunicipalityModel> captor = ArgumentCaptor.forClass(MunicipalityModel.class);
    verify(updateMunicipalityPort).update(captor.capture());
    assertAll(
        "update municipality",
        () -> assertSame(updated, result, "debe retornar lo que devuelve el puerto"),
        () -> assertEquals(new MunicipalityId("13034"), captor.getValue().getId()),
        () -> assertEquals(new MunicipalityName("Ciudad Real Capital"), captor.getValue().getName()),
        () -> assertEquals(new ProvinceName("Ciudad Real"), captor.getValue().getProvinceName()));
  }

  // ── municipio no encontrado

  @Test
  @DisplayName("execute() lanza MunicipalityNotFoundException cuando el id no existe")
  void shouldThrowWhenMunicipalityNotFound() {
    // Arrange
    final UpdateMunicipalityCommand command =
        new UpdateMunicipalityCommand("99999", "No Existe", "Ciudad Real");

    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(MunicipalityNotFoundException.class, () -> service.execute(command));
    verify(updateMunicipalityPort, never()).update(any());
  }

  // ── validación del command

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el nombre es muy corto")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final UpdateMunicipalityCommand command =
        new UpdateMunicipalityCommand("13034", "Ci", "Ciudad Real");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(updateMunicipalityPort, getMunicipalityByIdPort);
  }
}
