package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityAlreadyExistsException.
 *
 * <p>Verifica que el mensaje incluya el id duplicado, dato que necesita la capa de entrada para
 * informar al usuario cuál código ya está registrado.
 */
@DisplayName("MunicipalityAlreadyExistsException")
class MunicipalityAlreadyExistsExceptionTest {

  @Test
  @DisplayName("becauseIdAlreadyExists() debe incluir el id duplicado en el mensaje de error")
  void shouldIncludeMunicipalityIdInMessage() {
    // Arrange
    final String municipalityId = "13034";

    // Act
    final String message =
        MunicipalityAlreadyExistsException.becauseIdAlreadyExists(municipalityId).getMessage();

    // Assert
    assertTrue(
        message.contains(municipalityId),
        "el mensaje debe identificar el id del municipio duplicado");
  }
}
