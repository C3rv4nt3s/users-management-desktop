package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.MunicipalityAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.MunicipalityResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.MunicipalityController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateMunicipalityRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.MunicipalityResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateMunicipalityHandler implements OperationHandler {

  private final MunicipalityController municipalityController;
  private final ConsoleIO console;
  private final MunicipalityResponsePrinter printer;

  @Override
  public void handle() {
    final String id           = console.readRequired("ID (5-digit code, e.g. 13034): ");
    final String name         = console.readRequired("Name                         : ");
    final String provinceName = console.readRequired("Province name                 : ");

    try {
      final MunicipalityResponse created =
          municipalityController.createMunicipality(
              new CreateMunicipalityRequest(id, name, provinceName));
      console.println("\n  Municipality created successfully.");
      printer.print(created);
    } catch (final MunicipalityAlreadyExistsException exception) {
      console.println("  Error: " + exception.getMessage());
    }
  }
}
