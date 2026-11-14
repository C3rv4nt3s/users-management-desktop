package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidMunicipalityNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MunicipalityNameTest {

  // --- Happy Path Tests ---

  @ParameterizedTest
  @ValueSource(strings = {"Ciudad Real", "   Ciudad Real   ", "Ciudad Real \t"})
  @DisplayName("Valida que el valor se normalice (trim) y tenga al menos 3 caracteres")
  void shouldNormalizeValue(final String input) {
    // Arrange
    final String correctValue = "Ciudad Real";
    // Act
    final MunicipalityName valueObject = new MunicipalityName(input);
    // Assert
    assertEquals(correctValue, valueObject.toString());
  }

  // -- Flujo con excepciones y ramas de validación ---

  @Test
  @DisplayName("Valida que el valor no sea nulo")
  void shouldValidateValueIsNotNull() {
    assertThrows(NullPointerException.class, () -> new MunicipalityName(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "\t", "\n", "\r", "\f"})
  @DisplayName("Valida que el valor no sea vacío")
  void shouldValidateValueIsNotEmpty(final String input) {
    assertThrows(InvalidMunicipalityNameException.class, () -> new MunicipalityName(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Jo", "Ty  ", "   Cy ", "Ed\t", "a"})
  @DisplayName("Valida que el valor tenga un tamaño mínimo de 3 caracteres")
  void shouldValidateMinimumLength(final String input) {
    assertThrows(InvalidMunicipalityNameException.class, () -> new MunicipalityName(input));
  }
}
