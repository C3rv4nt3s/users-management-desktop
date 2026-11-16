package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetMunicipalityByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveMunicipalityPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.domain.exception.MunicipalityAlreadyExistsException;
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
 * Tests para CreateMunicipalityService.
 *
 * <p>Cubre: flujo feliz (se guarda el municipio mapeado), id duplicado y validación del command.
 */
@DisplayName("CreateMunicipalityService")
@ExtendWith(MockitoExtension.class)
class CreateMunicipalityServiceTest {

  @Mock private SaveMunicipalityPort saveMunicipalityPort;
  @Mock private GetMunicipalityByIdPort getMunicipalityByIdPort;

  private CreateMunicipalityService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new CreateMunicipalityService(
              saveMunicipalityPort, getMunicipalityByIdPort, validatorFactory.getValidator());
    }
  }

  private static MunicipalityModel buildMunicipality(final String id, final String name) {
    return MunicipalityModel.create(
        new MunicipalityId(id), new MunicipalityName(name), new ProvinceName("Ciudad Real"));
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() guarda el municipio cuando el id no existe")
  void shouldSaveMunicipalityWhenIdIsFree() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("13034", "Ciudad Real", "Ciudad Real");
    final MunicipalityModel saved = buildMunicipality("13034", "Ciudad Real");

    when(getMunicipalityByIdPort.getById(any())).thenReturn(Optional.empty());
    when(saveMunicipalityPort.save(any())).thenReturn(saved);

    // Act
    final MunicipalityModel result = service.execute(command);

    // Assert
    final ArgumentCaptor<MunicipalityModel> captor = ArgumentCaptor.forClass(MunicipalityModel.class);
    verify(saveMunicipalityPort).save(captor.capture());
    assertAll(
        "create municipality",
        () -> assertSame(saved, result, "debe retornar lo que devuelve el puerto"),
        () -> assertEquals(new MunicipalityId("13034"), captor.getValue().getId()),
        () -> assertEquals(new MunicipalityName("Ciudad Real"), captor.getValue().getName()),
        () -> assertEquals(new ProvinceName("Ciudad Real"), captor.getValue().getProvinceName()));
  }

  // ── id duplicado

  @Test
  @DisplayName("execute() lanza MunicipalityAlreadyExistsException cuando el id ya existe")
  void shouldThrowWhenIdAlreadyExists() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("13034", "Ciudad Real", "Ciudad Real");

    when(getMunicipalityByIdPort.getById(any()))
        .thenReturn(Optional.of(buildMunicipality("13034", "Ciudad Real")));

    // Act & Assert
    assertThrows(MunicipalityAlreadyExistsException.class, () -> service.execute(command));
    verify(saveMunicipalityPort, never()).save(any());
  }

  // ── validación del command

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id no tiene 5 dígitos")
  void shouldThrowWhenIdFormatIsInvalid() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("123", "Ciudad Real", "Ciudad Real");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveMunicipalityPort, getMunicipalityByIdPort);
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el nombre es muy corto")
  void shouldThrowWhenNameIsTooShort() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("13034", "Ci", "Ciudad Real");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveMunicipalityPort, getMunicipalityByIdPort);
  }
}
