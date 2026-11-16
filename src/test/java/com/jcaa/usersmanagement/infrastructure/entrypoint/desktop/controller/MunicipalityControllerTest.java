package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.in.CreateMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteMunicipalityUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllMunicipalitiesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetMunicipalityByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateMunicipalityUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateMunicipalityCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetMunicipalityByIdQuery;
import com.jcaa.usersmanagement.domain.exception.MunicipalityAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.domain.model.MunicipalityModel;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityId;
import com.jcaa.usersmanagement.domain.valueobject.MunicipalityName;
import com.jcaa.usersmanagement.domain.valueobject.ProvinceName;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateMunicipalityRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for MunicipalityController.
 *
 * <p>Covers: correct delegation to every use-case port, accurate DTO→command/query mapping,
 * accurate domain-model→response mapping, and transparent exception propagation. All ports are
 * mocked; no infrastructure is exercised.
 */
@DisplayName("MunicipalityController")
@ExtendWith(MockitoExtension.class)
class MunicipalityControllerTest {

  @Mock private CreateMunicipalityUseCase createMunicipalityUseCase;
  @Mock private UpdateMunicipalityUseCase updateMunicipalityUseCase;
  @Mock private DeleteMunicipalityUseCase deleteMunicipalityUseCase;
  @Mock private GetMunicipalityByIdUseCase getMunicipalityByIdUseCase;
  @Mock private GetAllMunicipalitiesUseCase getAllMunicipalitiesUseCase;

  private MunicipalityController controller;

  // ── helpers

  private static MunicipalityModel buildMunicipality(final String id, final String name) {
    return MunicipalityModel.create(
        new MunicipalityId(id), new MunicipalityName(name), new ProvinceName("Ciudad Real"));
  }

  @BeforeEach
  void setUp() {
    controller =
        new MunicipalityController(
            createMunicipalityUseCase,
            updateMunicipalityUseCase,
            deleteMunicipalityUseCase,
            getMunicipalityByIdUseCase,
            getAllMunicipalitiesUseCase);
  }

  // ── listAllMunicipalities

  @Test
  @DisplayName("listAllMunicipalities() returns a mapped MunicipalityResponse list")
  void listAllMunicipalities_returnsMappedResponseList_whenMunicipalitiesExist() {
    // Arrange
    when(getAllMunicipalitiesUseCase.execute())
        .thenReturn(List.of(buildMunicipality("13034", "Ciudad Real")));

    // Act
    final List<MunicipalityResponse> result = controller.listAllMunicipalities();

    // Assert
    assertAll(
        "single-municipality list mapping",
        () -> assertEquals(1, result.size(), "list must contain exactly one element"),
        () -> assertEquals("13034", result.get(0).id(), "id must match"),
        () -> assertEquals("Ciudad Real", result.get(0).name(), "name must match"),
        () -> assertEquals("13", result.get(0).provinceCode(), "province code must match"));
  }

  @Test
  @DisplayName("listAllMunicipalities() returns an empty list when there are no municipalities")
  void listAllMunicipalities_returnsEmptyList_whenNoMunicipalities() {
    // Arrange
    when(getAllMunicipalitiesUseCase.execute()).thenReturn(List.of());

    // Act
    final List<MunicipalityResponse> result = controller.listAllMunicipalities();

    // Assert
    assertTrue(result.isEmpty(), "result must be empty");
  }

  // ── findMunicipalityById

  @Test
  @DisplayName("findMunicipalityById() builds the query and returns the mapped response")
  void findMunicipalityById_buildsQueryAndReturnsResponse() {
    // Arrange
    when(getMunicipalityByIdUseCase.execute(any()))
        .thenReturn(buildMunicipality("13034", "Ciudad Real"));

    // Act
    final MunicipalityResponse result = controller.findMunicipalityById("13034");

    // Assert
    final ArgumentCaptor<GetMunicipalityByIdQuery> captor =
        ArgumentCaptor.forClass(GetMunicipalityByIdQuery.class);
    verify(getMunicipalityByIdUseCase).execute(captor.capture());
    assertAll(
        "find by id",
        () -> assertEquals("13034", captor.getValue().id(), "query id must match"),
        () -> assertEquals("Ciudad Real", result.name(), "response name must match"));
  }

