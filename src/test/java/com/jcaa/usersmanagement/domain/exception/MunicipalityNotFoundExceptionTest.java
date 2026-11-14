package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityNotFoundException.
 *
 * <p>La excepción no tiene lógica propia; su valor está en el mensaje: el id del municipio no
 * encontrado debe aparecer explícitamente para que la capa superior pueda construir una respuesta
 * de error útil.
 */
@DisplayName("MunicipalityNotFoundException")
class MunicipalityNotFoundExceptionTest {

  @Test
  @DisplayName("becauseIdWasNotFound() debe incluir el id del municipio en el mensaje de error")
  void shouldIncludeMunicipalityIdInMessage() {
    // Arrange
    final String municipalityId = "99999";

    // Act
    final String message =
        MunicipalityNotFoundException.becauseIdWasNotFound(municipalityId).getMessage();

    // Assert
    assertTrue(
        message.contains(municipalityId),
        "el mensaje debe identificar el id del municipio no encontrado");
  }
}
