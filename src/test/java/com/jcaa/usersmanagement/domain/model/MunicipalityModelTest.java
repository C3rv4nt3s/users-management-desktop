package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityModel.
 *
 * <p>Sólo se testean los métodos con lógica real: {@code create()} que conserva los campos
 * recibidos y {@code provinceCode()} que deriva el código de provincia desde el id. El constructor
 * trivial y los getters quedan cubiertos como efecto secundario de estos tests.
 */
@DisplayName("MunicipalityModel")
class MunicipalityModelTest {

  private MunicipalityId municipalityId;
  private MunicipalityName municipalityName;
  private ProvinceName provinceName;

  @BeforeEach
  void setUp() {
    municipalityId = new MunicipalityId("13034");
    municipalityName = new MunicipalityName("Ciudad Real");
    provinceName = new ProvinceName("Ciudad Real");
  }

  // ── create()

  @Test
  @DisplayName("create() debe preservar todos los campos recibidos")
  void shouldCreateMunicipalityAndPreserveAllFields() {
    // Act
    final MunicipalityModel model =
        MunicipalityModel.create(municipalityId, municipalityName, provinceName);

    // Assert
    assertAll(
        "create() factory",
        () -> assertSame(municipalityId, model.getId(), "id debe preservarse"),
        () -> assertSame(municipalityName, model.getName(), "name debe preservarse"),
        () -> assertSame(provinceName, model.getProvinceName(), "provinceName debe preservarse"));
  }

  // ── provinceCode()

  @Test
  @DisplayName("provinceCode() debe retornar los dos primeros dígitos del id del municipio")
  void shouldDeriveProvinceCodeFromId() {
    // Arrange
    final MunicipalityModel model =
        MunicipalityModel.create(municipalityId, municipalityName, provinceName);

    // Act
    final String provinceCode = model.provinceCode();

    // Assert
    assertEquals("13", provinceCode);
  }

  // ── igualdad por valor (@Value)

  @Test
  @DisplayName("Dos modelos con los mismos valores deben ser iguales")
  void shouldBeEqualWhenAllFieldsAreEqual() {
    // Arrange
    final MunicipalityModel first =
        MunicipalityModel.create(municipalityId, municipalityName, provinceName);
    final MunicipalityModel second =
        MunicipalityModel.create(
            new MunicipalityId("13034"),
            new MunicipalityName("Ciudad Real"),
            new ProvinceName("Ciudad Real"));

    // Assert
    assertEquals(first, second);
  }
}
