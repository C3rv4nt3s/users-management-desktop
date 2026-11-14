package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateMunicipalityRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for MunicipalityDesktopMapper: request → command/query and model → response. */
@DisplayName("MunicipalityDesktopMapper")
class MunicipalityDesktopMapperTest {

  private static MunicipalityModel buildMunicipality(final String id, final String name) {
    return MunicipalityModel.create(
        new MunicipalityId(id), new MunicipalityName(name), new ProvinceName("Ciudad Real"));
  }

  @Test
  @DisplayName("toCreateCommand() copies every field of the request")
  void toCreateCommand_copiesAllFields() {
    // Arrange
    final CreateMunicipalityRequest request =
        new CreateMunicipalityRequest("13034", "Ciudad Real", "Ciudad Real");

    // Act
    final CreateMunicipalityCommand command = MunicipalityDesktopMapper.toCreateCommand(request);

    // Assert
    assertAll(
        "create command mapping",
        () -> assertEquals("13034", command.id()),
        () -> assertEquals("Ciudad Real", command.name()),
        () -> assertEquals("Ciudad Real", command.provinceName()));
  }

  @Test
  @DisplayName("toUpdateCommand() copies every field of the request")
  void toUpdateCommand_copiesAllFields() {
    // Arrange
    final UpdateMunicipalityRequest request =
        new UpdateMunicipalityRequest("13034", "Ciudad Real Capital", "Ciudad Real");

    // Act
    final UpdateMunicipalityCommand command = MunicipalityDesktopMapper.toUpdateCommand(request);

    // Assert
    assertAll(
        "update command mapping",
        () -> assertEquals("13034", command.id()),
        () -> assertEquals("Ciudad Real Capital", command.name()),
        () -> assertEquals("Ciudad Real", command.provinceName()));
  }

  @Test
  @DisplayName("toDeleteCommand() and toGetByIdQuery() wrap the id")
  void toDeleteCommandAndToGetByIdQuery_wrapTheId() {
    // Act
    final DeleteMunicipalityCommand command = MunicipalityDesktopMapper.toDeleteCommand("13034");
    final GetMunicipalityByIdQuery query = MunicipalityDesktopMapper.toGetByIdQuery("13034");

    // Assert
    assertAll(
        "id wrapping",
        () -> assertEquals("13034", command.id()),
        () -> assertEquals("13034", query.id()));
  }

  @Test
  @DisplayName("toResponse() maps the model including the derived province code")
  void toResponse_mapsModelAndProvinceCode() {
    // Act
    final MunicipalityResponse response =
        MunicipalityDesktopMapper.toResponse(buildMunicipality("13034", "Ciudad Real"));

    // Assert
    assertAll(
        "response mapping",
        () -> assertEquals("13034", response.id()),
        () -> assertEquals("Ciudad Real", response.name()),
        () -> assertEquals("Ciudad Real", response.provinceName()),
        () -> assertEquals("13", response.provinceCode()));
  }

  @Test
  @DisplayName("toResponseList() keeps the order and size of the list")
  void toResponseList_keepsOrderAndSize() {
    // Arrange
    final List<MunicipalityModel> municipalities =
        List.of(buildMunicipality("13001", "Alcázar de San Juan"), buildMunicipality("13034", "Ciudad Real"));

    // Act
    final List<MunicipalityResponse> responses =
        MunicipalityDesktopMapper.toResponseList(municipalities);

    // Assert
    assertAll(
        "list mapping",
        () -> assertEquals(2, responses.size()),
        () -> assertEquals("13001", responses.get(0).id()),
        () -> assertEquals("13034", responses.get(1).id()));
  }
}