  @Test
  @DisplayName("findMunicipalityById() propagates MunicipalityNotFoundException")
  void findMunicipalityById_propagatesNotFound() {
    // Arrange
    when(getMunicipalityByIdUseCase.execute(any()))
        .thenThrow(MunicipalityNotFoundException.becauseIdWasNotFound("99999"));

    // Act & Assert
    assertThrows(MunicipalityNotFoundException.class, () -> controller.findMunicipalityById("99999"));
  }

  // ── createMunicipality

  @Test
  @DisplayName("createMunicipality() maps the request to a command and returns the response")
  void createMunicipality_mapsRequestAndReturnsResponse() {
    // Arrange
    when(createMunicipalityUseCase.execute(any()))
        .thenReturn(buildMunicipality("13034", "Ciudad Real"));

    // Act
    final MunicipalityResponse result =
        controller.createMunicipality(
            new CreateMunicipalityRequest("13034", "Ciudad Real", "Ciudad Real"));

    // Assert
    final ArgumentCaptor<CreateMunicipalityCommand> captor =
        ArgumentCaptor.forClass(CreateMunicipalityCommand.class);
    verify(createMunicipalityUseCase).execute(captor.capture());
    assertAll(
        "create mapping",
        () -> assertEquals("13034", captor.getValue().id(), "command id must match"),
        () -> assertEquals("Ciudad Real", captor.getValue().name(), "command name must match"),
        () -> assertEquals("13034", result.id(), "response id must match"));
  }

  @Test
  @DisplayName("createMunicipality() propagates MunicipalityAlreadyExistsException")
  void createMunicipality_propagatesAlreadyExists() {
    // Arrange
    when(createMunicipalityUseCase.execute(any()))
        .thenThrow(MunicipalityAlreadyExistsException.becauseIdAlreadyExists("13034"));

    // Act & Assert
    assertThrows(
        MunicipalityAlreadyExistsException.class,
        () ->
            controller.createMunicipality(
                new CreateMunicipalityRequest("13034", "Ciudad Real", "Ciudad Real")));
  }

  // ── updateMunicipality

  @Test
  @DisplayName("updateMunicipality() maps the request to a command and returns the response")
  void updateMunicipality_mapsRequestAndReturnsResponse() {
    // Arrange
    when(updateMunicipalityUseCase.execute(any()))
        .thenReturn(buildMunicipality("13034", "Ciudad Real Capital"));

    // Act
    final MunicipalityResponse result =
        controller.updateMunicipality(
            new UpdateMunicipalityRequest("13034", "Ciudad Real Capital", "Ciudad Real"));

    // Assert
    final ArgumentCaptor<UpdateMunicipalityCommand> captor =
        ArgumentCaptor.forClass(UpdateMunicipalityCommand.class);
    verify(updateMunicipalityUseCase).execute(captor.capture());
    assertAll(
        "update mapping",
        () -> assertEquals("Ciudad Real Capital", captor.getValue().name(), "command name"),
        () -> assertEquals("Ciudad Real Capital", result.name(), "response name"));
  }

  // ── deleteMunicipality

  @Test
  @DisplayName("deleteMunicipality() builds the command and delegates to the use case")
  void deleteMunicipality_buildsCommandAndDelegates() {
    // Arrange
    doNothing().when(deleteMunicipalityUseCase).execute(any());

    // Act
    controller.deleteMunicipality("13034");

    // Assert
    final ArgumentCaptor<DeleteMunicipalityCommand> captor =
        ArgumentCaptor.forClass(DeleteMunicipalityCommand.class);
    verify(deleteMunicipalityUseCase).execute(captor.capture());
    assertEquals("13034", captor.getValue().id(), "command id must match");
  }

  @Test
  @DisplayName("deleteMunicipality() propagates MunicipalityNotFoundException")
  void deleteMunicipality_propagatesNotFound() {
    // Arrange
    doThrow(MunicipalityNotFoundException.becauseIdWasNotFound("99999"))
        .when(deleteMunicipalityUseCase)
        .execute(any());

    // Act & Assert
    assertThrows(MunicipalityNotFoundException.class, () -> controller.deleteMunicipality("99999"));
  }
}
