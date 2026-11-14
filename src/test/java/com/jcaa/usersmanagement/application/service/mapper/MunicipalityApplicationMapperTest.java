package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.domain.exception.InvalidMunicipalityIdException;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para MunicipalityApplicationMapper.
 *
 * <p>Verifica la conversión de commands/queries a objetos de dominio (Value Objects y modelo).
 */
@DisplayName("MunicipalityApplicationMapper")
class MunicipalityApplicationMapperTest {

  @Test
  @DisplayName("fromCreateCommandToModel() debe convertir el command a un modelo de dominio")
  void shouldMapCreateCommandToModel() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("13034", "Ciudad Real", "Ciudad Real");

    // Act
    final MunicipalityModel model = MunicipalityApplicationMapper.fromCreateCommandToModel(command);

    // Assert
    assertAll(
        "fromCreateCommandToModel",
        () -> assertEquals(new MunicipalityId("13034"), model.getId()),
        () -> assertEquals(new MunicipalityName("Ciudad Real"), model.getName()),
        () -> assertEquals(new ProvinceName("Ciudad Real"), model.getProvinceName()));
  }

  @Test
  @DisplayName("fromCreateCommandToModel() debe lanzar excepción de dominio si el id es inválido")
  void shouldThrowWhenCreateCommandHasInvalidId() {
    // Arrange
    final CreateMunicipalityCommand command =
        new CreateMunicipalityCommand("abc", "Ciudad Real", "Ciudad Real");

    // Act & Assert
    assertThrows(
        InvalidMunicipalityIdException.class,
        () -> MunicipalityApplicationMapper.fromCreateCommandToModel(command));
  }
}
