package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.MunicipalityAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.MunicipalityNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.MunicipalityResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for the municipality CLI handlers.
 *
 * <p>The controller is mocked; the console reads from a fixed input and writes to an in-memory
 * buffer, so the tests verify what the user would see on screen without any real terminal.
 */
@DisplayName("Municipality CLI handlers")
@ExtendWith(MockitoExtension.class)
class MunicipalityHandlersTest {

  private static final MunicipalityResponse CIUDAD_REAL =
      new MunicipalityResponse("13034", "Ciudad Real", "Ciudad Real", "13");

  @Mock private MunicipalityController municipalityController;

  private ByteArrayOutputStream output;

  @BeforeEach
  void setUp() {
    output = new ByteArrayOutputStream();
  }

  private ConsoleIO consoleWithInput(final String input) {
    return new ConsoleIO(
        new Scanner(input), new PrintStream(output, true, StandardCharsets.UTF_8));
  }

  private String screen() {
    return output.toString(StandardCharsets.UTF_8);
  }

  // ── list

  @Test
  @DisplayName("ListMunicipalitiesHandler prints every municipality returned by the controller")
  void listHandler_printsMunicipalities() {
    // Arrange
    final ConsoleIO console = consoleWithInput("");
    when(municipalityController.listAllMunicipalities()).thenReturn(List.of(CIUDAD_REAL));

    // Act
    new ListMunicipalitiesHandler(municipalityController, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertAll(
        "list output",
        () -> assertTrue(screen().contains("Total: 1 municipality(ies)")),
        () -> assertTrue(screen().contains("13034")),
        () -> assertTrue(screen().contains("Ciudad Real")));
  }

  @Test
  @DisplayName("ListMunicipalitiesHandler tells the user when there are no municipalities")
  void listHandler_printsMessageWhenEmpty() {
    // Arrange
    final ConsoleIO console = consoleWithInput("");
    when(municipalityController.listAllMunicipalities()).thenReturn(List.of());

    // Act
    new ListMunicipalitiesHandler(municipalityController, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertTrue(screen().contains("No municipalities found."));
  }

  // ── find

  @Test
  @DisplayName("FindMunicipalityByIdHandler prints the municipality found")
  void findHandler_printsMunicipality() {
    // Arrange
    final ConsoleIO console = consoleWithInput("13034\n");
    when(municipalityController.findMunicipalityById("13034")).thenReturn(CIUDAD_REAL);

    // Act
    new FindMunicipalityByIdHandler(
            municipalityController, console, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertTrue(screen().contains("Ciudad Real"));
  }

  @Test
  @DisplayName("FindMunicipalityByIdHandler reports 'Not found' when the id does not exist")
  void findHandler_reportsNotFound() {
    // Arrange
    final ConsoleIO console = consoleWithInput("99999\n");
    when(municipalityController.findMunicipalityById("99999"))
        .thenThrow(MunicipalityNotFoundException.becauseIdWasNotFound("99999"));

    // Act
    new FindMunicipalityByIdHandler(
            municipalityController, console, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertTrue(screen().contains("Not found:"));
  }

  // ── create

  @Test
  @DisplayName("CreateMunicipalityHandler creates the municipality and shows it")
  void createHandler_createsMunicipality() {
    // Arrange
    final ConsoleIO console = consoleWithInput("13034\nCiudad Real\nCiudad Real\n");
    when(municipalityController.createMunicipality(any())).thenReturn(CIUDAD_REAL);

    // Act
    new CreateMunicipalityHandler(
            municipalityController, console, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertAll(
        "create output",
        () -> assertTrue(screen().contains("Municipality created successfully.")),
        () -> assertTrue(screen().contains("13034")));
  }

  @Test
  @DisplayName("CreateMunicipalityHandler reports the error when the id already exists")
  void createHandler_reportsAlreadyExists() {
    // Arrange
    final ConsoleIO console = consoleWithInput("13034\nCiudad Real\nCiudad Real\n");
    when(municipalityController.createMunicipality(any()))
        .thenThrow(MunicipalityAlreadyExistsException.becauseIdAlreadyExists("13034"));

    // Act
    new CreateMunicipalityHandler(
            municipalityController, console, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertTrue(screen().contains("Error:"));
  }

  // ── update

  @Test
  @DisplayName("UpdateMunicipalityHandler updates the municipality and shows the result")
  void updateHandler_updatesMunicipality() {
    // Arrange
    final ConsoleIO console = consoleWithInput("13034\nCiudad Real Capital\nCiudad Real\n");
    when(municipalityController.updateMunicipality(any()))
        .thenReturn(new MunicipalityResponse("13034", "Ciudad Real Capital", "Ciudad Real", "13"));

    // Act
    new UpdateMunicipalityHandler(
            municipalityController, console, new MunicipalityResponsePrinter(console))
        .handle();

    // Assert
    assertAll(
        "update output",
        () -> assertTrue(screen().contains("Municipality updated successfully.")),
        () -> assertTrue(screen().contains("Ciudad Real Capital")));
  }

  // ── delete

  @Test
  @DisplayName("DeleteMunicipalityHandler deletes the municipality")
  void deleteHandler_deletesMunicipality() {
    // Arrange
    final ConsoleIO console = consoleWithInput("13034\n");

    // Act
    new DeleteMunicipalityHandler(municipalityController, console).handle();

    // Assert
    verify(municipalityController).deleteMunicipality("13034");
    assertTrue(screen().contains("Municipality deleted successfully."));
  }
}
