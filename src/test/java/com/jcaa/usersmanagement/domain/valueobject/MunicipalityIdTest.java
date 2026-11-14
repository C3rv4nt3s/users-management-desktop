package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidMunicipalityIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MunicipalityIdTest {

  // --- Happy path ---

  @ParameterizedTest
  @ValueSource(strings = {"13034", " 13034 ", "  13034  ", "13034\t"})
  @DisplayName("Should create MunicipalityId with trimmed value")
  void shouldCreateMunicipalityIdWithTrimmedValue(final String input) {
    // Arrange
    final String correctId = "13034";
    // Act
    final MunicipalityId municipalityId = new MunicipalityId(input);
    // Assert
    assertEquals(correctId, municipalityId.toString());
  }

  @Test
  @DisplayName("provinceCode() debe retornar los dos primeros dígitos del id")
  void shouldReturnProvinceCodeFromFirstTwoDigits() {
    // Arrange
    final MunicipalityId municipalityId = new MunicipalityId("13034");
    // Act
    final String provinceCode = municipalityId.provinceCode();
    // Assert
    assertEquals("13", provinceCode);
  }

  @Test
  @DisplayName("Dos MunicipalityId con el mismo valor son iguales")
  void shouldBeEqualWhenValueIsTheSame() {
    assertEquals(new MunicipalityId("13034"), new MunicipalityId(" 13034 "));
  }

  // --- Flujo con excepciones y ramas condicionales ---

  @Test
  @DisplayName("Should throw NullPointerException when MunicipalityId is null")
  void shouldThrowNullPointerExceptionWhenIdIsNull() {
    assertThrows(NullPointerException.class, () -> new MunicipalityId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f"})
  @DisplayName("Should throw InvalidMunicipalityIdException when the id is empty")
  void shouldThrowWhenIdIsEmpty(final String input) {
    assertThrows(InvalidMunicipalityIdException.class, () -> new MunicipalityId(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1303", "130345", "1303A", "abcde", "13 34", "13-34", "-1303"})
  @DisplayName("Should throw InvalidMunicipalityIdException when the id is not a 5-digit code")
  void shouldThrowWhenFormatIsInvalid(final String input) {
    assertThrows(InvalidMunicipalityIdException.class, () -> new MunicipalityId(input));
  }
}
